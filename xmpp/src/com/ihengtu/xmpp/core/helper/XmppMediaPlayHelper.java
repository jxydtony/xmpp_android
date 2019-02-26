package com.ihengtu.xmpp.core.helper;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * play the sound and voice
 * 
 * @author Administrator
 * 
 */
public class XmppMediaPlayHelper{
	private MediaPlayer mPlayer;
	private MediaPlayerListener mediaplayListener;
	private String playfile;
	private Object currObject;
	private Object lastObject;
	// ͬ��������
//	private String playLock = "lock";
	
	private boolean isPapusePlaying=false;
	
	private void startPlaying() {
		//如果是暂停播放就继续播放
		if(isPapusePlaying&&mPlayer!=null){
			if (mediaplayListener != null) {
				lastObject = currObject;
				mediaplayListener.onMediaPlayStart(currObject);
			}
			isPapusePlaying=false;
			mPlayer.start();
			return ;
		}
		mPlayer = new MediaPlayer();
		mPlayer.setOnCompletionListener(onCompletionListener);
		try {
			// ����Ҫ���ŵ��ļ�
			mPlayer.setDataSource(playfile);
			mPlayer.prepare();
			// ׼����֮��ʼ���ſ�ʼ
			if (mediaplayListener != null) {
				lastObject = currObject;
				mediaplayListener.onMediaPlayStart(currObject);
			}
			mPlayer.start();
		} catch (Exception e) {
			e.printStackTrace();
			if (mediaplayListener != null) {
				lastObject = currObject;
				mediaplayListener.onMediaPlayError(currObject);
			}
		}
	}

	/**
	 * ��ͣ���ͷ���Դ
	 */
	private void stopPlaying() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
		}
		if (mediaplayListener != null) {
//			mediaplayListener.onMediaPlayEnd(currObject);
			mediaplayListener.onMediaPlayStop(currObject);
		}
	}

	/**
	 * ��ʼ��������
	 */
	public void start(String playfile) {
		this.playfile = playfile;
		startPlaying();
	}

	public void papuse(){
		if(mPlayer!=null&&mPlayer.isPlaying()){
			mPlayer.pause();
			isPapusePlaying=true;
			if (mediaplayListener != null) {
//				mediaplayListener.onMediaPlayEnd(currObject);
				mediaplayListener.onMediaPlayStop(currObject);
			}
		}
	}
	
	/**
	 * ֹͣ��������
	 */
	public void stop() {
		stopPlaying();
	}
	
	public boolean isPlaying(){
		if(mPlayer!=null){
			return mPlayer.isPlaying();
		}
		return false;
	}

	public void addMediaPlayerListener(MediaPlayerListener listener, Object object) {
		this.mediaplayListener = listener;
		currObject = object;
	}

	OnCompletionListener onCompletionListener=new OnCompletionListener() {
		
		@Override
		public void onCompletion(MediaPlayer arg0) {
			// TODO Auto-generated method stub
			if (mediaplayListener != null) {
				mediaplayListener.onMediaPlayEnd(currObject);
				lastObject=null;
			}
		}
	};

	/**
	 * ����mediaplayer������Ƶ��ʼ�ͽ���
	 * 
	 * @author hpc
	 * 
	 */
	public interface MediaPlayerListener {

		/**
		 * 播放开始
		 */
		void onMediaPlayStart(Object object);

		/**
		 * 播放错误
		 */
		void onMediaPlayError(Object object);

		/**
		 * 异常播放停止
		 */
		void onMediaPlayStop(Object object);
		
		/**
		 * 播放结束
		 */
		void onMediaPlayEnd(Object object);
		
	}
}
