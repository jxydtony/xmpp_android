package com.ihengtu.xmpp.core.group;

import java.io.Serializable;

import org.jivesoftware.smack.packet.PacketExtension;

/** 
* @ClassName: ConferenceUser 
* @Description: TODO 群组用户实体类
* @author hepengcheng
* @date 2015年3月3日 下午2:18:05 
*  
*/
public class ConferenceUser implements PacketExtension,Serializable{

	/**
	 * ���л�ID
	 */
	private static final long serialVersionUID = 3L;

	public static final int DEFAULT_ROLE_TYPE=2;
	
	public static final int ROLE_GROUP_OWNER=0;
	
	public static final int ROLE_GROUP_ADMIN=1;
	
	// the role of this user for group
	private int role=DEFAULT_ROLE_TYPE;
	
	//the status of this group user
	private String status;
	
	// the JID of this user
	private String userJid=null;
	
	//this nick name for this group to user
	private String nick;
	
	//this user's logo path
	private String logo;
	
	// this user's sex 0: men 1:women
	private int sex; 
    
	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getNick() {
		return nick;
	}



	public void setNick(String nick) {
		this.nick = nick;
	}



	public String getLogo() {
		return logo;
	}



	public void setLogo(String logo) {
		this.logo = logo;
	}



	public int getSex() {
		return sex;
	}



	public void setSex(int sex) {
		this.sex = sex;
	}
	
	public int getRole() {
		return role;
	}



	public void setRole(int role) {
		this.role = role;
	}



	public String getUserJid() {
		return userJid;
	}



	public void setUserJid(String userJid) {
		this.userJid = userJid;
	}
	
	// static

    public static final String NODENAME = "user";
    
    //static
    
    public static final String NAMESPACE="jabber:im:conference";
    
    /***
     * Creates a conference user 
     */
    public ConferenceUser(){
       super();
       this.sex=0;
    }
    
    
    
    /**
     * Returns the XML element name of the element.
     *
     * @return the XML element name of the element.
     */
	@Override
	public String getElementName() {
		// TODO Auto-generated method stub
		return NODENAME;
	}

	
	@Override
	public String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	/**
	 * Convert a user element to XML
	 * @return a string with the XML representation
	 * */
	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuffer buff=new StringBuffer();
		buff.append("<").append("user");
		buff.append(" ");
		/*if(getNamespace()!=null)
			buff.append("xmlns=\""+NAMESPACE+ "\" ");*/
		buff.append("role=\""+getRole()+"\"").append(">");
		buff.append(""+getUserJid().substring(0, getUserJid().indexOf("@")));
		buff.append("</user>");
		
		return buff.toString();
	}

}
