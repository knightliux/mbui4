package com.bestbaan.moonbox.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

public class SystemInfoUtil {
     public static void getInfo(){
    	 String str1 = "/system/build.prop";  
         String str2="";  
         StringBuffer strbf=new StringBuffer();
         try {  
             FileReader fr = new FileReader(str1);  
             BufferedReader localBufferedReader = new BufferedReader(fr, 8192);  
             while ((str2 = localBufferedReader.readLine()) != null) {  
            	 strbf.append(str2);
                 Log.i("信息", "---" + str2);  
             }  
             //Log.i("信息", strbf.toString());  
         } catch (IOException e) {  
         }  
     }
}
