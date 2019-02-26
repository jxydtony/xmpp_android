package com.ihengtu.xmpp.core.login;

import android.content.Context;
import android.content.Intent;

import com.ihengtu.xmpp.core.service.ImService;

/** 
* @ClassName: XmppLogin 
* @Description: TODO Xmpp 登录与登出
* @author hepengcheng
* @date 2015年3月3日 下午2:53:36 
*  
*/
public class XmppLogin {

	/**
	 * XMPP login 账号登录
	 * @param context
	 * @param username
	 * @param password
	 * @param platname 登录设备的平台 eg:android ios pc
	 * @param tag 设备登录标识符，用于避免同一台设备将自己挤下线
	 */
	public static void login(Context context,String username,String password,String platname
			,String tag){
		Intent xmpp=new Intent();
//		xmpp.setAction(XmppAction.IM_SERVICE_INTENT_ACTION);
		xmpp.setClass(context, ImService.class);
		XmpploginEntity entity=new XmpploginEntity();
		entity.setUsername(username);
		entity.setPassword(password);
		entity.setOptionType(XmpploginEntity.XMPP_LOGIN);
		entity.setTag(tag);
		xmpp.putExtra(XmpploginEntity.XMPP_USER_KEY, entity);
		context.startService(xmpp);
	}
	
	/**
	 * XMPP loginout 账号登出
	 * @param context
	 * @param username
	 */
	public static void loginout(Context context,String username){
		Intent xmpp=new Intent();
		xmpp.setClass(context, ImService.class);
		XmpploginEntity entity=new XmpploginEntity();
		entity.setUsername(username);
		entity.setOptionType(XmpploginEntity.XMPP_CANCEL);
		xmpp.putExtra(XmpploginEntity.XMPP_USER_KEY, entity);
		context.startService(xmpp);
	}
}
