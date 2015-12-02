package com.example.invinjun.listtexureviewdemo.activity;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.invinjun.listtexureviewdemo.R;
import com.example.invinjun.listtexureviewdemo.cacheplayer.service.NetBroadCastReciver;
import com.example.invinjun.listtexureviewdemo.configs.Configs;
import com.example.invinjun.listtexureviewdemo.dao.Weibo;
import com.example.invinjun.listtexureviewdemo.dbutil.Downloader;
import com.example.invinjun.listtexureviewdemo.utils.FileUtil;
import com.example.invinjun.listtexureviewdemo.utils.MD5Util;
import com.example.invinjun.listtexureviewdemo.utils.NetWorkUtil;
import com.example.invinjun.listtexureviewdemo.view.MyAdapter;
import com.example.invinjun.listtexureviewdemo.view.MyListView;
import com.example.invinjun.listtexureviewdemo.view.ProgressWheel;
import com.example.invinjun.listtexureviewdemo.view.VideoPlayTextureView;
import com.example.invinjun.listtexureviewdemo.view.XListView;


public class TestActivity extends Activity implements MyAdapter.videoPlayListener,OnClickListener, XListView.IXListViewListener, MyListView.PlayIngListener, OnCompletionListener{
	private int vHeight;
	private int vWidth;
	private String TAG="Downloader";
	private String MesgTAG="message";
	private List<Weibo> weibos=new ArrayList<Weibo>();
	private SharedPreferences mPreferences;
	private MyListView lv;
	private ProgressWheel loading;
	private VideoPlayTextureView	textureView;
	private MyAdapter mAdapter;
	private NetBroadCastReciver recriver;
	private int from;
	private boolean isCloseed=true;
	private String lastURL;
	private String FileName;
	private ImageView  video;
	private ImageView  videoPlay;
	private Downloader downloader;
	private String url1 = "http://qiubai-video.qiushibaike.com/JI0MRXVWWOC77JEP.mp4";

	
	private String url2 = "http://qiubai-video.qiushibaike.com/GM2OCN1JY53R30L7.mp4";


	private String url3 = "http://qiubai-video.qiushibaike.com/X82XT17A3PMRG2QN.mp4";


	private String url4 = "http://qiubai-video.qiushibaike.com/TOWORRQBDN8W390V_3g.mp4";

	private String url5 = "http://qiubai-video.qiushibaike.com/CUR8IWOVHCZ7MIYA.mp4";


	private String url6 = "http://qiubai-video.qiushibaike.com/5B5EM9KWW2V2BFQ9.mp4";


	private String url7 = "http://qiubai-video.qiushibaike.com/BRP5WS3KYA9YVCR6.mp4";
	// 存放各个下载器
	private Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	private int times=0;//onfocuse次数，第一次自动播放
	private View LastShowView;//用于保存最后播放的view，通过判断暂停滑出屏幕的view播放器
@Override
protected void onCreate(Bundle arg0) {
	requestWindowFeature(Window.FEATURE_NO_TITLE);
	setContentView(FileUtil.getLayoutResIDByName(TestActivity.this, "activity_main"));
	//网络监听 在清单文件中配置
	recriver=new NetBroadCastReciver();
	if (!NetWorkUtil.isAvailable(TestActivity.this)) {
		Toast.makeText(TestActivity.this, "网络连接不可用", Toast.LENGTH_SHORT).show();
	}
	mPreferences=getSharedPreferences(Configs.CONFIG_NAME, MODE_PRIVATE);
	from=mPreferences.getInt(Configs.NET_PARAM_NAME, Configs.WIFI);
    //获取屏幕宽高 传递给adapter和listview
    DisplayMetrics dm = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(dm);
    int density  = (int) dm.density;
    int nowHeigth = dm.heightPixels; //
    int nowWidth= dm.widthPixels;
    vHeight = nowHeigth; 
    vWidth= nowWidth;
	inintView();
	inintNetInfo();
	super.onCreate(arg0);
}
//获取网络信息
private void inintNetInfo() {
	if (NetWorkUtil.isAvailable(TestActivity.this)) {
		switch (from) {
		case Configs.WIFI:
			if (NetWorkUtil.isWifiState(TestActivity.this)) {
				isCloseed=true;
			}else if(NetWorkUtil.isGPRSState(TestActivity.this)){
				isCloseed=false;
			}
			break;
		case Configs.AUTO:
			isCloseed=true;
			break;
		case Configs.CLOSE:
			isCloseed=false;
			break;
		default:
			break;
		}
	
			}
}
@Override
protected void onResume() {
	from=mPreferences.getInt(Configs.NET_PARAM_NAME, Configs.WIFI);
	super.onResume();
}
@Override
public void onWindowFocusChanged(boolean hasFocus) {
	// TODO Auto-generated method stub
	if (times==0) {
		View viewfirst=lv.getChildAt(1);
		startDownload(viewfirst);
	}
	times++;
	super.onWindowFocusChanged(hasFocus);
}
@Override
protected void onStart() {
	IntentFilter intentFilter = new IntentFilter();
	intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");   //为BroadcastReceiver指定action，使之用于接收同action的广播
	      registerReceiver(recriver,intentFilter);
	      super.onStart();
}
@Override
protected void onStop() {
	releaseMediaplayer();
	super.onStop();
}
private void releaseMediaplayer() {
	if (textureView!=null){
		textureView.getMediaPlayer().stop();
		for (String key : downloaders.keySet()) {
			downloaders.get(key).pause();
		}
	}


}
@Override
protected void onDestroy() {
	unregisterReceiver(recriver);
	super.onDestroy();
}
private void inintView() {
	addNewData();
	lv=(MyListView) findViewById(FileUtil.getIdResIDByName(TestActivity.this, "main_listview"));
	lv.setXListViewListener(this);
	mAdapter=new MyAdapter(TestActivity.this,weibos,vWidth);
	mAdapter.setVideoListener(this);
	lv.setScreenInfo(vHeight,vWidth);
	lv.setPullLoadEnable(true);
	lv.setPlayIngListener(this);
	lv.setAdapter(mAdapter);
	lv.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
	lv.setItemChecked(0, true);
}



protected void stop(View view) {
		ImageView videoPlay = (ImageView) view
				.findViewById(FileUtil.getIdResIDByName(TestActivity.this, "play_controller"));
		ImageView video = (ImageView) view
				.findViewById(FileUtil.getIdResIDByName(TestActivity.this, "video_cover_default"));
 		VideoPlayTextureView textureView = (VideoPlayTextureView) view.findViewById(FileUtil.getIdResIDByName(TestActivity.this, "videoview"));
 		ProgressWheel loading=(ProgressWheel) view.findViewById(R.id.loading);
 		textureView.getMediaPlayer().reset();
 		video.setVisibility(View.VISIBLE);
 			
}
	
	
	

/**
 * 得到播放文件路径
 * @return
 * @throws IOException
 */
private String getPath(String name) throws IOException
{
	String path = FileUtil.setMkdir(this)+File.separator+name;
	return path;
}


//listview的刷新动作
@Override
public void onRefresh() {
	
	mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
			addDate();
			onLoad();
		}
	}, 2000);
	
}
@Override
public void onLoadMore() {
	
	mHandler.postDelayed(new Runnable() {
		@Override
		public void run() {
			addDate();
			onLoad();
		}
	}, 2000);
	
}
private void onLoad() {
		lv.stopRefresh();
		lv.stopLoadMore();
		lv.setRefreshTime("刚刚");
}
@Override
public void playTop(View view) {
	startDownload(view);
	
}
@Override
public void playBottom(View view) {

	startDownload(view);
}
@Override
public void stopTop(View view) {
	stop(view);
}
@Override
public void stopBottom(View view) {
	stop(view);
}
//播放按钮点击事件的监听
@Override
public void OnClicked(View view) {
	startDownload(view);
}
@Override
public void onClick(View v) {
	switch (v.getId()) {
	case R.id.videoview: {
		if (textureView.getMediaPlayer().isPlaying()) {
			textureView.pause();
			videoPlay.setVisibility(View.VISIBLE);
		}
	}
		break;
	default:
		break;
	}
	
}

/**
 * 响应开始下载事件
 */
public void startDownload(View view) {
		textureView = (VideoPlayTextureView) view.findViewById(R.id.videoview);
		lastURL = textureView.getVideoPath();
		loading = (ProgressWheel) view.findViewById(R.id.loading);
		pauseDownload(lastURL,view);
		Log.e(MesgTAG, "-------------------------目前view的="+lastURL);
		video = (ImageView) view.findViewById(R.id.video_cover_default);
		videoPlay = (ImageView) view.findViewById(R.id.play_controller);
		textureView.setOnClickListener(this);
		textureView.getMediaPlayer().setOnCompletionListener(this);
		FileName = MD5Util.getMd5(lastURL);
		String localfile = Environment.getExternalStorageDirectory()+ File.separator + "123456" + FileName+".mp4";
		//单线程下载
		int threadcount = 1;
		String newPath=Environment.getExternalStorageDirectory().getPath()+File.separator+"AAA"+File.separator;
		downloader = downloaders.get(lastURL);
		if (downloader == null) {
			//localfile 临时存储路径 newPath 下载完成路径
			downloader = new Downloader(lastURL, localfile,newPath, threadcount, this,mHandler);
			downloaders.put(lastURL, downloader);
		}
		
		//如果正在下载 就直接return
		if (downloader.isdownloading()) {
			return;
		}
		downloader.getDownloaderInfors();
	}

/**
 * 利用消息处理机制适时更新进度条
 */
private Handler mHandler = new Handler() {
    public void handleMessage(Message msg) {
    	switch (msg.what) {
		case 0:
			//准备下载
//    		loading.setVisibility(View.VISIBLE);
			break;
		case 1:
			if (videoPlay.getVisibility()==View.VISIBLE) {
				videoPlay.setVisibility(View.GONE);
			}
			String url1 = (String) msg.obj;
			Log.e(MesgTAG, "消息发送的:="+url1+"-------------------------目前view的="+lastURL);
			if (url1.equals(lastURL)) {
				Log.e(MesgTAG, "相同设置大小");
				 int length = msg.arg1;
		            int filesize = msg.arg2;
		            div(length, filesize, 1);
		            int res = length*360/filesize;
					loading.incrementProgress(res);
						if (loading.getVisibility()!=View.VISIBLE) {
							loading.setVisibility(View.VISIBLE);
						}
			}
			break;
		case 2:
			String message=(String) msg.obj;
			String url2 = message.substring(0,message.indexOf("`"));
			FileName=message.substring(message.indexOf("`")+1,message.length());
        	String urlstr = textureView.getVideoPath();
        	Toast.makeText(TestActivity.this, "下载完成！FileName"+urlstr, Toast.LENGTH_SHORT).show();
			textureView.prepare(FileName);
				textureView.getMediaPlayer().setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
							Log.e("加入的mediaplayer","位置" + 0 + "mediaplayer"+ textureView.getMediaPlayer());
							textureView.play();
							if (textureView.getMediaPlayer().isPlaying()) {
								Log.e("下载好的mediaplayer", textureView.getMediaState().toString());
								loading.setVisibility(View.GONE);
								video.setVisibility(View.GONE);
								videoPlay.setVisibility(View.GONE);	
							}
						}
					});
			 downloaders.get(url2).delete(url2);
             downloaders.get(url2).reset();
             downloaders.remove(url2);
			break;
		case 3:
        	if(!textureView.getMediaPlayer().isPlaying()){
        		loading.setVisibility(View.GONE);
        		video.setVisibility(View.GONE);
        		String url3 = textureView.getVideoPath();
        		String LocaPath=(String) msg.obj;
//    			String url3 = LocaPath.substring(0,LocaPath.indexOf("`"));
    			FileName=LocaPath.substring(LocaPath.indexOf("`")+1,LocaPath.length());
				textureView.prepare(FileName);
				textureView.getMediaPlayer().setOnPreparedListener(new OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mp) {
							Log.e("加入的mediaplayer","位置" + 0 + "mediaplayer"+ textureView.getMediaPlayer());
							textureView.play();
							if(textureView.getMediaPlayer().isPlaying()){
							   videoPlay.setVisibility(View.GONE);
							}
						}
					});
				textureView.getMediaPlayer().setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						videoPlay.setVisibility(View.VISIBLE);
						video.setVisibility(View.VISIBLE);
							}
						});
				  }
			break;
		case 4:
			Toast.makeText(TestActivity.this, "视频不存在", Toast.LENGTH_LONG).show();
		case 5:
			String error=(String) msg.obj;
			Toast.makeText(TestActivity.this, "服务器异常:"+error, Toast.LENGTH_SHORT).show();
		default:
			break;
		}
        }
};
/**
 * 暂停下载
 */
	public void pauseDownload(String url, View view) {
		// 如果最后一次播放的item已经滑出屏幕，则停止播放
		if (null != LastShowView && view != LastShowView) {
			stop(LastShowView);
		}
		for (String key : downloaders.keySet()) {
			if (key != url) {
				downloaders.get(key).pause();
			}

		}
		LastShowView = view;
	}
@Override
public void scrolle() {
	
}
//进行进度条除法运算
public static double div(double d1, double d2, int len) {
		BigDecimal b1 = new BigDecimal(d1);
		BigDecimal b2 = new BigDecimal(d2);
		return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
	} 
//返回按键的处理
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	if (keyCode==KeyEvent.KEYCODE_BACK) {
		releaseMediaplayer();
	}
	return super.onKeyDown(keyCode, event);
}
     // 播放完成监听事件
@Override
public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		textureView.getMediaPlayer().setOnCompletionListener(
				new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						videoPlay.setVisibility(View.VISIBLE);
						video.setVisibility(View.VISIBLE); 
					}
				});
	}
	
	private void addDate() {
		//刷新按钮，添加数据
	

				
		Weibo a=new Weibo();
		a.setVideoSourceId("http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4 ");
		a.setPicUrl("http://i0.kdsxiaohua.fd.pchome.net/g1/M00/0A/1F/oYYBAFQAG1eINzNtAAB4JrD1Js8AAB4uQHvXq0AAHg-359.jpg");
		weibos.add(a);
		Weibo b=new Weibo();
		b.setVideoSourceId("http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4 ");
		b.setPicUrl("http://i0.kdsxiaohua.fd.pchome.net/g1/M00/0A/1F/oYYBAFQAG1eINzNtAAB4JrD1Js8AAB4uQHvXq0AAHg-359.jpg");
		weibos.add(b);
		Weibo c=new Weibo();
		c.setVideoSourceId("http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4 ");
		c.setPicUrl("http://i0.kdsxiaohua.fd.pchome.net/g1/M00/0A/1F/oYYBAFQAG1eINzNtAAB4JrD1Js8AAB4uQHvXq0AAHg-359.jpg");
		weibos.add(c);
		Weibo d=new Weibo();
		d.setVideoSourceId("http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4 ");
		d.setPicUrl("http://i0.kdsxiaohua.fd.pchome.net/g1/M00/0A/1F/oYYBAFQAG1eINzNtAAB4JrD1Js8AAB4uQHvXq0AAHg-359.jpg");
		weibos.add(d);
		Weibo f=new Weibo();
		f.setVideoSourceId("http://www.ydtsystem.com/CardImage/21/video/20140305/20140305124807_37734.mp4 ");
		f.setPicUrl("http://i0.kdsxiaohua.fd.pchome.net/g1/M00/0A/1F/oYYBAFQAG1eINzNtAAB4JrD1Js8AAB4uQHvXq0AAHg-359.jpg");
		weibos.add(f);
	}
	
	private void addNewData() {
		//初始化添加假数据
		Weibo weibo11=new Weibo();
		weibo11.setVideoSourceId(url1);
		weibo11.setPicUrl("http://c.hiphotos.baidu.com/image/pic/item/aec379310a55b31951a0887141a98226cffc175c.jpg");
		weibos.add(weibo11);
		
		Weibo weibo12=new Weibo();
		weibo12.setVideoSourceId(url2);
		weibo12.setPicUrl("http://d.hiphotos.baidu.com/image/w%3D1280%3Bcrop%3D0%2C0%2C1280%2C720/sign=7599428948ed2e73fce9822ebf319ae8/38dbb6fd5266d0164ac73325952bd40734fa35e4.jpg");
		weibos.add(weibo12);
		
		Weibo weibo13=new Weibo();
		weibo13.setVideoSourceId(url3);
		weibo13.setPicUrl("http://img1.imgtn.bdimg.com/it/u=4029030709,3339531598&fm=23&gp=0.jpg");
		weibos.add(weibo13);
		
		Weibo weibo14=new Weibo();
		weibo14.setVideoSourceId(url4);
		weibo14.setPicUrl("http://wenwen.soso.com/p/20101011/20101011185121-485848880.jpg");
		weibos.add(weibo14);
		
		Weibo weibo15=new Weibo();
		weibo15.setVideoSourceId(url5);
		weibo15.setPicUrl("http://www.putaojiayuan.com/uploadfile/2012/0914/20120914104132619.jpg");
		weibos.add(weibo15);
		
		Weibo weibo16=new Weibo();
		weibo16.setVideoSourceId(url6);
		weibo16.setPicUrl("http://www.pic.sayingfly.com/Photo/OriginalImages/ShenghuoBaike/shishangshenghuo/hunshasheying_051117/480.jpg");
		weibos.add(weibo16);
		Weibo a=new Weibo();
		a.setVideoSourceId(url7);
		a.setPicUrl("http://img1.niutuku.com/HD/1112/2501/250134422.jpg");
		weibos.add(a);
		
	}
}

	