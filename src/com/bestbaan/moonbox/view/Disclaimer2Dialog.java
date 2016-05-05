package com.bestbaan.moonbox.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.moonbox.android.iptv.R;


public class Disclaimer2Dialog{
	
	private Context mContext;
    private AlertDialog mDialog = null;
	
	public Disclaimer2Dialog(Context ctx){
		mContext = ctx;
		mDialog = new AlertDialog.Builder(mContext,R.style.disclaimer_dialog).create();
	}	
	
	public void setContentView(int resid){
		mDialog.setContentView(resid);
		TextView content = (TextView) mDialog.findViewById(R.id.disclaimer_content);
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
	public void setContentView(View v){
		mDialog.setContentView(v);
		TextView content = (TextView) mDialog.findViewById(R.id.disclaimer_content);
		content.setMovementMethod(ScrollingMovementMethod.getInstance());
	}
	
	public boolean isShowing(){
		return mDialog.isShowing();
	}
	
	public void setMessage(String msg){
		mDialog.setMessage(msg);
	}
	
	public void setCancelable(boolean cancelable){
		mDialog.setCancelable(cancelable);
	}
	
	public void setTitle(int resId){
		mDialog.setTitle(resId);
	}
	
	public void setTitle(String title){
		mDialog.setTitle(title);
	}
	
	public void show(){
		mDialog.show();
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}
	
//	public void setPositiveButton(String text, OnClickListener listener, int count){
//		text = getTimeText(text, count);
//		mDialog.setButton(Dialog.BUTTON_POSITIVE, text, listener);
//	}
//	
//	public void setNegativeButton(String text, OnClickListener listener, int count){
//		text = getTimeText(text, count);
//		mDialog.setButton(Dialog.BUTTON_NEGATIVE, text, listener);
//	}
	
	public  int mReadTimes = 15;
	public static final int READ_DISCLAIMER = 0x100;
	public void startTiming(){
		mHandler.sendEmptyMessage(READ_DISCLAIMER);
	}
    
	public OnClickListener mCommitCL = null;
	
	public void setCommitClickListener(OnClickListener cl){
		mCommitCL = cl;
	}
	
    private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
        	switch(msg.what){
        	case READ_DISCLAIMER:
        		if(mReadTimes > 0){
	        		Button b = (Button) mDialog.findViewById(R.id.commit_button);
	        		b.setClickable(false);
	        		if(null != b) 
	        			b.setText(getTimeText(mContext.getText(R.string.read_disclaimer).toString(), mReadTimes));
	        		mHandler.sendEmptyMessageDelayed(READ_DISCLAIMER, 1000);
	        		mReadTimes--;
        		} else {
        			Button b = (Button) mDialog.findViewById(R.id.commit_button);
        			b.setFocusable(true);
        			b.requestFocus();
        			b.requestFocusFromTouch();
        			if(null != mCommitCL)
        				b.setOnClickListener(mCommitCL);
//        			b.setOnClickListener(new android.view.View.OnClickListener() {
//						@Override
//						public void onClick(View arg0) {
//							mDialog.dismiss();
//						}
//					});
        			b.setText(mContext.getText(R.string.agree_disclaimer).toString());
        			if(null != b) {
        				b.setClickable(true);
        				b.requestFocus();
        				b.requestFocusFromTouch();
        			}
        		}
        		break;
        	}
        }
    };
    
    
    private String getTimeText(String text, int count){
    	if(text != null && text.length() > 0 && count > 0){
    		int index = text.indexOf("[");
    		if(index > 0){
    			text = text.substring(0, index);
    			return (text + "["+count+"s]");
    		}else{
    			return (text + "["+count+"s]");
    		}
    	}
    	return text;
    }

}
