package com.example.invinjun.listtexureviewdemo.dbutil;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.invinjun.listtexureviewdemo.db.dao.Dao;


public class Downloader {
    private String urlstr;// 下载的地址
    private String localfile;// 临时文件保存路径
    private String LastFilepth;// 保存路径
	private int threadcount=1;// 线程数
    private Handler mHandler;// 消息处理器
    private Dao dao;// 工具类
    private int fileSizeOne;// 所要下载的文件的大小
    private DownloadInfo info;// 单线程存放下载信息类的集合
    private static final int INIT = 1;//定义三种下载的状态：初始化状态，正在下载状态，暂停状态
    private static final int DOWNLOADING = 2;
    private static final int PAUSE = 3;
    private static final int END = 4;
    private int state = INIT;
    final String LOG_TAG = "HttpEngine";
    final String TAG = "Downloader";
    private static final int DOWNLOAD_OLD =4;
    public static Downloader instance;
    private boolean isFirstdown;
    public  String newLocalPath;  
    public Downloader(String urlstr, String localfile, String newLocalPath,int threadcount,
            Context context, Handler mHandler) {
    	//localfile /storage/emulated/0/1234561440473176524.mp4
    	//url http://qiubai-video.qiushibaike.com/GM2OCN1JY53R30L7.mp4
    	//newLocalPath newLocalPath
    	// threadcount 1
    	//
    	this.newLocalPath=newLocalPath;
        this.urlstr = urlstr;
        this.localfile = localfile;
        this.threadcount = threadcount;
        this.mHandler = mHandler;
//        LastFilepth= newLocalPath
        String path=localfile.substring(localfile.lastIndexOf("/")+1,localfile.length());
        LastFilepth=newLocalPath+path;
        dao = new Dao(context);
    }
  

    /**
     * 得到downloader里的信息
     * 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
     * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
     */
    public void getDownloaderInfors() {
		Log.v(LOG_TAG, "000:" + localfile);
		if (isFirst(urlstr)) {// 数据库中是否存在
			isFirstdown = true;
			if (isExist(LastFilepth)) {
				state = END;
				// 直接去播放
				Message message = Message.obtain();
				message.what = 3;
				message.obj =  urlstr+"`"+LastFilepth;
				mHandler.sendMessage(message);
				LoadInfo loadInfo = new LoadInfo(0, 0, "");
				state = END;
				
//				String path=localfile.substring(localfile.lastIndexOf("/")+1,localfile.length());
//            	DownloadUtil.moveFile(localfile,newLocalPath+path);
//            	
//            	Message message2 = Message.obtain();
//                message2.what = 2;
//                message2.obj = urlstr+"`"+newLocalPath+path;
//                mHandler.sendMessage(message2);
				return;
			} else {
				// 没有文件去下载
				Log.v(LOG_TAG, "没有文件去下载:" + localfile);
				new Thread(runnable).start();// 初始化
				Log.v("TAG", "isFirst");
				info = new DownloadInfo(threadcount - 1, 0, 0 - 1, 0, fileSizeOne, urlstr);
				Log.e(TAG, "fileSize:" + 0 + "|urlstr:" + urlstr);
				dao.saveInfo(info);
				download();
				return;
			}
		} else {
			isFirstdown = false;
			// 得到数据库中已有的urlstr的下载器的具体信息
			info = dao.getInfo(urlstr);
			Log.v(LOG_TAG, "not isFirst size=" + info.getTotalSize());
			int size = 0;
			int compeleteSize = 0;
			size = info.getEndPos() - info.getStartPos() + 1;
			compeleteSize = info.getCompeleteSize();
			int fileTotalSize = info.getTotalSize();
			Log.v(LOG_TAG, "数据库中已有:" + urlstr);
			download();
			return;
		}

	}
    Runnable runnable = new Runnable(){

		@Override
		public void run() {
			init();
		}
    	
    };
    /**
     * 初始化
     */
	private void init() {
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(10000); 
			connection.setRequestMethod("GET");
			fileSizeOne = connection.getContentLength();
			Log.v(LOG_TAG, "fileSize" + fileSizeOne);

			int respCode = connection.getResponseCode();
			if (respCode == 200) {
				File file = new File(localfile);
				if (!file.exists()) {
					file.createNewFile();
				}
				// 本地访问文件
				RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
				accessFile.setLength(fileSizeOne);
				accessFile.close();
				connection.disconnect();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	};
  

    /**
     * 判断是否是第一次 下载
     */
    private boolean isFirst(String urlstr) {
        return dao.isHasInfors(urlstr);
    }

    /**
     * 利用线程开始下载数据
     */
    public void download() {
        if (info != null) {
            if (state == DOWNLOADING||state ==END)
                return;
            state = DOWNLOADING;
            new MyThread(info.getThreadId(), info.getStartPos(),
                    info.getEndPos(), info.getCompeleteSize(),info.getTotalSize(),
                    info.getUrl()).start();
        }
    }

    public class MyThread extends Thread {
        private int threadId;
        private int startPos;
        private int endPos;
        private int compeleteSize;
        private int totalSize;
        private String urlstr;
 
        public MyThread(int threadId, int startPos, int endPos,
                int compeleteSize,int totalSize, String urlstr) {
            this.threadId = threadId;
            this.startPos = startPos;
            this.endPos = endPos;
            this.compeleteSize = compeleteSize;
            this.totalSize=totalSize;
            this.urlstr = urlstr;
            File dirFirstFile=new File(newLocalPath);//新建一级主目录  
            if(!dirFirstFile.exists()){//判断文件夹目录是否存在  
                dirFirstFile.mkdir();//如果不存在则创建  
            }  
        }
        @Override
        public void run() {
            HttpURLConnection connection = null;
            RandomAccessFile randomAccessFile = null;
            InputStream is = null;
            try {
            	 Message message0 = Message.obtain();
                 message0.what = 0;
                 mHandler.sendMessage(message0);
                URL url = new URL(urlstr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setRequestProperty("Accept-Encoding", "identity"); 
                connection.setRequestProperty("Range", "bytes="+(startPos + compeleteSize) + "-");
                Log.i(TAG, "下载起始位置"+startPos + compeleteSize);
                int status = connection.getResponseCode();
                Log.i(TAG, "状态"+status);
                if (status==416) {
                	//文件下载异常之后数据库存储大小等于文件大小但无法播放，重新请求下载，下载位置超出范围
                	//删除数据库、原文件重新下载
                	dao.delete(urlstr);
					deleFile(localfile);
					isFirstdown=true;
					state = END;
					getDownloaderInfors();
					return;
				}else if (status==404) {
					//服务器删除原文件或者请求地址错误，服务器找不到该文件
					 Message message4 = Message.obtain();
		                message4.what = 4;
		                mHandler.sendMessage(message4);
				}else if(status==206){
				randomAccessFile = new RandomAccessFile(localfile, "rwd");
	            randomAccessFile.seek(startPos + compeleteSize);	
               //服务器正常返回数据
//               int filetotalsize = connection.getContentLength();
                is = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int length = -1;
              
                Log.i(TAG, "下载total" + totalSize);
                if (isFirstdown) {//第一次下载 请求文件长度 保存至数据库中，否则直接读取
                	
                	totalSize = connection.getContentLength();
                	Log.e(TAG, "url:"+urlstr+" 存入数据库大小："+totalSize);
                    dao.updataInfos(threadId, compeleteSize,urlstr,totalSize);	
				}
                if (totalSize==0) {//数据库得到文件大小为零，重新获取，并跟新数据库
            		totalSize = connection.getContentLength();
            		 dao.updataInfos(threadId, compeleteSize,urlstr,totalSize);	
				}
                while ((length = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, length);
                    compeleteSize+=length;
                    dao.updataInfo(threadId, compeleteSize, urlstr);
                    if (state == PAUSE) {
                      	Log.i(TAG, "暂停state:"+state);
                          return;
                      }
                    // 更新数据库中的下载信息
                    // 用消息将下载信息传给进度条，对进度条进行更新
                    Message message1 = Message.obtain();
                    message1.what = 1;
                    message1.obj = urlstr;
                    message1.arg1 = compeleteSize;
                    message1.arg2=totalSize;
                    mHandler.sendMessage(message1);
                    Log.e(LOG_TAG, "compeleteSize:"+compeleteSize+"ffilesize:"+totalSize+"--=="+urlstr);
                }
                if ((length = is.read(buffer)) == -1) {
                	String path=localfile.substring(localfile.lastIndexOf("/")+1,localfile.length());
                	DownloadUtil.moveFile(localfile,newLocalPath+path);
                	
                	Message message2 = Message.obtain();
                    message2.what = 2;
                    message2.obj = urlstr+"`"+newLocalPath+path;
                    mHandler.sendMessage(message2);
                    return;
				}
                
				}
                
            } catch (Exception e) {
                e.printStackTrace();
                dao.delete(urlstr);
				deleFile(localfile);
                Message message5=new Message();
                message5.what=5;
                message5.obj=e.getMessage();
                mHandler.sendMessage(message5);
            } finally {
                try {
                    is.close();
                    randomAccessFile.close();
                    connection.disconnect();
                    dao.closeDb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
    //删除数据库中urlstr对应的下载器信息
    public void delete(String urlstr) {
        dao.delete(urlstr);
        Log.i(TAG, "执行删除："+urlstr);
    }
    //设置暂停
    public void pause() {
        state = PAUSE;
        Log.i(TAG, "执行暂停："+urlstr);
        
    }
    //重置下载状态
    public void reset() {
        state = INIT;
    }
    public static boolean isExist(String name){
		boolean b = false;
		if(checkSDCard()){
			File file = new File(name);
			if(!file.exists())
			{
				b = false;
				Log.e("file", "判断文件不存在   ");
			}else if(file.exists()){
				b=true;
				Log.e("file", "判断文件存在   ");
			}
		}
		return b;
		
	}
	public static boolean checkSDCard()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return true;
		}else{
			return false;
		}
	}
	public static boolean deleFile(String fileName){
		File file =new File(fileName);
		if (file.exists()&&file.isFile()) {
			if (file.delete()) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	  public String getLastFilepth() {
			return newLocalPath;
		}
		public void setLastFilepth(String lastFilepth) {
			newLocalPath = lastFilepth;
		}
		
		
		/**
	     *判断是否正在下载 
	     */
	    public boolean isdownloading() {
	        return state == DOWNLOADING;
	    }
	    /**
	     *判断是否正在下载 
	     */
	    public boolean isEnd() {
	    	return state == END;
	    }
}
