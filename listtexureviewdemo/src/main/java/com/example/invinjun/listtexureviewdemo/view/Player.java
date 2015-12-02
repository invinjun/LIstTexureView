package com.example.invinjun.listtexureviewdemo.view;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invinjun.listtexureviewdemo.R;
import com.example.invinjun.listtexureviewdemo.configs.Configs;
import com.example.invinjun.listtexureviewdemo.utils.NetWorkUtil;


@SuppressLint("HandlerLeak")
public class Player implements OnBufferingUpdateListener, OnCompletionListener,
		MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {

	private static final String TAG = "Player";

	private int screenWidth;
	private int screenHeight;

	private Button stateBtn;
	private TextView curTime;
	private TextView totalTime;
	private SeekBar skbProgress;
	private Dialog creatingProgress;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;

	private String url;
	private int playType;
	private Activity context;

	private boolean isFirst = true;

	public MediaPlayer mediaPlayer;

	private Timer mTimer = new Timer();

	private PlayerCallBack callBack;

	/**
	 * 视频控制器
	 * 
	 * @author Gogh
	 * 
	 * @param context
	 *            上下文对象
	 * @param surfaceView
	 *            视频容器
	 * @param skbProgress
	 *            播放进度
	 * @param stateBtn
	 *            控制按钮
	 * @param url
	 *            视频地址
	 * @param playType
	 *
	 * @param totalTime
	 *            视频总时长
	 * @param curTime
	 *            当前播放的时间
	 */
	public Player(Activity context, SurfaceView surfaceView,
			SeekBar skbProgress, Button stateBtn, String url, int playType,
			TextView totalTime, TextView curTime, PlayerCallBack callBack) {
		this.url = url;
		this.context = context;
		this.playType = playType;
		this.stateBtn = stateBtn;
		this.surfaceView = surfaceView;
		this.skbProgress = skbProgress;
		this.totalTime = totalTime;
		this.curTime = curTime;
		this.callBack = callBack;

		initWidget();
		screenSize();
	}

	/**
	 * 绑定控件
	 */
	@SuppressWarnings("deprecation")
	private void initWidget() {
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceView.setEnabled(false);
		mTimer.schedule(mTimerTask, 0, 1000);
	}

	/**
	 * 获取屏幕的尺寸
	 */
	private void screenSize() {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// double diagonalPixels = Math.sqrt(Math.pow(metrics.widthPixels, 2)
		// + Math.pow(metrics.heightPixels, 2));
		// double screenSize = diagonalPixels / metrics.densityDpi;
		// Log.e("screenSize", screenWidth + "__" + screenHeight);
		// Log.e("screenSize", "density__" + metrics.density);
		// Log.e("screenSize", "densityDpi__" + metrics.densityDpi);
		// Log.e("screenSize", "scaledDensity__" + metrics.scaledDensity);
		// Log.e("screenSize", "___" + screenSize);
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	/**
	 * 初始化缓冲进度条
	 */
	public void createLoadingDialog(int width, int height) {
		creatingProgress = new Dialog(context, R.style.Dialog_loading_noDim);
		ProgressBar bar = new ProgressBar(context);
		Window dialogWindow = creatingProgress.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.width = (int) (context.getResources().getDisplayMetrics().density
		// * 300);
		// lp.height = (int) (context.getResources().getDisplayMetrics().density
		// * 100);
		lp.width = width;
		lp.height = height;
		lp.gravity = Gravity.CENTER;
		dialogWindow.setAttributes(lp);
		creatingProgress.setCanceledOnTouchOutside(false);
		creatingProgress.setContentView(bar);
	}

	public void show() {
		creatingProgress.show();
	}

	public void dismiss() {
		creatingProgress.dismiss();
	}

	/**
	 * 通过定时器和Handler来更新进度条 {@link handleProgress}
	 */
	TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mediaPlayer == null)
				return;
			if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
				handleProgress.sendEmptyMessage(0);
			}
		}
	};
	/**
	 * 刷新进度
	 */
	Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {

			int position = mediaPlayer.getCurrentPosition();
			int duration = mediaPlayer.getDuration();

			if (duration > 0) {
				long pos = skbProgress.getMax() * position / duration;
				skbProgress.setProgress((int) pos);
			}
		};
	};

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setDataSource(url);
			new Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}, 1000);
		} catch (Exception e) {
			Log.e("mediaPlayer", "error", e);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
	}

	/**
	 * 通过onPrepared播放
	 */
	@Override
	public void onPrepared(MediaPlayer mPlayer) {
		callBack.onInitPlayerFinished();
		totalTime.setText(getTime(mediaPlayer.getDuration()));
		setFullScreen();
		startByNetStatus();
	}

	/**
	 * 全屏显示
	 */
	public void setFullScreen() {
		int width = mediaPlayer.getVideoWidth();
		int height = mediaPlayer.getVideoHeight();
		float scale = getScale(width, height);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				(int) resetWidth(scale, width),
				(int) resetHeight(scale, height));
		params.gravity = Gravity.CENTER;
		surfaceView.setLayoutParams(params);
	}

	/**
	 * 还原视频大小
	 */
	public void setOrignalSize() {
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
		params.gravity = Gravity.CENTER;
		surfaceView.setLayoutParams(params);
	}

	/**
	 * 计算缩放倍数
	 * 
	 * @param width
	 *            视频宽
	 * @param height
	 *            视频高
	 * @return
	 */
	private float getScale(int width, int height) {
		float scale = 1.0f;
		if (width >= height) {
			scale = (screenWidth * 1.0f) / (width * 1.0f);
		} else if (height > width) {
			scale = (screenHeight * 1.0f) / (height * 1.0f);
		}
		return scale;
	}

	/**
	 * 计算视频容器高度
	 * 
	 * @param scale
	 *            缩放倍数
	 * @param height
	 *            视频高度
	 * @return
	 */
	private float resetHeight(float scale, int height) {
		return height * scale;
	}

	/**
	 * 计算视频容器宽度
	 * 
	 * @param scale
	 *            缩放倍数
	 * @param width
	 *            视频宽度
	 * @return
	 */
	private float resetWidth(float scale, int width) {
		return width * scale;
	}

	/**
	 * 检测自动播放设置
	 */
	private void startByNetStatus() {
		Log.d(TAG, "start type:" + playType);
		if (NetWorkUtil.isAvailable(context)) {
			switch (playType) {
			case Configs.WIFI:
				mediaPlayer.start();
				break;
			case Configs.AUTO:
				mediaPlayer.start();
				break;
			case Configs.CLOSE:
				break;
			}
			stateBtn.setEnabled(true);
			surfaceView.setEnabled(true);
			skbProgress.setEnabled(true);
			isFirst = !isFirst;
		} else {
			Toast.makeText(context,
					context.getString(R.string.toast_connect_not_avalible),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 播放完成后的处理
	 */
	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (!isFirst) {
			callBack.onCompletion();
			skbProgress.setProgress(skbProgress.getMax());
			curTime.setText(totalTime.getText().toString());
			isFirst = !isFirst;
		}
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int bufferingProgress) {
		skbProgress.setSecondaryProgress(bufferingProgress);
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();

		if (currentProgress >= bufferingProgress) {
			// uIHandler.sendEmptyMessage(0);
			callBack.onLoading();
		} else {
			callBack.onLoadFinished();
			// uIHandler.removeCallbacksAndMessages(null);
			// if (creatingProgress != null) {
			// if (creatingProgress.isShowing()) {
			// creatingProgress.dismiss();
			// }
			// }
		}
		Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
	}

	/**
	 * 刷新缓冲进度条
	 */
	public void refreshDialog() {
		int currentProgress = skbProgress.getMax()
				* mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
		int bufferingProgress = skbProgress.getSecondaryProgress();
		Log.e("refreshDialog", currentProgress + "__" + bufferingProgress);
		if (currentProgress >= bufferingProgress) {
			// uIHandler.sendEmptyMessage(0);
			callBack.onLoading();
		} else {
			callBack.onLoadFinished();
			// uIHandler.removeCallbacksAndMessages(null);
			// if (creatingProgress != null) {
			// if (creatingProgress.isShowing()) {
			// creatingProgress.dismiss();
			// }
			// }
		}
	}

//	/**
//	 * 显示缓冲进度条
//	 */
//	private Handler uIHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			creatingProgress.show();
//		}
//	};

	/**
	 * 是否正在播放
	 * 
	 * @return
	 */
	public boolean isPlaying() {
		if (mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;
	}

	private String getTime(int duration) {
		int min = (duration / 1000) / 60;
		int sec = (duration / 1000) % 60;
		return getType(min) + ":" + getType(sec);
	}

	private String getType(int time) {
		if (time < 10) {
			return "0" + time;
		}
		return String.valueOf(time);
	}

	public interface PlayerCallBack {
		void onInitPlayerFinished();

		void onCompletion();

		void onLoading();

		void onLoadFinished();

	}

}
