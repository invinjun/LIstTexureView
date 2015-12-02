package com.example.invinjun.listtexureviewdemo.utils;


import android.content.Context;
import android.os.Handler;
import android.os.Message;


public class WebServiceHelper {

	private static final String TAG = "WebServiceHelper";

	private static WebServiceHelper HELPER;

	private WebServiceCallBack callBack;

	public static WebServiceHelper newInstance() {
		if (HELPER == null) {
			HELPER = new WebServiceHelper();
		}
		return HELPER;
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param nameSpace
	 *            命名空间
	 * @param wsdlUrl
	 *            wsdl地址
	 * @param methodName
	 *            方法名
	 * @param
	 *            请求实体
	 * @param callBack
	 *            回调
	 */
	public void getResultString(Context context, final String nameSpace,
			final String wsdlUrl, final String methodName,
			 WebServiceCallBack callBack) {

		this.callBack = callBack;

		callBack.onStart();
		if (NetWorkUtil.isAvailable(context)) {

//			new Thread() {
//				@Override
//				public void run() {
//
//					try {
//						SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//								SoapEnvelope.VER11);
//						envelope.bodyOut = request;
//						envelope.dotNet = true;
//						envelope.setOutputSoapObject(request);
//
//						(new MarshalBase64()).register(envelope);
//
//						HttpTransportSE httpTranstation = new HttpTransportSE(
//								wsdlUrl);
//						httpTranstation.call(nameSpace + methodName, envelope);
//
//						if (envelope.getResponse() != null) {
////							LogUtils.d(TAG,
////									"result：\n" + envelope.bodyIn.toString());
//							Message message = mHandler.obtainMessage();
//							message.what = Const.SUCCESS;
//							message.obj = FileUtil.subString(envelope.bodyIn
//									.toString());
//							mHandler.sendMessage(message);
//						} else {
////							LogUtils.w(TAG, "disconnected net work");
//							mHandler.sendEmptyMessage(Const.EMPTY);
//						}
//					} catch (Exception e) {
//						Message message = mHandler.obtainMessage();
//						message.what = Const.EXCEPTION;
//						message.obj = e;
//						mHandler.sendMessage(message);
////						LogUtils.e("webservice",
////								"error msg__>>" + e.getMessage());
//					}
//				}
//			}.start();

		} else {
//			LogUtils.e("webservice", "disconnected");
			mHandler.sendEmptyMessage(Const.DISNETWORK);
		}

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Const.SUCCESS:
				callBack.onSuccess((String) msg.obj);
				break;
			case Const.EMPTY:
				callBack.onEmptyResponse();
				break;
			case Const.EXCEPTION:
				callBack.onException((Exception) msg.obj);
				break;
			case Const.DISNETWORK:
				callBack.onDisconnectedNetWork();
				break;
			}
		}
	};

	// public void get() {
	//
	// new Thread() {
	// @Override
	// public void run() {
	// try {
	// System.out.println("rpc------");
	// SoapObject request = new SoapObject(
	// WebServiceHelper.NAME_SPACE, "GetMailList");
	// request.addProperty(ParamsKeys.INBOX_LIST_MAILTYPE, 1);
	// request.addProperty(ParamsKeys.INBOX_LIST_USERNAME, "admin");
	// request.addProperty(ParamsKeys.INBOX_LIST_PAGENO, 1);
	//
	// @SuppressWarnings("deprecation")
	// AndroidHttpTransport ht = new AndroidHttpTransport(
	// WebServiceHelper.WSDL_URL);
	// ht.debug = true;
	//
	// SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	// SoapEnvelope.VER11);
	//
	// envelope.bodyOut = request;
	// envelope.dotNet = true;
	// envelope.setOutputSoapObject(request);
	//
	// ht.call(WebServiceHelper.NAME_SPACE + "GetMailList",
	// envelope);
	//
	// System.out.println("_______________>>>>>>>>>>>>>"
	// + envelope.bodyIn.toString());
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.println(e.getMessage());
	// }
	// }
	// }.start();
	// }

}