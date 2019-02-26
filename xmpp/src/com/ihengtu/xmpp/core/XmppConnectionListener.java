package com.ihengtu.xmpp.core;
/** 
* @ClassName: XmppConnectionListener 
* @Description: TODO 本地封装xmpp当前连接状态，用于Service或者客户端代码监听XmppConnection连接状态
* @author hepengcheng
* @date 2015年3月3日 上午11:39:39 
*  
*/
public interface XmppConnectionListener {
	
	void connectionClosed();
	
	void reconnectingIn();
	
	void reconnectionSuccessful();
	
	void reconnectionFailed(Exception e);
	
	void connectionClosedOnUserConflict(Exception e);
	
	void connectionClosedOnNetworkError(Exception e);
}
