package com.ihengtu.xmpp.core.group;

import com.ihengtu.xmpp.core.group.XmppGroup;

/** 
* @ClassName: IGroupClientOption 
* @Description: TODO 群组客户端操作接口
* @author hepengcheng
* @date 2015年3月3日 下午2:20:27 
*  
*/
public interface IGroupClientOption extends IGroupOption{

	/**客户端主动申请加入群组*/
	void joinGroup(XmppGroup group);
	
	/**客户端主动退出群组*/
	void dropoutGroup(XmppGroup group);
}
