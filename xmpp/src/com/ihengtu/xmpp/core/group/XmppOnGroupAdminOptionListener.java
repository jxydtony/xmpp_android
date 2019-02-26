package com.ihengtu.xmpp.core.group;


/**
 * 群事件监听器
 * 监听群创建，群删除，群员增加，群员移除，群修改消息等
 * */
public interface XmppOnGroupAdminOptionListener {

	/**当群创建后回调该方法*/
	void onGroupCreated(XmppGroup group);
	
	/**当群删除后回调该方法*/
	void onGroupDeleted(XmppGroup group);
	
	/**当群员增加后回调该方法*/
	void onGroupMemberAdded(XmppGroup group);
	
	/**当群员移除后回调该方法*/
	void onGroupMemberRemoved(XmppGroup group);
	
	/**当群组信息修改更新后回调该方法*/
	void onGroupInfoUpdated(XmppGroup group);
}
