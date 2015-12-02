package com.example.invinjun.listtexureviewdemo.view;



import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;

public class VideoPlayTextureView extends TextureView implements
		TextureView.SurfaceTextureListener, OnPreparedListener,
		OnCompletionListener {

	private MediaPlayer mediaPlayer;
	private Surface surface;
	private MediaStateLitenser mediaStateLitenser;
	private MediaState currentMediaState = MediaState.RESET;
	private boolean isChange = true;//
	private String VideoPath;
	private int size;
	public void setChange(boolean change){
		isChange = change;
	}
	
	public boolean isChange(){
		return isChange;
	}
	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}


	public VideoPlayTextureView(Context context) {
		super(context);
		init(context);
	}

	public VideoPlayTextureView(Context context, AttributeSet paramAttributeSet) {
		super(context, paramAttributeSet);
		init(context);
	}

	public VideoPlayTextureView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		init(paramContext);
	}

	/**
	 * ��ʼ�����
	 * @param context
	 */	
	private void init(Context context) {
		mediaPlayer = new MediaPlayer();
		this.setMediaPlayer(mediaPlayer);
		setSurfaceTextureListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnPreparedListener(this);
	}

	@Override
	protected void onMeasure(int paramInt1, int paramInt2) {
		int i = MeasureSpec.getSize(paramInt1);
		setMeasuredDimension(i, i);
	}

	/**
	 * 
	 */
	public void play() {
		if(currentMediaState ==  MediaState.PLAY){
			//currentMediaState = MediaState.PAUSE;
			if (mediaPlayer != null)
//				mediaPlayer.pause();
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPauseListener();
		}else{
			currentMediaState = MediaState.PLAY;
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPlayListener();
			if (mediaPlayer != null && !mediaPlayer.isPlaying()){
			mediaPlayer.start();
				
		}}
	}
	public void playOld() {
		if(currentMediaState ==  MediaState.PLAY){
			//currentMediaState = MediaState.PAUSE;
			if (mediaPlayer != null)
//				mediaPlayer.pause();
				if(mediaStateLitenser != null)
					mediaStateLitenser.OnPauseListener();
		}else{
			currentMediaState = MediaState.PLAY;
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPlayListener();
			if (mediaPlayer != null && !mediaPlayer.isPlaying()){
				mediaPlayer.start();
				
			}}
	}
	  public boolean isInPlaybackState()
	  {
	    return (this.mediaPlayer != null) && (this.currentMediaState != MediaState.PAUSE) && (this.currentMediaState != MediaState.PREPARE) && (this.currentMediaState != MediaState.RESET);
	  }


	  public void stopPlayback1() throws IllegalStateException, IOException
	  {
	  if (mediaPlayer != null) {
		  mediaPlayer.stop();
		  mediaPlayer.release();
		  mediaPlayer=null;
      }
	  }
	  public void stopPlayback()
	  {
	    if (this.mediaPlayer != null)
	    {
	      if ((getContext() instanceof Activity))
	        ((Activity)getContext()).runOnUiThread(new Runnable()
	        {
	          @Override
			public void run()
	          {
	        	  VideoPlayTextureView.this.setKeepScreenOn(false);
	          }
	        });
	      release(true);
	    }
	  }
	  public void release(boolean paramBoolean)
	  {
	    this.currentMediaState = MediaState.PLAY;
	    if ((this.mediaPlayer == null) || (isInPlaybackState()));
	    try
	    {
	      this.mediaPlayer.reset();
	      if (this.mediaPlayer != null)
	        this.mediaPlayer.release();
	      this.mediaPlayer = null;
	      return;
	    }
	    catch (IllegalStateException localIllegalStateException)
	    {
	      while (true);
	    }
	  }

	public void play1() {
		
		 if (mediaPlayer != null && currentMediaState ==  MediaState.PREPARE) {
			 mediaPlayer.start();
			 currentMediaState =  MediaState.RESET;
     } else {
    	 currentMediaState =  MediaState.PREPARE;
     }
		}
	

	/**
	 * ��ͣ����
	 */
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()){
			currentMediaState = MediaState.PAUSE;
			mediaPlayer.pause();
			if(mediaStateLitenser != null)
				mediaStateLitenser.OnPauseListener();
		}
	}

	/**
	 * ֹͣ���ţ���ʱûʹ��
	 */
	public void stop() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			currentMediaState = MediaState.RESET;
			mediaPlayer.stop();
			mediaPlayer.reset();
		}
	}
	
	/**
	 * ���õ�ǰmediaPlyer����listView���øÿؼ�ʱ����
	 */
	public void reset(){
		currentMediaState = MediaState.RESET;
		mediaPlayer.reset();
	}

	/**
	 * @param path
	 */
	public void prepare(String path) {
		try {
			if (!mediaPlayer.isPlaying()) {
				mediaPlayer.reset();
			}
			currentMediaState = MediaState.PREPARE; 
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(path);
			mediaPlayer.setSurface(surface);
			mediaPlayer.prepare();
		} catch (Exception e) {
			Log.e("videoviewError", "e="+e);
		}
	}
	
	public MediaState getMediaState(){
		return currentMediaState;
	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture arg0, int arg1,
			int arg2) {
		surface = new Surface(arg0);
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
		return false;
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int arg1,
			int arg2) {

	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture arg0) {

	}

	/**
	 * ���ò���״̬�ļ���
	 * @param mediaStateLitenser
	 */
	public void setMediaStateLitenser(MediaStateLitenser mediaStateLitenser) {
		this.mediaStateLitenser = mediaStateLitenser;
	}

	/**
	 * ����״̬
	 * @author QD
	 *
	 */
	public enum MediaState {
		RESET(0x5),PREPARE(0x1), COMPLETE(0x2), PLAY(0x3), PAUSE(0x4);
		static MediaState mapIntToValue(final int stateInt) {
			for (MediaState value : MediaState.values()) {
				if (stateInt == value.getIntValue()) {
					return value;
				}
			}
			return RESET;
		}

		private int mIntValue;

		MediaState(int intValue) {
			mIntValue = intValue;
		}

		int getIntValue() {
			return mIntValue;
		}
	}
	
	/**
	 * ��ʼ��Ƶ�ļ������ӵ���
	 */
	public void OnDownLoadingListener(){
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnDownLoadingListener();
	}

	public interface MediaStateLitenser {
		public void OnCompletionListener();

		public void OnPrepareListener();

		public void OnPauseListener();

		public void OnPlayListener();
		
		public void OnDownLoadingListener();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnCompletionListener();
		currentMediaState = MediaState.COMPLETE;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mediaStateLitenser != null)
			mediaStateLitenser.OnPrepareListener();
	}

	public String getVideoPath() {
		return VideoPath;
	}

	public void setVideoPath(String videoPath) {
		VideoPath = videoPath;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
