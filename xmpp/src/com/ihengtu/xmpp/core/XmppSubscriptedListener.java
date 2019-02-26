package com.ihengtu.xmpp.core;

import org.jivesoftware.smack.packet.Presence;

/**
 * 已经添加对方为好友监听器
 * */
public interface XmppSubscriptedListener {
    
	//已经添加对方为好友
	void subscripted(Presence presence);
}
