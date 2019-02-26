package com.ihengtu.xmpp.core.group;


/** 
* @ClassName: XmppGroupAction 
* @Description: TODO xmpp group action enum
* @author hepengcheng
* @date 2015年3月3日 下午2:21:45 
*  
*/
public enum XmppGroupAction {

	ACTION_GET("get"),
	ACTION_CREATE("create"),
	ACTION_DELETE("del"),
	ACTION_UPDATE("checkAndedit"),
	ACTION_QUERY("query"),
	ACTION_ADD("add"),
	ACTION_REMOVE("remove"),
	ACTION_JOIN("join"),
	ACTION_QUIT("dropout");
	
	private String ActionCode;
	
	private XmppGroupAction(String inActionCode)
	{
		this.ActionCode=inActionCode;
	}
	
	public String toString(){
		return ActionCode;
	}
	
	public static XmppGroupAction getAction(String inActionCode){
		
		for(XmppGroupAction action:XmppGroupAction.values()){
			if(action.ActionCode.equals(inActionCode))
				return action;
		}
		return null;
	}
}
