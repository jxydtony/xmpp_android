package com.ihengtu.xmpp.core;

/**
 * 定义好友操作接口
 * 包括IM好友的增加，删除，更新，查询
 * */
public interface ISubscriptionOption {

	/**
	 * 添加好友
	 * */
	void addSubscription(String JID);
	
	/**
	 * 删除好友
	 * 
	 * */
	void removeSubscription(String JID);
	 
	/**
	 * 查找指定好友
	 * */
	void querySubscription(String JID);
	
	/**
	 * 接受好友请求信息
	 * */
	void acceptSubscription(String JID);
	
	/**
	 * 抑或忽略好友请求信息
	 * */
	void rejuectSubscription(String JID);
}
