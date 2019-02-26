package com.ihengtu.xmpp.core;


/** 
* @ClassName: XmppAction 
* @Description: TODO Xmpp发送本地广播 的Action
* @author hepengcheng
* @date 2015年3月3日 上午9:45:53 
*  
*/
public class XmppAction {

	public static final String ROSTER_DELETED = "business.im.roster.deleted";
	public static final String ROSTER_DELETED_KEY = "business.im.roster.deleted.key";
	
	public static final String ROSTER_UPDATED = "business.im.roster.updated";
	public static final String ROSTER_UPDATED_KEY = "business.im.roster.updated.key";
	   
	public static final String ROSTER_ADDED = "business.im.roster.added";
	public static final String ROSTER_ADDED_KEY = "business.im.roster.added.key";
	
	public static final String ROSTER_PRESENCE_CHANGED = "business.im.presence.changed";
	public static final String ROSTER_PRESENCE_CHANGED_KEY = "business.im.presence.changed.key";
	public static final String ROSTER_PRESENCE_CHANGED_VALUE = "business.im.presence.changed.value";
	
	public static final String ROSTER_SUBSCRIPTION = "business.im.subscribe";
	public static final String ROSTER_SUB_FROM = "business.im.subscribe.from";
	
	public static final String NEW_MESSAGE_ACTION = "business.im.newmessage";
	public static final String NEW_MESSAGE_ACTION_VALUE="business.im.newmessage.value";
	
	public static final String MESSAGE_READED_ACTION="business.im_msg_readed";
	public static final String MESSAGE_READED_ACTION_VALUE="business.im_msg_readed_value";
	
	public static final String DELETE_MESSAGE_ACTION="business.im.deletemessage";
	public static final String DELETE_MESSAGE_ACTION_VALUE="business.im.deletemessage.value";
	
	public static final String IM_CHAT_MSG="im_chat_msg"; 
	public static final String IM_PUSH_MSG="im_push_msg";  
	public static final String IM_CATER_MSG="im_cater_msg";  
	
	public static final String MESSAGE_OFFLINE_ACTION="business.im.offline.message.action";
	
	public static final String SYSTEM_REBACK_EVENT_ACTION="business.im.reback.event";
	
	public static final String IM_SERVICE_INTENT_ACTION="business.im.xmppservice";
	
	public static final String ACTION_ALERTDIALOG_ACTIVITY="business.conflict.dialog";
	
	public static final String ACTION_CONNECTION_RESETSUCC="business.im.xmppconnectreset";
	
	public static final String ACTION_LOGIT_CONFLICT="business.im.xmppconflict";
	
	public static final String ACTION_CONNECTION_STATUS_CHANGED="business.im.xmppchanged";
	
}
