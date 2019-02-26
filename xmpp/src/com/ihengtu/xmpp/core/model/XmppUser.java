package com.ihengtu.xmpp.core.model;

import android.os.Parcel;
import android.os.Parcelable;


/** 
* @ClassName: XmppUser 
* @Description: TODO im user 实体类
* @author hepengcheng
* @date 2015年3月3日 下午3:01:02 
*  
*/
public class XmppUser implements Parcelable{

	public static String userKey="userkey";
	
	private String username;
	
	/**
	 * �û�Jid
	 * eg: 10061@goopai.cn
	 * */
	private String jid;
	
	private String groupname;
	
	private byte[] head=new byte[]{};
	
	private UserStatus status=UserStatus.OFFLINE;
	
	
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public String getGroupname() {
		return groupname;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public byte[] getHead() {
		return head;
	}

	public void setHead(byte[] head) {
		this.head = head;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.username);
		dest.writeString(this.jid);
		dest.writeString(this.groupname);
		dest.writeString(this.status.toString());
		dest.writeByteArray(this.head);
	}
	
	
	public static final Parcelable.Creator<XmppUser> CREATOR = new Parcelable.Creator<XmppUser>() {

		public XmppUser createFromParcel(Parcel source) {
			XmppUser user = new XmppUser();
			user.setUsername(source.readString());
			user.setJid(source.readString());
			user.setGroupname(source.readString());
			user.setStatus(UserStatus.getUserStatus(source.readString()));
			byte[] post=new byte[]{};
			source.readByteArray(post);
			user.setHead(post);
			return user;
		}

		public XmppUser[] newArray(int size) {
			return new XmppUser[size];
		}
	};
	
	
	@Override
	public XmppUser clone() {
		XmppUser user = new XmppUser();
		user.setJid(XmppUser.this.jid);
		user.setUsername(XmppUser.this.username);
		return user;
	}
	
	
	/**
	 * �û�״̬ö��
	 * ���ߣ����ߣ��뿪��æµ�������
	 * */
	public enum UserStatus{
		
		/**
		 * ����
		 * */
		ONLINE("online"),
		
		/**
		 * ����
		 * */
		OFFLINE("offline"),
		
		/**
		 * �뿪
		 * */
		LEAVE("leave"),
		
		/**
		 * æµ
		 * */
		BUSY("busy"),
		
		/**
		 * ����
		 * */
		STEALTH("stealth");
		
		
		private String TypeCode;
		
		/**
		 * ����һ������ָ���ַ��ö��ֵ
		 * @param inTypeCode
		 */
		private UserStatus(String inTypecode){
			this.TypeCode=inTypecode;
		}
		
		public String toString(){
			return TypeCode;
		}
		
		/**
		 * ���ָ�����ַ��ȡ��Ӧ��ö��ֵ
		 * @param inActionCode
		 * @return
		 */
		public static UserStatus getUserStatus(String inTypeCode){
			for(UserStatus type:UserStatus.values()){
				if(type.TypeCode.equals(inTypeCode))
					return type;
			}
			return null;
		}
	}
	
}
