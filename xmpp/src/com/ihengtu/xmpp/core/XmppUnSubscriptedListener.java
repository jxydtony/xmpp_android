package com.ihengtu.xmpp.core;


/**已经与对方解除好友关系监听器
 * 或者是当一个好友发来好友请求后，发送这个包忽略掉
 * 如果不发 这个包，则下次重新登录后还会受到对方发来的好友请求信息
 * */
public interface XmppUnSubscriptedListener {

	/**
	 * 移除好友
	 * @param user 要移除的好友名称，nickName
	 * */
	void unsubscripted(String user);
}
