/**   
* @Title: LoginTaskThread.java
* @ProjectName XmppCoreForClientBetaV2_0 
* @Package com.ihengtu.xmpp.core.service 
* @author hepengcheng
* @date 2015年2月5日 下午3:30:00 
* @version V1.0
* Copyright 2013-2015 深圳市点滴互联科技有限公司  版权所有
*/
package com.ihengtu.xmpp.core.service;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;

import com.ihengtu.xmpp.core.manager.LoginManager;
import com.ihengtu.xmpp.core.manager.LoginManager.LoginStatus;

/** 
 * @ClassName: LoginTaskThread 
 * @Description: TODO Xmpp Service 登录服务器线程
 * @author hepengcheng
 * @date 2015年2月5日 下午3:30:00 
 *  
 */
public class LoginServerThread extends Thread{

	private static final long SLEEP_TIME=3000;
	
	private String username;
	private String password;
	private String platname;
	private String tag;
	
	private boolean mAllowLogined=true;
	
	private Connection mConnect=null;
	
	private OnLoginResponse mOnLoginResponse=null;
	
	public LoginServerThread(Connection connect,String username, String password,String platname,String tag){
		this.mConnect=connect;
		this.username=username;
		this.password=password;
		this.platname=platname;
		this.tag=tag;
	}
	
	public  LoginServerThread(Connection connect,String username,String password,String tag){
		this.mConnect=connect;
		this.username=username;
		this.password=password;
		this.platname="android";
		this.tag=tag;
	}
	
	void stoplogin(){
		mAllowLogined=false;
		mConnect=null;
	}
	
	void setOnLoginResponse(OnLoginResponse response){
		this.mOnLoginResponse=response;
	}
	
	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		if (mConnect != null && username != null && password != null
				&& !"".equals(username)) {
			super.start();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		while (allowLoginning()) {
			log("ImService login xmppServer");
			// 首次登陆，直接调用登陆方法，否则进行重连
			if (LoginManager.getInstance().getLoginStatus() == LoginStatus.unlogined) {
				try {
					log("ImService login ....");
					if (mConnect.isAuthenticated()) {
						loginSuccessful();
						break;
					}
					log("ImService login connecting...");
					LoginManager.getInstance().setLoginStatus(LoginStatus.loginning);
					mConnect.connect();
					mConnect.login(username, password, platname, tag);
					if (mConnect.isAuthenticated()) {
						loginSuccessful();
					}
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					log("login XmppException :"+e.getMessage());
					loginFailure(e);
				} catch (IllegalStateException e) {
					e.printStackTrace();
					log("login IllegalStateException :"+e.getMessage());
					if(mConnect.isAuthenticated()){
						mAllowLogined=false;
						LoginManager.getInstance().setLoginStatus(LoginStatus.logined);
					}
					else{
						//暂时不做处理，自动重新连接服务器
						loginFailure(e);
					}
				} catch (Exception e) {
					e.printStackTrace();
					log("login Exception :"+e.getMessage());
					loginFailure(e);
				}
			}

			// 否则如果已经登录过，则进行重新连接
			else if (allowReconnecting()) {
				try {
					log("ImService connect server...");
					// LoginManager.getInstance().setReconnecting(true);
					LoginManager.getInstance().setLoginStatus(
							LoginStatus.reconnecting);
					mConnect.connect();
				} catch (Exception e) {
					e.printStackTrace();
					// LoginManager.getInstance().setReconnecting(false);
					LoginManager.getInstance().setLoginStatus(
							LoginStatus.reconnectfailured);
				}
			}
			
			//间隔500毫秒后再次尝试登录
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				mAllowLogined=false;
			}
		}
	}
	
	private boolean allowLoginning(){
		LoginManager manager=LoginManager.getInstance();
		if(mAllowLogined==false
				||manager.getLoginStatus()==LoginStatus.logined
				||manager.getLoginStatus()==LoginStatus.loginconfilcted
				||manager.getLoginStatus()==LoginStatus.loginning){
			return false;
		}
		return true;
	}
	
	private boolean allowReconnecting(){
		LoginManager manager=LoginManager.getInstance();
		if(manager.getLoginStatus()==LoginStatus.logined
				||manager.getLoginStatus()==LoginStatus.loginconfilcted
				||manager.getLoginStatus()==LoginStatus.reconnecting
				||manager.getLoginStatus()==LoginStatus.loginning){
			return false;
		}
		return true;
	}
	
	private void loginSuccessful(){
		log("ImService login succ");
		mAllowLogined=false;
		LoginManager.getInstance().setLoginStatus(LoginStatus.logined);
		if(mOnLoginResponse!=null){
			mOnLoginResponse.onloginSuccessful();
		}
	}
	
	private void loginFailure(Exception e){
		//XMPPException handle
		if(e instanceof XMPPException){
			LoginManager.getInstance().setLoginStatus(LoginStatus.unlogined);
		}
		//IllegalStateException handle
		else if(e instanceof IllegalStateException){
			LoginManager.getInstance().setLoginStatus(LoginStatus.unlogined);
		}
		//other exception handle
		else{
			mAllowLogined=false;
			LoginManager.getInstance().setLoginStatus(LoginStatus.unlogined);
		}
		if(mOnLoginResponse!=null){
			mOnLoginResponse.onloginFailure(e);
		}
	}
	
	private void log(String msg) {
		if (Connection.DEBUG_ENABLED) {
			android.util.Log.d("login", "" + msg);
		}
	}
}
