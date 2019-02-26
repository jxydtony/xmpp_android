package com.ihengtu.xmpp.core.login;

import android.os.Parcel;
import android.os.Parcelable;

/** 
* @ClassName: XmpploginEntity 
* @Description: TODO IM 登录实体
* @author hepengcheng
* @date 2015年3月3日 下午2:54:23 
*  
*/
public class XmpploginEntity implements Parcelable{

	//登录
	public static final int XMPP_LOGIN=0;
	
	//注销
	public static final int XMPP_CANCEL=1;
	
	//Xmpp login user key
	public static final String XMPP_USER_KEY="userkey";
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPlatname() {
		return platname;
	}

	public void setPlatname(String platname) {
		this.platname = platname;
	}

	public int getOptionType() {
		return optionType;
	}

	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	String username;
	
	String password;
	
	String platname;
	
	int optionType=-1;
	
	String tag;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(username);
		dest.writeString(password);
		dest.writeString(platname);
		dest.writeInt(optionType);
		dest.writeString(tag);
	}
	
	public static final Parcelable.Creator<XmpploginEntity> CREATOR = new Parcelable.Creator<XmpploginEntity>() {

		public XmpploginEntity createFromParcel(Parcel source) {
			XmpploginEntity entity=new XmpploginEntity();
			entity.setUsername(source.readString());
			entity.setPassword(source.readString());
			entity.setPlatname(source.readString());
			entity.setOptionType(source.readInt());
			entity.setTag(source.readString());
			return entity;
		}

		public XmpploginEntity[] newArray(int size) {
			return new XmpploginEntity[size];
		}
	};
	
}
