package com.ihengtu.xmpp.core.handler;
/**
版权所有：版权所有(C)2014，固派软件
文件名称：com.ihengtu.xmpp.core.XmppMessageReceiptListener.java
系统编号：
系统名称：Im_forAiDL
模块编号：
模块名称：
设计文档：
创建日期：2014-4-25 下午5:01:34
作 者：何鹏程
Version: 1.0
内容摘要：回执监听接口
类中的代码包括三个区段：类变量区、类属性区、类方法区。
文件调用:
 */
public interface XmppMessageReceiptListener {

	void onreceiptReceived(String messageId);
}
