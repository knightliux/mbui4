package com.moon.android.iptv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.bestbaan.moonbox.database.RegionLimitDAO;
import com.bestbaan.moonbox.database.UserMsgDAO;
import com.bestbaan.moonbox.model.AdMsg;
import com.bestbaan.moonbox.model.LauncherMsg;
import com.bestbaan.moonbox.model.Regionlimit;
import com.bestbaan.moonbox.model.STBInfo;
import com.bestbaan.moonbox.model.UpdateData;
import com.bestbaan.moonbox.model.UserMsg;
import com.bestbaan.moonbox.util.Logger;
import com.bestbaan.moonbox.util.MACUtils;
import com.bestbaan.moonbox.util.NetworkUtil;
import com.bestbaan.moonbox.util.RequestUtil;
import com.bestbaan.moonbox.util.VersionUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MsgService extends Service {
	private static Logger logger = Logger.getInstance();
	private Timer mTimerMsg2 = new Timer(true);
	private Timer mTimerUpgrade2 = new Timer(true);
	private Timer mTimerAdPic = new Timer(true);
	private Timer mTimerAdPic2 = new Timer(true);
	private Timer mTimerLauncherMsg = new Timer(true);
	private Timer mTimerGetHardwareMsg = new Timer(true);
	private Timer mTimerRegionFirstLimit = new Timer(true);
	private Timer mTimerRegionLimit = new Timer(true);
	
	private boolean isUpdate = false;
	private boolean isGetRegionLimit = false;
	private boolean isGetHardWare = false;
	private boolean isGetAdPic = false;
	
	public IBinder onBind(Intent paramIntent) {
		return null;
	}
	
	private void getRegionLimit() {
		if(!isGetHardWare) return;
		String requestStr = new Configs().new ServerInterface().REGION_LIMIT +"fver="+Declare.SOFTWRAE_NAME+
					"&mver="+VersionUtil.getVersionName(getApplicationContext())+"&mac="
					+MACUtils.getMac()+"&model="+ Declare.HARDWARE_MODEL;
		String requestResult = RequestUtil.getInstance().request(requestStr);
		logger.i(requestResult);
		if(!isBlank(requestResult)){
			try{  
				Regionlimit regionLimit = new Gson().fromJson(requestResult, new TypeToken<Regionlimit>(){}.getType());
				if(null != regionLimit){
					String code = regionLimit.getCode();
					//每次取到一次状态后，就存储起来，下次启动如果未联网 的话就取数据库的信息来判断是否已经开启机顶盒、
					new RegionLimitDAO(getApplicationContext()).changeLoadStatus(code,regionLimit.getMsg());	
					//end
					logger.i("Regtion limit status = " + code);
					isGetRegionLimit = true;
					Intent intent = new Intent();
					intent.setAction(Configs.RegionLimit.ACTION_REGION_LIMIT);
					intent.putExtra(Configs.PARAM_1, regionLimit);
					sendBroadcast(intent);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static final int regionLimitPeiodTime = 60 * 30;  //second
	int i = 0;
	public void onCreate() {
		super.onCreate();
		
		/*
		 * 区域限制功能固定 半个小时取一次
		 * 如果设置不验证区域，则不验证
		 */
		if(Configs.isVerifyRegionRestrictions){
			mTimerRegionLimit.schedule(new TimerTask() {
				@Override
				public void run() {
					if(isGetHardWare){
						getRegionLimit();
					}
				}
			}, regionLimitPeiodTime * 1000L, regionLimitPeiodTime * 1000L);
		}
		
		/*
		 * 区域限制刚进入的时候 从第5s开始 每隔 5s取一次数据 知道取到区域限制信息、
		 * 如果网络是断的，则取上一次的区域限制信息
		 * 
		 */
		if(Configs.isVerifyRegionRestrictions){
			mTimerRegionFirstLimit.schedule(new TimerTask() {
				String limitStatus = "";
				Regionlimit regionLimit;
				@Override
				public void run() {
					if(NetworkUtil.isConnectingToInternet(MsgService.this)){
						i++;
						if(!isGetRegionLimit && i <= 5){
							if(isGetHardWare){
								getRegionLimit();
							}
						} else cancel();
					} else {
						if("".equals(limitStatus)){
							try{
								regionLimit = new RegionLimitDAO(getApplicationContext()).getRegionLimit();
								if(null != regionLimit){
									Intent intent = new Intent();
									intent.setAction(Configs.RegionLimit.ACTION_REGION_LIMIT);
									limitStatus = regionLimit.getCode();
									intent.putExtra(Configs.PARAM_1, regionLimit);
									sendBroadcast(intent);
								} 
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
			}, 5000L, 5000L);
		}
		
		mTimerLauncherMsg.schedule(new TimerTask() {
			@Override
			public void run() {
				List<LauncherMsg> list = loadLauncherMsg();
				try{
					if(list.size() > 0 && !isBlank(list.get(0).getTitle())){
						Intent intent = new Intent();
						intent.setAction(Configs.BroadCastConstant.GET_LAUNCHER_MSG);
						intent.putExtra(Configs.PARAM_1,(Serializable)list);
						sendBroadcast(intent);
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}, 1000L,60 * 60 * 1000L);
		
		/*
		 * 主页广告图片  
		 * 进去第10分钟开始 每隔60分钟取一次是否广告图片更新 
		 */
		mTimerAdPic2.schedule(new TimerTask() {
			public void run() {
				loadAdPic();
			}
		}, 10 * 60 * 1000L, 60 * 60 * 1000L);
		
		
		
		mTimerAdPic.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!isGetAdPic){
					logger.i("获取广告图片");
					loadAdPic();
				} else {
					logger.i("获取广告图片成功，以后每小时获取一次广告图片");
					cancel();
				}
			}
		}, 5 * 1000L, 5 * 1000L);
		
		/*
		 * 我的MoonBox  中的消息
		 * 1分钟时开始取    1个小时获取一次 
		 */
		mTimerMsg2.schedule(new TimerTask() {
			public void run() {
				loadUserMsg();
			}
		}, 60 * 1000, 60 * 60 * 1000);
		
		/*
		 * 检测升级
		 * 第10s开始检测，平均5s检测一次 直到检测到升级消息为止
		 */
		mTimerUpgrade2.schedule(new TimerTask() {
			public void run() {
				if(!isUpdate){
					UpdateData updateData = checkUpate(MsgService.this);
					try{
						if (null != updateData) {
							isUpdate = true;
							if(null != updateData.getCode() && "1".equals(updateData.getCode())){
								logger.d("***************************************************");
								logger.d("****** MBUI3 has no update  ");
								logger.d("***************************************************");
							}
							if(!isBlank(updateData.getCode()) && "0".equals(updateData.getCode())){
								Intent intent = new Intent();
								intent.setAction(Configs.BroadCastConstant.ACTION_UPGRADE);
								LauncherApplication.updateData = updateData;
								sendBroadcast(intent);
								logger.d("***************************************************");
								logger.d("****** MBUI3 has update  ");
								logger.d("****** Send update broadcast  ");
								logger.d("***************************************************");
							}
						} else {
							logger.d("***************************************************");
							logger.d("******  Not get MBUI3 update message  ");
							logger.d("******  Send reqeust repeat 3s later  ");
							logger.d("***************************************************");
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}, 10000L,20 * 1000L);
		
		/*
		 * 取硬件信息
		 * 进入即开始取，平均3s一次 直到取到信息为止
		 */
		mTimerGetHardwareMsg.schedule(new TimerTask() {
			@Override
			public void run() {
				if(!isGetHardWare)
					getHardware();
				else  cancel();
			}
		}, 1000L, 5 * 1000L);
		return;
	}
	
	public static UpdateData checkUpate(Context context) {
		try {
			String requestUrl = new Configs().new ServerInterface().UPGRADE_LAUNCHER+
					"&version=" + VersionUtil.getVersionName(context) + "&mac="
					+ MACUtils.getMac();
			String requestResult = RequestUtil.getInstance().request(requestUrl);
			if (isBlank(requestResult))
				return null;
			UpdateData updateData = (UpdateData) new Gson().fromJson(requestResult,
					new TypeToken<UpdateData>() {
					}.getType());
			if ((updateData != null)) {
//				if ((null != updateData.getCode())
//						&& (!updateData.getCode().equals(""))
//						&& (updateData.getCode().equals("0"))) {
//					return updateData;
//				}
				return updateData;
			}
		} catch (Exception localException) {
			return null;
		}
		return null;
	}
	
	private static boolean isBlank(String paramString) {
		return (paramString == null) || (paramString.trim().equals(""));
	}
	
	private void getHardware(){
		String msgReturnStr = RequestUtil.getInstance().request(new Configs().new ServerInterface().SOFTWARE_MSG +"mac="+MACUtils.getMac());
		STBInfo stbInfo = null;
		try{
			stbInfo = new Gson().fromJson(msgReturnStr, new TypeToken<STBInfo>(){}.getType());
		}catch(Exception e){
			e.printStackTrace();
		}
		if(null != stbInfo){
			isGetHardWare = true;
			if("0".equals(stbInfo.getResult())){
				Declare.HARDWARE_MODEL = stbInfo.getModel();
				Declare.SOFTWRAE_NAME = stbInfo.getFirmware();
				Intent intent = new Intent(Configs.BroadCastConstant.ACTION_GET_HARDWARE);
				sendBroadcast(intent);
			}
		} 
	}
	private List<LauncherMsg> loadLauncherMsg() {
		String msgReturnStr = RequestUtil.getInstance().request(new Configs().new ServerInterface().MSG_LAUNCHER +"mac="+MACUtils.getMac()+"&firmware="+Declare.SOFTWRAE_NAME);
		List<LauncherMsg> list = convertGsonFromLauncherMsgString(msgReturnStr);
		return list;
	}

	private void loadUserMsg() {
		UserMsgDAO msgDAO = new UserMsgDAO(LauncherApplication.getApplication());
		if(msgDAO.checkIsNull()){
			insertUserMsg(msgDAO,"0");
			logger.i("user msg table is null, init insert");
			Intent intent = new Intent(Configs.BroadCastConstant.GET_USER_MSG);
			sendBroadcast(intent);
		} 
		else {
			String maxMsgId = msgDAO.getMaxId();
			logger.i("My moonbox max id = " + maxMsgId);
			if(insertUserMsg(msgDAO,maxMsgId)){
				logger.i("Get new user msg!");
				Intent intent = new Intent(Configs.BroadCastConstant.GET_USER_MSG);
				sendBroadcast(intent);
			} 
		}
	}
	
	boolean canInsert = true;
	private boolean insertUserMsg(UserMsgDAO msgDAO,String msgid){
		String msgReturnStr = RequestUtil.getInstance().request(
				new Configs().new ServerInterface().MSG_CENTER +"mac="+MACUtils.getMac()+"&firmware="+Declare.SOFTWRAE_NAME+"&msgid="+msgid);
		List<UserMsg> list = convertGsonFromUsermsgString(msgReturnStr);
		if(null != list && list.size() > 0 && canInsert == true){
			canInsert = false;
			for(UserMsg userMsg : list){
				try{
					logger.i("insert msg title : " + userMsg.getTitle());
					msgDAO.insert(userMsg);
				}catch(Exception e){
					continue;
				}
			}
			canInsert = true;
			return true;
		}
		return false;
	}
	
	private void loadAdPic() {
		String adReturnStr = RequestUtil.getInstance().request(new Configs().new ServerInterface().AD_PICTURE +"mac="+MACUtils.getMac()+"&firmware="+Declare.SOFTWRAE_NAME);
		List<AdMsg> listAdMsg = convertGsonFromAdmsgString(adReturnStr);
		if(listAdMsg.size() > 0){
			isGetAdPic = true;
			Intent intent = new Intent(Configs.BroadCastConstant.GET_AD_PICTURE);
			intent.putExtra(Configs.PARAM_1, (Serializable)listAdMsg);
			sendBroadcast(intent);
			logger.i("发送获取到广告图片的广播");
		}
	}
	
	public static List<LauncherMsg> convertGsonFromLauncherMsgString(String gsonStr){
		String jsonResultStr = gsonStr;
		List<LauncherMsg> list = new ArrayList<LauncherMsg>();
		try{
			int startSymbol = gsonStr.indexOf("[");
			int endSymbol = gsonStr.lastIndexOf("]");
			jsonResultStr = gsonStr.substring(startSymbol, endSymbol + 1);
			list = new Gson().fromJson(jsonResultStr, new TypeToken<List<LauncherMsg>>(){}.getType());
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<UserMsg> convertGsonFromUsermsgString(String gsonStr){
		String jsonResultStr;
		logger.i(gsonStr);
		List<UserMsg> list = new ArrayList<UserMsg>();
		try{
			int startSymbol = gsonStr.indexOf("[");
			int endSymbol = gsonStr.lastIndexOf("]");
			jsonResultStr = gsonStr.substring(startSymbol, endSymbol + 1);
			list = new Gson().fromJson(jsonResultStr, new TypeToken<List<UserMsg>>(){}.getType());
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<AdMsg> convertGsonFromAdmsgString(String gsonStr){
		String jsonResultStr;
		List<AdMsg> list = new ArrayList<AdMsg>();
		try{
			int startSymbol = gsonStr.indexOf("[");
			int endSymbol = gsonStr.lastIndexOf("]");
			jsonResultStr = gsonStr.substring(startSymbol, endSymbol + 1);
			list = new Gson().fromJson(jsonResultStr, new TypeToken<List<AdMsg>>(){}.getType());
		} catch(Exception e){
			
		}
		return list;
	}
	
	public void onDestroy() {
		try {
			mTimerUpgrade2.cancel();
			mTimerMsg2.cancel();
			mTimerAdPic2.cancel();
			mTimerLauncherMsg.cancel();
		} catch (Exception localException) {
		}
		super.onDestroy();
	}
}
