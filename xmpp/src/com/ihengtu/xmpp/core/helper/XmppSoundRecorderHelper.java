package com.ihengtu.xmpp.core.helper;

import java.io.File;

import android.media.MediaRecorder;

public class XmppSoundRecorderHelper {

	static final private double EMA_FILTER = 0.6;

	private MediaRecorder mRecorder = null;
	private double mEMA = 0.0;

	public void start(String filepath) {
//		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
//			return;
//		}
		if(XmppFileHelper.getSDCardPath()==null){
			return ;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			mRecorder.setAudioChannels(1);
			mRecorder.setAudioSamplingRate(8000);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(filepath);
			try {
				mRecorder.prepare();
				mRecorder.start();
				mEMA = 0.0;
			} catch (Exception e) {
				System.out.print(e.getMessage());
				File tmp=new File(filepath);
				if(tmp.exists()){
					tmp.delete();
				}
			} 
		}
	}

	public void stop() {
		try{
			if (mRecorder != null) {
				mRecorder.stop();
				mRecorder.release();
				mRecorder = null;
			}
		}
		catch(Exception e){
			e.toString();
		}
	}

	public void pause() {
		try{
			if (mRecorder != null) {
				mRecorder.stop();
			}
		}
		catch(IllegalStateException e){
			e.toString();
		}
	}

	public double getAmplitude() {
		if (mRecorder != null)
			return (mRecorder.getMaxAmplitude() / 2700.0);
		else
			return 0;

	}

	public double getAmplitudeEMA() {
		double amp = getAmplitude();
		mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
		return mEMA;
	}

}
