package com.example.invinjun.listtexureviewdemo.cacheplayer.service;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.invinjun.listtexureviewdemo.utils.FileUtil;
import com.example.invinjun.listtexureviewdemo.utils.IBuild;
import com.example.invinjun.listtexureviewdemo.utils.NetWorkUtil;


public class WebServiceHelper {

	private static WebServiceHelper HELPER;
	private SharedPreferences preferences;

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
	 */
	public void getResultString(final Context context) {
		preferences = context.getSharedPreferences(ServiceParams.NAME_SP,
					Context.MODE_PRIVATE);
		final String playTime = preferences.getString(ServiceParams.PLAY_TIME, "0");
			if (NetWorkUtil.isAvailable(context)) {

//				new Thread() {
//					@Override
//					public void run() {
//
//						try {
//							SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//									SoapEnvelope.VER11);
//
//							SoapObject soapObject = new SoapObject(
//									ServiceParams.NAME_SPACE,
//									ServiceParams.METHOD_NAME);
//
//							soapObject.addProperty(ServiceParams.IMEI,
//									IBuild.getIMEI(context));
//							soapObject.addProperty(ServiceParams.MANUFACTURER,
//									IBuild.MANUFACTURER);
//							soapObject.addProperty(ServiceParams.MODEL,
//									IBuild.MODEL);
//							soapObject.addProperty(ServiceParams.RELEASE,
//									IBuild.RELEASE);
//							soapObject.addProperty(ServiceParams.PLAY_TIME,
//									playTime);
//							soapObject.addProperty(ServiceParams.REPORT_FROM,
//									"校园微视");
//							soapObject.addProperty(ServiceParams.PHONE_NUMBER,
//									IBuild.Number(context));
//
//							envelope.bodyOut = soapObject;
//							envelope.dotNet = true;
//							envelope.setOutputSoapObject(soapObject);
//
//							(new MarshalBase64()).register(envelope);
//
//							HttpTransportSE httpTranstation = new HttpTransportSE(
//									ServiceParams.WSDL_URL);
//							httpTranstation.call(ServiceParams.NAME_SPACE
//									+ ServiceParams.METHOD_NAME, envelope);
//
//							if (envelope.getResponse() != null) {
//								String resault = FileUtil.subString(envelope.bodyIn
//										.toString());
//								JSONObject object = new JSONObject(resault);
//
//								if (object.getInt("code") == 1) {
//								}
//							}
//						} catch (Exception e) {
//							//System.out.println(e.getMessage());
//						}
//					}
//				}.start();
			}
		}
	}

