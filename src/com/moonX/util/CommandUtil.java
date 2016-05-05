package com.moonX.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.R.bool;
import android.util.Log;

public class CommandUtil {
	public static boolean DoShell(String commod) {
		boolean resault=false;
		Process p;
		try {
	
			p = Runtime.getRuntime().exec(commod);
			int status = p.waitFor();
			//System.out.println(status+"status");
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
			//	System.out.println(line);
				buffer.append(line);
			}
		//	System.out.println("Return ============" + buffer.toString());
			if (status == 0) {
				resault =true;
			} else {
				resault =false;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			return resault;
		} catch (InterruptedException e) {
		
			e.printStackTrace();
			return resault;
		}
//		return resault;
		return resault;
	}
	public static boolean Ping(String str) {
		boolean resault=false;
		Process p;
		try {
			// ping -c 3 -w 100 中 ，-c 是指ping的次数 3是指ping 3次 ，-w 10
			// 以秒为单位指定超时间隔，是指超时时间为10秒
			p = Runtime.getRuntime().exec("ping -c 3 -w 10 " + str);
			int status = p.waitFor();
			//System.out.println(status+"status");
			InputStream input = p.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = in.readLine()) != null) {
			//	System.out.println(line);
				buffer.append(line);
			}
		//	System.out.println("Return ============" + buffer.toString());
			if (status == 0) {
				resault =true;
			} else {
				resault =false;
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			return resault;
		} catch (InterruptedException e) {
		
			e.printStackTrace();
			return resault;
		}
//		return resault;
		return resault;
	}

}
