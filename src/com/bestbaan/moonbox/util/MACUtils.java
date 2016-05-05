package com.bestbaan.moonbox.util;

import java.io.FileInputStream;
import java.io.IOException;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class MACUtils {

	public static String getMac() {
		FileInputStream localFileInputStream;
		String str = "";
		try{  
			localFileInputStream = new FileInputStream(
					"/sys/class/net/eth0/address");
			byte[] arrayOfByte = new byte[17];
			localFileInputStream.read(arrayOfByte, 0, 17);
			str = new String(arrayOfByte);
			localFileInputStream.close();
			if (str.contains(":"))
				str = str.replace(":", "").trim();
			if (str.contains("-"))
				str = str.replace("-", "").trim();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return str.toLowerCase();
//		return "002157f3b51c";
	}
	
	public static String getWifiMac(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		WifiInfo info = wifi.getConnectionInfo(); 
		return info.getMacAddress();
	}
	
	public static String getChipMode(){
		return null;
	}
}
