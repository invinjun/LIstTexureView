package com.example.invinjun.listtexureviewdemo.utils;

import android.os.AsyncTask;

/**
 *  异步请求 HttpRequestTask
 * @author Administrator
 *
 */
public class HttpRequestTask extends AsyncTask<String, Integer, Object> {
	private static final String TAG = HttpRequestTask.class.getSimpleName();
	HttpTaskCallBack mListener;
	/**
	 * task is finished
	 */
	public boolean isFinished = false;

	public HttpRequestTask(HttpTaskCallBack listener) {
		this.mListener = listener;
	}

	@Override
	protected Object doInBackground(String... params) {
		return mListener.onTaskRunning(params);
	}

	@Override
	protected void onPostExecute(Object result) {
	
		isFinished = true;
		
		mListener.onTaskFinished(result);
		super.onPostExecute(result);
	}

	@Override
	protected void onCancelled() {
		this.cancel(true);
		mListener.onCancelled();
		super.onCancelled();
	}

	@Override
	protected void onPreExecute() {
		mListener.onTaskStart();
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	/**
	 * 
	 * @author wenqiurong
	 * 
	 */
	public interface HttpTaskCallBack {

		/**
		 * on Task Start
		 */
		public void onTaskStart();

		/**
		 * on Task Running
		 */
		public Object onTaskRunning(String... params);

		/**
		 * 注意网络异常、http 请求异常信息 on Task Finished
		 * 
		 * @param result
		 *            will be null
		 */
		public void onTaskFinished(Object result);

		/**
		 * onCancelled
		 */
		public void onCancelled();

	}
}
