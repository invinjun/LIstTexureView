package com.example.invinjun.listtexureviewdemo.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class IBuild
{
  public static final String IMEI = Build.DEVICE;

  public static final String RELEASE = Build.VERSION.RELEASE;

  public static final String MANUFACTURER = Build.MANUFACTURER;

  public static final String MODEL = Build.MODEL;

  public static int FREQUENCY = 0;
  public static String getIMEI(Context context){
	  TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	  String imei=telephonyManager.getDeviceId();
	  return imei;
  }
  public static String Number(Context context) {
		TelephonyManager tmNum = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int status = tmNum.getSimState();
		if(status!=TelephonyManager.SIM_STATE_ABSENT){
			return tmNum.getLine1Number();
		}
		return null;
	}
}