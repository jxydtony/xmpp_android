package com.ihengtu.xmpp.core.group;

import java.util.List;

import com.ihengtu.xmpp.core.group.ConferenceUser;
import com.ihengtu.xmpp.core.group.XmppGroup;
import com.ihengtu.xmpp.core.group.XmppGroupIQ;

/** 
* @ClassName: IGroupAdminOption 
* @Description: TODO 群组操作接口
* @author hepengcheng
* @date 2015年3月3日 下午2:19:55 
*  
*/
public interface IGroupAdminOption extends IGroupOption{

	/**创建群组*/
	void createGroup(XmppGroupIQ iq);
	
	/**删除群组*/
	void deleteGroup(XmppGroup group);
	
	/**添加成员*/
	void addGroupMember(XmppGroup group,ConferenceUser user);
	
	/**添加成员列表*/
	void addGroupMemberList(XmppGroup group,List<ConferenceUser> users);
	
	/**移除成员*/
	void removeGroupMember(XmppGroup group,ConferenceUser user);
	
	/**移除成员列表*/
	void removeGroupMemberList(XmppGroup group,List<ConferenceUser> users);
	
	/**修改群组属性*/
	void updateGroup(XmppGroup group);
}
