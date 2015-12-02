package com.example.invinjun.listtexureviewdemo.cacheplayer.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.invinjun.listtexureviewdemo.configs.Configs;
import com.example.invinjun.listtexureviewdemo.utils.NetWorkUtil;


public class NetBroadCastReciver extends BroadcastReceiver {

	private static final String TAG = "NetBroadCastReciver";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Configs.TIMES == 3) {
			Configs.TIMES = 1;
		}
		if (NetWorkUtil.isAvailable(context)) {
			if (Configs.TIMES == 1) {
				if (NetWorkUtil.isWifiState(context)) {
					Log.d(TAG, "net work type : wifi");
				} else if (NetWorkUtil.isGPRSState(context)) {
					Toast.makeText(context, "您正在使用2G/3G网络", Toast.LENGTH_LONG).show();
				}
			}
		} else {
			if (Configs.TIMES == 1) {
				Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
			}
		}
		Configs.TIMES++;
	}

}
