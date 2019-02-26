package com.ihengtu.xmpp.core;

import org.jivesoftware.smack.packet.Presence;

/**
 * 当花名册中好友信息改变时候，回调处理
 * */
public interface XmppSubscriptionRequestListener {

	/**当收到好友请求时*/
	void onReceiveSubscriptoinRequest(Presence packet);
	

}
