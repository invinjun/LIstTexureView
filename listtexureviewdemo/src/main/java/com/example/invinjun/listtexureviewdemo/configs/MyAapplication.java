package com.example.invinjun.listtexureviewdemo.configs;

import android.app.Application;
import android.content.Context;



public class MyAapplication extends Application {
@Override
public void onCreate() {
	// TODO Auto-generated method stub
	initImageLoader(getApplicationContext());//初始化图片缓存 
	super.onCreate();
}
/**
 * 配置imageLoadger
 *  Configuration of ImageLoader:
 *  This configuration tuning is custom.
 *  You can tune every option, you may tune some of them,
 *  or you can create default configuration by
 *  ImageLoaderConfiguration.createDefault(this) method.
 *  
 *  Note:
 *  1 enableLogging() // Not necessary in common
 */
public static void initImageLoader(Context context) {

}
}
