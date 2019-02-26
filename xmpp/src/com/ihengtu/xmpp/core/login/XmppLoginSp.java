/**   
* @Title: XmppLoginSp.java
* @ProjectName XmppCoreForClientBetaV2_0 
* @Package com.ihengtu.xmpp.core.login 
* @author hepengcheng
* @date 2015年2月7日 下午3:26:48 
* @version V1.0
* Copyright 2013-2015 深圳市点滴互联科技有限公司  版权所有
*/
package com.ihengtu.xmpp.core.login;

import android.content.Context;
import android.content.SharedPreferences;

/** 
 * @ClassName: XmppLoginSp 
 * @Description: TODO Xmpp 登录用户信息保存，
 * 保存自当前应用程序私有目录，以sharedprefrence键值对保存
 * @author hepengcheng
 * @date 2015年2月7日 下午3:26:48 
 *  
 */
public class XmppLoginSp {

	/** 
	* @Title: saveXmppLoginInfo 
	* @Description: TODO 保存用户登录信息
	* @param context
	* @param strings  分别是：username ,password,platname,tag    
	* @return void    
	* @throws 
	*/
	public synchronized static void saveXmppLoginInfo(Context context,String...strings ){
		if(strings==null||strings.length==0){
			return ;
		}
		 SharedPreferences sp=context.getSharedPreferences("im", 0);
		 SharedPreferences.Editor editor=sp.edit();
		 editor.putString("username", strings[0]);
		 editor.putString("password", strings[1]);
		 editor.putString("platname", strings[2]);
		 editor.putString("tag", strings[3]);
		 editor.commit();
	}
	
	/** 
	* @Title: getXmppLoginInfo 
	* @Description: TODO 获取临时登录信息
	* @param context
	* @return     
	* @return StringBuilder    
	* @throws 
	*/
	public synchronized static StringBuilder getXmppLoginInfo(Context context){
		SharedPreferences sp=context.getSharedPreferences("im", 0);
		StringBuilder sb=new StringBuilder();
		String username=sp.getString("username",null);
		if(username!=null){
			sb.append(username);
			sb.append(",");
		}
		String password=sp.getString("password",null);
		if(password!=null){
			sb.append(password);
			sb.append(",");
		}
		String platname=sp.getString("platname", null);
		if(platname!=null){
			sb.append(platname);
			sb.append(",");
		}
		String tag=sp.getString("tag", null);
		if(tag!=null){
			sb.append(tag);
		}
		return sb;
	}
	
	public static void clearXmppLoginInfo(Context context){
		SharedPreferences sp=context.getSharedPreferences("im", 0);
		SharedPreferences.Editor editor=sp.edit();
		editor.clear();
		editor.commit();
	}
	
}
