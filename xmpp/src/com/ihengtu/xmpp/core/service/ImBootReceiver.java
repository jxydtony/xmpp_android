package com.ihengtu.xmpp.core.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ihengtu.xmpp.core.helper.XmppNetworkHelper;
import com.ihengtu.xmpp.core.helper.XmppServiceHelper;
import com.ihengtu.xmpp.core.login.XmppLoginSp;
import com.ihengtu.xmpp.core.login.XmpploginEntity;

/** 
* @ClassName: XmppBootReceiver 
* @Description: TODO  Xmpp 自启动Receiver,接收系统特定事件广播等
* @author hepengcheng
* @date 2014-10-31 上午11:27:05 
*  
*/
public class ImBootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String action=arg1.getAction();
//		Log.d("xmppreceiver", "boot receiver action:"+action);
		//监听网络变化
		if(action.equals("android.net.conn.CONNECTIVITY_CHANGE")){
			if(XmppNetworkHelper.isConnectingToInternet(arg0)){
				start(arg0);
			}
		}
		//监听电话网络信号变化
		else if(action.equals("android.intent.action.SIG_STR")){
			start(arg0);
		}
		//监听界面唤醒,就是解锁
		else if(action.equals("android.intent.action.USER_PRESENT")){
			start(arg0);
		}
		
	}
	
	private void start(final Context context){
		new Thread(){
			public void run() {
				startImService(context);
			}
		}.start();
	}

	private synchronized void startImService(Context context){
		//当前网络可用的话，进行登录
		if(XmppNetworkHelper.isConnectingToInternet(context)){
//			Log.d("xmppreceiver", "im service running:"+isImServiceRunning(context));
			if(!isImServiceRunning(context)){
				StringBuilder sb=XmppLoginSp.getXmppLoginInfo(context);
				Log.d("bootreceiver", "stringbuilder len :"+sb.length()+" and content:"+sb.toString());
				if(sb.length()>0){
					String[] values=sb.toString().split(",");
					Intent xmpp=new Intent();
					xmpp.setClass(context, ImService.class);
					XmpploginEntity entity=new XmpploginEntity();
					entity.setUsername(values[0]);
					entity.setPassword(values[1]);
					entity.setPlatname(values[2]);
					entity.setTag(values[3]);
					entity.setOptionType(XmpploginEntity.XMPP_LOGIN);
					xmpp.putExtra(XmpploginEntity.XMPP_USER_KEY, entity);
					//表明是否架包自启动
					xmpp.putExtra("boot", true);
					context.startService(xmpp);
				}
			}
		}  
	}
	
	/**
	 * 判断ImService是否运行
	 * @param context
	 * @return
	 */
	private boolean isImServiceRunning(Context context){
		boolean isRunning=XmppServiceHelper.isServiceRunning(context,
				"com.ihengtu.xmpp.core.service.ImService");
		return isRunning;
	}
}
