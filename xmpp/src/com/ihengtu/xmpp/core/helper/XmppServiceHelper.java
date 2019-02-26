package com.ihengtu.xmpp.core.helper;

import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

/** 
* @ClassName: ServiceUtil 
* @Description: TODO 判断对应服务是否运行
* @author hepengcheng
* @date 2014-10-31 上午10:40:12 
*  
*/
public class XmppServiceHelper {
	
	/**
     * 用来判断服务是否运行.
     * @param context
     * @param className 判断的服务名字：包名+类名
     * @return true 在运行, false 不在运行
     */
	public static boolean isServiceRunning(Context context, String className) {

		boolean isRunning = false;

		ActivityManager activityManager =

		(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningServiceInfo> serviceList

		= activityManager.getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {

			if (serviceList.get(i).service.getClassName().equals(className) == true) {

				isRunning = true;

				break;
			}

		}
		return isRunning;

	}
}
