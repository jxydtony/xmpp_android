package com.ihengtu.xmpp.core.group;

/** 
* @ClassName: XmppOnGroupClientEventListener 
* @Description: TODO 群组普通用户操作结果监听
* @author hepengcheng
* @date 2015年3月3日 下午2:24:49 
*  
*/
public interface XmppOnGroupClientEventListener {

	/**
	 * 加入群组回调
	 * @param groupNumber
	 */
	void onJoinGroup(String groupNumber);
	
	/**
	 * 退出群组回调
	 * @param groupNumber
	 */
	void onDropoutGroup(String groupNumber);
}
