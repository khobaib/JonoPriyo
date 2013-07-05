package com.priyo.apps.lazylist;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.priyo.apps.jonopriyo.R;

import android.os.Handler;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class ImageLoader {
    
    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Activity mContext;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;
    Handler handler=new Handler();//handler to display images in UI thread
    
    public ImageLoader(Activity context){
    	this.mContext = context;
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }
    
    final int stub_id= R.drawable.image_back;
    public void DisplayImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
            imageView.setImageBitmap(resizeBitmap(bitmap));
        }
        else
        {
            queuePhoto(url, imageView, 400);
            imageView.setImageResource(stub_id);
        }
    }
    
    public void DisplaySplashImage(String url, ImageView imageView)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
            imageView.setImageBitmap(resizeBitmap_splash(bitmap));
        }
        else
        {
            Bitmap psiBmp = getBitmap(url, 2000);
            imageView.setImageBitmap(resizeBitmap_splash(psiBmp));
//            queuePhoto(url, imageView, 2000);
//            imageView.setImageResource(stub_id);
        }
    }
    
    
	private Bitmap resizeBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		Bitmap temp = null;
		try{
//			Logger.logger("bitmap size in resizeBitmap"+bitmap.getRowBytes());
			if (bitmap != null) {
				WindowManager w = mContext.getWindowManager();
				Point size = new Point();
				int Measuredwidth = 0;
				int Measuredheight = 0;
				float witdh,height;
				if(bitmap.getWidth()>mContext.getWindowManager().getDefaultDisplay().getWidth())
				{				
					Display d = w.getDefaultDisplay();
					Measuredwidth = d.getWidth();
					Measuredheight = d.getHeight();					
					Log.e("Measuredwidth",""+Measuredwidth);
					Log.e("Measuredheight",""+Measuredheight);
					if(Measuredheight<=800){
						witdh = 400;
						height = 400;
					}
					else{					
						witdh = Measuredheight;
						height = Measuredwidth;			
					}
					float retion=((float)witdh)/((float)bitmap.getWidth());
					witdh=((float)bitmap.getWidth())*retion;
					height=((float)bitmap.getHeight())*retion;

					temp=bitmap.createScaledBitmap(bitmap, (int)(witdh), (int)(height), true);
				}else
					temp=bitmap;
			}

		} catch(Exception e){
			temp=bitmap; 
			e.printStackTrace();
		}
		return temp;
	}
	
	private Bitmap resizeBitmap_splash(Bitmap bitmap) {
		// TODO Auto-generated method stub
		Bitmap temp = null;
		try{
//			Logger.logger("bitmap size"+bitmap.getRowBytes());
			if (bitmap != null) {
				WindowManager w = mContext.getWindowManager();

				int Measuredwidth = 0;
				int Measuredheight = 0;
				float width,height;

				Display d = w.getDefaultDisplay();
				Measuredwidth = d.getWidth();
				Measuredheight = d.getHeight();	
//				Logger.logger("========", "Measuredwidth = "  + Measuredwidth + " & Measuredheight = " + Measuredheight);
				//					Log.e("Measuredwidth",""+Measuredwidth);
				//					Log.e("Measuredheight",""+Measuredheight);

//				Logger.logger("========", "bitmap width = "  + bitmap.getWidth() + " & bitmap height = " + bitmap.getHeight());
				float retion=((float)Measuredwidth)/((float)bitmap.getWidth());
				width=((float)bitmap.getWidth())*retion;
				height=((float)bitmap.getHeight())*retion;
//				Logger.logger("on screen", "bitmap width = "  + width + " & bitmap height = " + height);
				//				 Log.e("witdh",""+witdh);
				//					Log.e("height",""+height);
				temp = bitmap.createScaledBitmap(bitmap, (int)(width), (int)(height), true);

			}

		} catch(Exception e){
			temp = bitmap; 
			e.printStackTrace();
		}
		return temp;
	}
        
    private void queuePhoto(String url, ImageView imageView, int imageQuality)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p, imageQuality));
    }
    
    public Bitmap getBitmap(String url, int imageQuality) 
    {
        File f=fileCache.getFile(url);
         
        //from SD cache
        Bitmap b = decodeFile(f, imageQuality);
        if(b!=null)
            return b;
        
        //from web
        try {
            Bitmap bitmap=null;
            Log.d("-------", url);
            if(!url.startsWith("http:")){
                url = "http:" + url;
            }
            URL imageUrl = new URL(url);
            Log.d("-------", url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            conn.disconnect();
            bitmap = decodeFile(f, imageQuality);
            return bitmap;
        } catch (Throwable ex){
           ex.printStackTrace();
           if(ex instanceof OutOfMemoryError)
               memoryCache.clear();
           return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, int imageQuality){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=imageQuality;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
    
    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        int imageQuality;
        PhotosLoader(PhotoToLoad photoToLoad, int imageQuality){
            this.photoToLoad=photoToLoad;
            this.imageQuality = imageQuality;
        }
        
        @Override
        public void run() {
            try{
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url, imageQuality);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                handler.post(bd);
            }catch(Throwable th){
                th.printStackTrace();
            }
        }
    }
    
    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }
    
    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

}
