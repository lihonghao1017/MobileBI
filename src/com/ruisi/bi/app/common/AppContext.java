package com.ruisi.bi.app.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.ruisi.bi.app.util.ImageUtil;

public class AppContext extends Application {
	private static AppContext context;
	public static int currentPage = 1;
	public static DisplayMetrics dm;
	private static DisplayImageOptions options;
	public static int video_progress;
	public static String avatarImageName;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		context = this;
		// 关闭友盟默认的统计方式
		dm = new DisplayMetrics();
		super.onCreate();

		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
//				.cacheOnDisc(true).cacheInMemory(true).cacheOnDisk(true)
//				.imageScaleType(ImageScaleType.EXACTLY).build();
		File cacheDir = ImageUtil.creatFile(ImageUtil.getSavePath()+"/cacheImg/Cache");
		if  (!cacheDir .exists()  && !cacheDir .isDirectory())      
		{       
		    cacheDir .mkdir();    
		}
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				context).defaultDisplayImageOptions(getOptions())
				.discCacheSize(50 * 1024 * 1024).memoryCacheSizePercentage(13)
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
				.memoryCacheExtraOptions(480, 480)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.threadPriority(Thread.NORM_PRIORITY - 1);
		ImageLoaderConfiguration imgConfig = builder.build();
		com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(
				imgConfig);
	}

	public static DisplayImageOptions getOptions() {
		if (options == null) {
			options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).cacheOnDisc(true)
					.imageScaleType(ImageScaleType.EXACTLY).build();
		}
		return options;
	}

	/**
	 * åˆ¤æ–­å½“å‰�ç½‘ç»œæ˜¯å�¦è¿žæŽ¥
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return null != networkInfo && networkInfo.isConnected();
	}

	/**
	 * åˆ¤æ–­å½“å‰�ç½‘ç»œæ˜¯å�¦ä¸ºWIFI
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiNetwork(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (null != netInfo
				&& netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	private List<Activity> mList = new LinkedList<Activity>();

	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {

		try {
			for (Activity activity : mList) {
				if (activity != null) {
					activity.finish();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// ç»“æ�Ÿè¿›ç¨‹
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		System.gc();
	}

	private boolean getSourceFile(int id, String name, String folder) {
		File file = null;
		boolean bResult = false;
		try {
			File cascadeDir = getDir(folder, Context.MODE_PRIVATE);
			file = new File(cascadeDir, name);
			if (!file.exists()) {
				InputStream is = getResources().openRawResource(id);
				FileOutputStream os = new FileOutputStream(file);

				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = is.read(buffer)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				is.close();
				os.close();
			}
			bResult = true;
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("CidtechLMS", "Failed to load file " + name
					+ ". Exception thrown: " + e);
		}

		return bResult;
	}

	public Context getInstance() {
		// TODO Auto-generated method stub
		return this;
	}

	public static Context getApplication() {
		// TODO Auto-generated method stub
		return context;
	}

}
