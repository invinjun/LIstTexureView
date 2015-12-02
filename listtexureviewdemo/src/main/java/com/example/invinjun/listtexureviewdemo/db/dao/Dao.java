package com.example.invinjun.listtexureviewdemo.db.dao;


	import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

	import com.example.invinjun.listtexureviewdemo.dbutil.DBHelper;
	import com.example.invinjun.listtexureviewdemo.dbutil.DownloadInfo;


/**
	 * 
	 * 一个业务类
	 */
	public class Dao {
	    private DBHelper dbHelper;

	    public Dao(Context context) {
	        dbHelper = new DBHelper(context);
	    }

	    /**
	     * 查看数据库中是否有数据
	     */
	    public boolean isHasInfors(String urlstr) {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select count(*)  from download_info where url=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	        cursor.moveToFirst();
	        int count = cursor.getInt(0);
	        cursor.close();
	        return count == 0;
	    }

	    /**
	     * 保存 下载的具体信息
	     */
	    public void saveInfos(List<DownloadInfo> infos) {
	        SQLiteDatabase database = dbHelper.getWritableDatabase();
	        for (DownloadInfo info : infos) {
	            String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,total_size,url) values (?,?,?,?,?,?)";
	            Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
	             info.getEndPos(), info.getCompeleteSize(), info.getTotalSize(),info.getUrl() };
	            database.execSQL(sql, bindArgs);
	        }
	    }
	    /**
	     * 保存 下载的具体信息
	     */
	    public void saveInfo(DownloadInfo info) {
	    	SQLiteDatabase database = dbHelper.getWritableDatabase();
	    		String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,total_size,url) values (?,?,?,?,?,?)";
	    		Object[] bindArgs = { info.getThreadId(), info.getStartPos(),
	    				info.getEndPos(), info.getCompeleteSize(),info.getTotalSize(), info.getUrl() };
	    		database.execSQL(sql, bindArgs);
	    	}

	    /**
	     * 得到下载具体信息
	     */
	    public List<DownloadInfo> getInfos(String urlstr) {
	        List<DownloadInfo> list = new ArrayList<DownloadInfo>();
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql ="select thread_id, start_pos, end_pos,compelete_size,total_size,url from download_info where url=?";

	        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	        while (cursor.moveToNext()) {
	            DownloadInfo info = new DownloadInfo(cursor.getInt(0),
	                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
	                    cursor.getInt(4),cursor.getString(4));
	            list.add(info);
	        }
	        cursor.close();
	        return list;
	    }
	    /**
	     * 得到下载具体信息
	     */
	    public DownloadInfo getInfo(String urlstr) {
	    	DownloadInfo info = null;
	    	SQLiteDatabase database = dbHelper.getReadableDatabase();
	    	String sql ="select thread_id, start_pos, end_pos,compelete_size,total_size,url from download_info where url=?";
	    	
	    	Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	    	while (cursor.moveToNext()) {
	    		        info = new DownloadInfo(cursor.getInt(0),
	    				cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),cursor.getInt(4),
	    				cursor.getString(5));
	    	}
	    	cursor.close();
	    	return info;
	    }

	    /**
	     * 更新数据库中的下载信息
	     */
	    public void updataInfos(int threadId, int compeleteSize, String urlstr,long totalsize) {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "update download_info set compelete_size=?,total_size=? where thread_id=? and url=?";
	        Object[] bindArgs = { compeleteSize,totalsize, threadId, urlstr };
	        database.execSQL(sql, bindArgs);
	    }
	    /**
	     * 更新数据库中的下载信息
	     */
	    public void updataInfo(int threadId, int compeleteSize, String urlstr) {
	    	SQLiteDatabase database = dbHelper.getReadableDatabase();
	    	String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
	    	Object[] bindArgs = { compeleteSize, threadId, urlstr };
	    	database.execSQL(sql, bindArgs);
	    }
	    /**
	     * 关闭数据库
	     */
	    public void closeDb() {
	        dbHelper.close();
	    }

	    /**
	     * 下载完成后删除数据库中的数据
	     */
	    public void delete(String url) {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        database.delete("download_info", "url=?", new String[] { url });
	        database.close();
	    }
	    /**
	     * 下载完成后删除数据库中的数据
	     */
	    public void deleteTable() {
	    	SQLiteDatabase database = dbHelper.getReadableDatabase();
	    	String sql="Drop Table download_info";
	    	database.execSQL(sql);
	    }
	}
