package com.ihengtu.xmpp.core.manager;

import java.util.HashMap;
import java.util.Map;

/**
版权所有：版权所有(C)2014，固派软件
文件名称：com.ihengtu.xmpp.core.manager.ChatThreadManager.java
系统编号：
系统名称：Im_forAiDL
模块编号：
模块名称：
设计文档：
创建日期：2014-5-6 下午4:58:40
作 者：何鹏程
Version: 1.0
内容摘要：im chat thread manager
类中的代码包括三个区段：类变量区、类属性区、类方法区。
文件调用:
 */
public class ChatThreadManager {

	private static Map<String, String> chatThreads = new HashMap<String, String>();
	
	private ChatThreadManager(){}
	
	
	/**
	 * @param jid 123@goopai.cn/android
	 * @param threadId 
	 */
	public static void put(String jid,String threadId){
		if(jid==null|| threadId==null){
			return ;
		}
		synchronized (chatThreads) {
			chatThreads.put(jid, threadId);
		}
	}
	
	/**
	 * @param jid 123@goopai.cn/android
	 * @return
	 */
	public static String get(String jid){
		if(jid==null){
			return null;
		}
		String threadId=chatThreads.get(jid);
		return threadId;
	}
	
	/**
	 * @param jid  123@goopai.cn/android
	 */
	public static void remove(String jid){
		if(jid==null){
			 return ;
		}
		synchronized (chatThreads) {
			chatThreads.remove(jid);
		}
	}
}
