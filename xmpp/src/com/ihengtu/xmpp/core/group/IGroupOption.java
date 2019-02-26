package com.ihengtu.xmpp.core.group;

import com.ihengtu.xmpp.core.group.XmppGroup;


/**
 * 
 * 群组操作的父接口，定义公共操作接口
 * */
public interface IGroupOption {

	/**根据群名称查询群信息*/
	XmppGroup getGroupInfoFromName(String groupName);
	
	/**根据群ID查询群信息*/
	XmppGroup getGroupInfoFromNumber(String groupNumber);
	
	/**根据群ID查询群成员*/
	XmppGroup getGroupMembers(String groupNumber);
}
