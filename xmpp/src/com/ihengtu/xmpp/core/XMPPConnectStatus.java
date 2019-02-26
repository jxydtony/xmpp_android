package com.ihengtu.xmpp.core;

/**
 * @author Administrator
 * xmpp connect status 常量值
 */
public class XMPPConnectStatus {

	public static final int CONNECT_NETWORK_ERROR=0x21; //XMPP 连接关闭，网络断开或者ping包无回应
	public static final int CONNECT_CONFLICT_ERROR=0x22; //XMPP 连接关闭，登录冲突
	public static final int CONNECT_RECONNECT_SUCC=0x23; //XMPP 重连成功
	public static final int CONNECT_RECONNECT_FAILUER=0x24; //XMPP 重连失败
	public static final int CONNECT_CLOSED=0x25; //XMPP 连接关闭，接收到服务器<stream:/stream>
	public static final int CONNECT_SUCC=0x26; //XMPP 登录连接成功
	public static final int CONNECT_EXCEPTION_ERROR=0x27; //XMPP 其他异常错误
}
