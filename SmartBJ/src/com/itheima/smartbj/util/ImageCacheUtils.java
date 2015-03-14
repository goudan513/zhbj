package com.itheima.smartbj.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class ImageCacheUtils {
	
	public static final int SUCCESS = 1;
	public static final int FAILURE = 2;
	private Handler handler;
	private ExecutorService pool;
	private File cacheFileDir;
	private LruCache<String, Bitmap> cacheImages;
	
	public ImageCacheUtils(Context context,Handler handler){
		this.handler = handler;
		pool = Executors.newFixedThreadPool(5);
		cacheFileDir = context.getCacheDir();
		
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		cacheImages = new LruCache<String, Bitmap>(maxSize){

			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	private Bitmap getFileCache(String url){
		String fileName = MD5Utils.encode(url).substring(0,10);
		File file = new File(cacheFileDir,fileName);
		if (file.exists()){
			try {
				Bitmap decodeStream = BitmapFactory
						.decodeStream(new FileInputStream(file));
				return decodeStream;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		}
		return null;
		
	}
	
	public Bitmap getBitmap(String url,int tag){
		Bitmap bitmap = cacheImages.get(url);
		if (bitmap != null){
			return bitmap;
		}
		
		bitmap = getFileCache(url);
		if (bitmap != null){
			return bitmap;
		}
		
		getBitmapFromNet(url,tag);
		
		return bitmap;
	}
	
	private void saveCacheFile(String url,Bitmap bitmap){
		String fileName = MD5Utils.encode(url).substring(0,10);
		File file = new File(cacheFileDir,fileName);
		try {
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[10240];
			int len = is.read(buffer);
			while (len != -1){
				fos.write(buffer, 0, len);
				len = is.read(buffer);
			}
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

	private void getBitmapFromNet(final String url, final int tag) {
		pool.execute(new Runnable(){

			@Override
			public void run() {
				URL u;
				HttpURLConnection con = null;
				try {
					u = new URL(url);
					con = (HttpURLConnection) u.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(5000);
					con.setReadTimeout(5000);
					int responseCode = con.getResponseCode();
					if (responseCode == 200) {
						InputStream inputStream = con.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
						//保存到缓存
						cacheImages.put(url, bitmap);
						
						//保存到本地
						saveCacheFile(url, bitmap);
						
						Message message = handler.obtainMessage();
						message.obj = bitmap;
						message.arg1 = tag;
						message.what = SUCCESS;
						message.sendToTarget();
						return;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (con != null) {
						con.disconnect();
					}
				}
				handler.obtainMessage(FAILURE).sendToTarget();
			}// end run
			
		});//end pool execute;
	}
	
	
}
