package com.example.invinjun.listtexureviewdemo.configs;


import android.os.Environment;

public class CONSTANTS {

	// ******************************************params*********************
	public final static int VIDEO_FRAMERATE = 15;
	public final static int VIDEO_QUALITY = 12;
	public final static int AUDIO_CHANNEL = 1;
	public final static int AUDIO_BITRATE = 96000;// Build.VERSION.SDK_INT >= 10
													// ? 96000 : 12200;
	public final static int VIDEO_BITRATE = 1000000;
	public final static int DEFAULT_SAMPLERATE = 44100;
	public final static int AUDIO_SAMPLINGRATE = 1000000;

	/*
	 * ת����Ƶ��������
	 * 
	 * videoCodec 
	 * videoBitrate 720p�ֱ����Ƽ�Ϊ1000-1200��������Ƶ�Ƽ�Ϊ600-800
	 * width ��Ƶ����ֱ��ʣ�0Ϊ����
	 * height ��Ƶ����ֱ��ʣ�0Ϊ����
	 * fps ��Ƶ֡���ʣ�0Ϊ���䡣�Ƽ�Ϊ0��25��PAL��ʽ��
	 * audioBitrate ��Ƶλ���ʣ���KbpsΪ��λ�����Ƽ�Ϊ64-128
	 * sampleRate ��Ƶ�����ʣ�0Ϊ���䡣�Ƽ�Ϊ0��48000��DVD��
	 */
	public final static int OUT_VIDEOBITRATE = 400000;
	public final static int OUT_WIDTH = 0;
	public final static int OUT_HEIGHT = 0;
	public final static int OUT_FPS = 25;
	public final static int OUT_AUDIOBITRATE = 128;
	public final static int OUT_SAMPLERATE = 48000;

	// **********************************************************************
	public final static String METADATA_REQUEST_BUNDLE_TAG = "requestMetaData";
	public final static String FILE_START_NAME = "VMS_";
	public final static String VIDEO_EXTENSION = ".mp4";
	public final static String IMAGE_EXTENSION = ".jpg";
	public final static String DCIM_FOLDER = "/DCIM";
	public final static String CAMERA_FOLDER = "/video";
	public final static String TEMP_FOLDER = "/Temp";
	public final static String WATER_FOLDER = "/mark";
	public final static String SAMPLE_FOLDER = "/sample";
	public final static String SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath();
	public final static String CAMERA_FOLDER_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ CONSTANTS.DCIM_FOLDER
			+ CONSTANTS.CAMERA_FOLDER;
	public final static String TEMP_FOLDER_PATH = Environment
			.getExternalStorageDirectory().toString()
			+ CONSTANTS.DCIM_FOLDER
			+ CONSTANTS.CAMERA_FOLDER + CONSTANTS.TEMP_FOLDER;
	public final static String VIDEO_CONTENT_URI = "content://media/external/video/media";

	public final static String KEY_DELETE_FOLDER_FROM_SDCARD = "deleteFolderFromSDCard";

	public final static String RECEIVER_ACTION_SAVE_FRAME = "com.javacv.recorder.intent.action.SAVE_FRAME";
	public final static String RECEIVER_CATEGORY_SAVE_FRAME = "com.javacv.recorder";
	public final static String TAG_SAVE_FRAME = "saveFrame";
	public final static String ERROR="ERROR_CODE";
	public final static String ERRORNUM="_001";

	public final static int RESOLUTION_HIGH = 1300;
	public final static int RESOLUTION_MEDIUM = 500;
	public final static int RESOLUTION_LOW = 180;

	public final static int RESOLUTION_HIGH_VALUE = 2;
	public final static int RESOLUTION_MEDIUM_VALUE = 1;
	public final static int RESOLUTION_LOW_VALUE = 0;
}
