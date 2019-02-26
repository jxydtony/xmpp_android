package com.ihengtu.xmpp.core.group;

import java.io.Serializable;


/** 
* @ClassName: XmppGroup 
* @Description: TODO 群组实体类
* @author hepengcheng
* @date 2015年3月3日 下午2:20:49 
*  
*/
public class XmppGroup implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_GROUP_SIZE=30;
	
	private String number;
	
	private String name;
	
	private int size=DEFAULT_GROUP_SIZE;
	
	private String description="";
	
	private String createtime;
	
	private String path;
	
	private String action;
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getNumber() {
		return number;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

}
