package com.ihengtu.xmpp.core.group;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.IQ;

/** 
* @ClassName: XmppGroupIQ 
* @Description: TODO 发送群信息实体IQ,继承自架包IQ
* @author hepengcheng
* @date 2015年3月3日 下午2:23:23 
*  
*/
public class XmppGroupIQ extends IQ{

	//static
//	public static final String CONFERENCE_NAME_SPACE="jabber:iq:conference";
	public static final String CONFERENCE_NAME_SPACE="jabber:iq:group";
	
	//static
//	public static final String NODENAME = "conference";
	public static final String NODENAME = "group";
	
	private String roomname;
	
	private int roomsize;
	
	private String description;
	
	private XmppGroupAction action;
	
	private String number;
	
	private String time;
	
	private String status;

	public static boolean isparserList=false;
    
	public  List<XmppGroup> imGroups=new ArrayList<XmppGroup>();
	
	private boolean isLogin=false;
	
	/**
     * The default constructor
     */
	public XmppGroupIQ(){
		roomsize=0;
	}
	
	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getRoomname() {
		return roomname;
	}


	public void setRoomname(String roomname) {
		this.roomname = roomname;
	}


	public int getRoomsize() {
		return roomsize;
	}


	public void setRoomsize(int roomsize) {
		this.roomsize = roomsize;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public XmppGroupAction getAction() {
		return action;
	}


	public void setAction(XmppGroupAction action) {
		this.action = action;
	}


	/**
	 * @return this element name of Conference
	 * 
	 */
	public static String getElementName() {
		return NODENAME;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String createtime) {
		this.time = createtime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setIsLogin(boolean login){
		this.isLogin=login;
	}
	
	
	/**
     * Return the XML representation of the packet.
     *
     * @return the XML string
     */
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		
//		    buf.append("<conference-qurey xmlns=\""+CONFERENCE_NAME_SPACE+"\"/>");
		    if(isLogin)
		    	return buf.toString();
		    buf.append("<").append(getElementName()).append(" ");
		    if(getRoomname()!=null)
		    	buf.append("name=\""+getRoomname()+"\" ");
		    if(getDescription()!=null)
		    	buf.append("description=\""+getDescription()+"\" ");
		    if(getRoomsize()>0)
		    	buf.append("size=\""+getRoomsize()+"\" ");
		    if(getNumber()!=null)
//		    	buf.append("number=\""+getNumber()+"\" ");
		    	buf.append("id=\""+getNumber()+"\" ");
		    if(getAction()!=null)
		    	buf.append("action=\""+getAction().toString()+"\"");
		    buf.append(">");
		    //add create time
		    if(getTime()!=null)
		    	buf.append("<time>").append(getTime()).append("</time>");
		    //add photo to conferenceIQ packet
		    buf.append("</").append(getElementName()).append(">");
		    
		return buf.toString();
	}
	
	
	public  void addGroup(XmppGroup group){
		if(group==null)
			return;
		synchronized (imGroups) {
			imGroups.add(group);
		}
	}
	
	public List<XmppGroup> getImGroupList()
	{
		List<XmppGroup> groups=null;
		
		synchronized (imGroups) {
		   groups=new ArrayList<XmppGroup>(imGroups);
		}
		
		return groups;
	}
	
}
