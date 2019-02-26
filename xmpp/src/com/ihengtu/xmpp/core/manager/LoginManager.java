package com.ihengtu.xmpp.core.manager;

/** 
* @ClassName: LoginManager 
* @Description: TODO 登录管理器，管理当前登录状态，登录账号信息等
* @author hepengcheng
* @date 2015年3月3日 下午2:56:07 
*  
*/
public class LoginManager {

	private static LoginManager instance;
	
	private String imuser="";
	
	private LoginStatus mLoginStatus=null;
	
	private long timedifference=0L;

	private LoginManager() {
		// TODO Auto-generated constructor stub
		//默认登录状态
		mLoginStatus=LoginStatus.unlogined;
	}
	
	/**
	 * @return
	 */
	public static LoginManager getInstance(){
		if(instance==null)
			instance=new LoginManager();
		return instance;
	}
	
	public static void destroy(){
		instance=null;
	}
	
	public String getImuser() {
		return imuser;
	}

	public void setImuser(String imuser) {
		this.imuser = imuser;
	}

	public long getTimedifference() {
		return timedifference;
	}

	public void setTimedifference(long timedifference) {
		this.timedifference = timedifference;
	}
	
	/** 
	 * @return mLoginStatus 
	 */
	public LoginStatus getLoginStatus() {
		return mLoginStatus;
	}

	/** 
	 * @param mLoginStatus 要设置的 mLoginStatus 
	 */
	public void setLoginStatus(LoginStatus mLoginStatus) {
		this.mLoginStatus = mLoginStatus;
	}

	public static enum LoginStatus{
		/** 
		* @Fields unlogined : TODO 首次登录，未登录状态，判断是否是第一次登录
		*/ 
		unlogined,
		
		/** 
		* @Fields loginning : TODO 登录中
		*/ 
		loginning,
		
		/** 
		* @Fields logined : TODO 已登录
		*/ 
		logined,
		
		/** 
		* @Fields reconnecting : TODO 重连中
		*/ 
		reconnecting,
		
		/** 
		* @Fields reconnectfailured : TODO 重连失败
		*/ 
		reconnectfailured,
		
		/** 
		* @Fields loginconfilcted : TODO 登录冲突，账号被迫下线
		*/ 
		loginconfilcted,
	}
	
}
