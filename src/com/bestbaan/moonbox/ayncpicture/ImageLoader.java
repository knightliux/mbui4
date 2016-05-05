package com.bestbaan.moonbox.ayncpicture;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.moonbox.android.iptv.R;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private boolean isShowShadow;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    
    public ImageLoader(Context context){
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY-1);
        
        fileCache=new FileCache(context);
    }
    
    final int stub_id=R.drawable.pic_loading;
    public void DisplayImage(String url, Context activity, ImageView imageView,boolean isShowShadow)
    {
        imageViews.put(imageView, url);
        this.isShowShadow = isShowShadow;
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
        	AlphaAnimation animation = new AlphaAnimation(0,1);
        	animation.setDuration(1000);
        	imageView.setAnimation(animation);
            imageView.setImageBitmap(bitmap);}
        else
        {
            queuePhoto(url, activity, imageView);
            imageView.setImageResource(stub_id);
        }    
    }
        
    private void queuePhoto(String url, Context activity, ImageView imageView)
    {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        photosQueue.Clean(imageView);
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        synchronized(photosQueue.photosToLoad){
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }
        
        //start thread if it's not started yet
        if(photoLoaderThread.getState()==Thread.State.NEW)
            photoLoaderThread.start();
    }
    
    private Bitmap getBitmap(String url) 
    {
        File f=fileCache.getFile(url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        if(b!=null){
            return b;
        }
        
        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex){
           ex.printStackTrace();
           return null;
        }
    }

    Bitmap bitmapOrigin  = null;
    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
    	FileInputStream fis = null;
    	Bitmap bitmapOrigin  = null;
    	try {
        	if(null == f) return null;
            //decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            
//            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//            
//            //Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE=70;
//            int width_tmp=o.outWidth, height_tmp=o.outHeight;
//            int scale=1;
            //low size
//            while(true){
//                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//                    break;
//                width_tmp/=2;
//                height_tmp/=2;
//                scale*=2;
//            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=1;
            o2.inPreferredConfig = Bitmap.Config.RGB_565;   
            o2.inPurgeable = true;  
            o2.inInputShareable = true;  
            fis = new FileInputStream(f);
            bitmapOrigin = BitmapFactory.decodeStream(fis, null, o2);
            if(!isShowShadow) return bitmapOrigin;
            else return createReflectedImage(bitmapOrigin);
        } catch (FileNotFoundException e) {}
        finally{
        	
        }
        return null;
    }
    

    public void recycleBitmap(){
    	if(!bitmapOrigin.isRecycled())
    		bitmapOrigin.recycle();
    }
    
 public static Bitmap createReflectedImage(Bitmap originalImage) {  
     final int reflectionGap = 4;  
     int width = originalImage.getWidth();   
     int height = originalImage.getHeight();  
   Matrix matrix = new Matrix();   
     matrix.preScale(1, -1);  
     Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,   
            height / 2, width, height / 2, matrix, false);  
     Bitmap bitmapWithReflection = Bitmap.createBitmap(width,   

             (height + height / 2), Config.ARGB_8888);  
     Canvas canvas = new Canvas(bitmapWithReflection);  
     canvas.drawBitmap(originalImage, 0, 0, null);  
     Paint defaultPaint = new Paint();   
     canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);  
     canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);  
     Paint paint = new Paint();   
     LinearGradient shader = new LinearGradient(0,   
             originalImage.getHeight(), 0, bitmapWithReflection.getHeight()   
                    + reflectionGap,0x70ffffff,0x00ffffff,  
             TileMode.MIRROR);  
     paint.setShader(shader);  
     paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
     canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()   

            + reflectionGap, paint);  

     return bitmapWithReflection;   

 } 

    
    /**
     * 
     * @param sourceImg 传入的图�?
     * @param number 0-100(0为完全�?明，100为不透明)
     * @return Bitmap 处理后的图片
     */
    public static Bitmap setAlpha(Bitmap sourceImg, int number) {   
         int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];   
         sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0,sourceImg.getWidth(), sourceImg.getHeight());   
         number = number * 255 / 100;   
         double round = (double)number/(double)(argb.length);
         System.out.println(round+ "  l="+argb.length +" n="+number);
         for (int i = 0; i < argb.length; i++) {   
          if(number-i*round>10){
              argb[i] = ((int)(number-i*round) << 24) | (argb[i] & 0x00FFFFFF);
              continue;
          }
          else{
              argb[i] = (10 << 24) | (argb[i] & 0x00FFFFFF);
              continue;
          }

         }   
         sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg.getHeight(), Config.ARGB_8888);   
         return sourceImg;   
       } 
    
    public static Bitmap setShadow(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);
        Bitmap shadowImage = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, height / 2, matrix, false);
        return shadowImage;
    }
    
    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public PhotoToLoad(String u, ImageView i){
            url=u; 
            imageView=i;
        }
    }
    
    PhotosQueue photosQueue=new PhotosQueue();
    
    public void stopThread()
    {
        photoLoaderThread.interrupt();
    }
    
    //stores list of photos to download
    class PhotosQueue
    {
        private Stack<PhotoToLoad> photosToLoad=new Stack<PhotoToLoad>();
        
        //removes all instances of this ImageView
        public void Clean(ImageView image)
        {
            for(int j=0 ;j<photosToLoad.size();){
                if(photosToLoad.get(j).imageView==image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }
    
    class PhotosLoader extends Thread {
        public void run() {
            try {
                while(true)
                {
                    //thread waits until there are any images to load in the queue
                    if(photosQueue.photosToLoad.size()==0)
                        synchronized(photosQueue.photosToLoad){
                            photosQueue.photosToLoad.wait();
                        }
                    if(photosQueue.photosToLoad.size()!=0)
                    {
                        PhotoToLoad photoToLoad;
                        synchronized(photosQueue.photosToLoad){
                            photoToLoad=photosQueue.photosToLoad.pop();
                        }
                        Bitmap bmp=getBitmap(photoToLoad.url);
                        memoryCache.put(photoToLoad.url, bmp);
                        String tag=imageViews.get(photoToLoad.imageView);
                        if(tag!=null && tag.equals(photoToLoad.url)){
                            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad.imageView);
                            Activity a=(Activity)photoToLoad.imageView.getContext();
                            a.runOnUiThread(bd);
                        }
                    }
                    if(Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }
    
    PhotosLoader photoLoaderThread=new PhotosLoader();
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageView imageView;
        public BitmapDisplayer(Bitmap b, ImageView i){bitmap=b;imageView=i;}
        public void run()
        {
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
