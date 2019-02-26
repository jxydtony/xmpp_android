package com.ihengtu.xmpp.core.handler;

import org.jivesoftware.smack.packet.Message;

/** 
* @ClassName: XmppMessageHandle 
* @Description: TODO 架包给客户端处理Message消息的接口，以及客户端更新回执的结果
* @author hepengcheng
* @date 2015年3月3日 上午11:41:29 
*  
*/
public interface XmppMessageHandler {

	void addXmppMessage(Message msg);
	
	boolean updateReceipt(String messageid,int status,boolean needtrigeed);
}
