package com.ihengtu.xmpp.core.helper;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class XmppFileHelper {

	// ��SDcard���ϴ���һ��·���������ļ���
	public static void createFolderOnSDcard(String saveFileAddress) {
		File sendfile = new File(saveFileAddress);
		if (!sendfile.exists()) {
			sendfile.mkdirs();
		}
	}

	/**
	 * �жϵ�ǰ��ַָ����ļ��Ƿ���ڣ��������򴴽�һ��
	 * 
	 * @param toFile
	 *            ����Ҫ�������ļ���
	 */
	public static void createFileIfNotExists(String toFile) {
		// ���toFile���ļ�������,�򴴽�һ��
		try {
			File file = new File(toFile);
			if (!file.exists())
				file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * �ж�SD���Ƿ����
	 * 
	 * @return
	 */
	public static boolean ExistSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}
	
	public static String getSDCardPath(){
		if(ExistSDCard()){
			File dir=new File(Environment.getExternalStorageDirectory().getPath());
			if(dir.exists()){
				return Environment.getExternalStorageDirectory().getPath();
			}
			else{
				return "/mnt/sdcard";
			}
		}
		else{
			File internelSdcard=new File("/mnt/sdcard-ext");
			if(internelSdcard.exists()){
				return internelSdcard.getAbsolutePath();
			}
		}
		return null;
	}

}
