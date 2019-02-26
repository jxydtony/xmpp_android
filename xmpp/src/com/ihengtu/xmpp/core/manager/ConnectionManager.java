package com.ihengtu.xmpp.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionCreationListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.ibb.provider.CloseIQProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.DataPacketProvider;
import org.jivesoftware.smackx.bytestreams.ibb.provider.OpenIQProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.BytestreamsProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.os.Build;

import com.ihengtu.xmpp.core.XmppConnectionListener;
import com.ihengtu.xmpp.core.manager.LoginManager.LoginStatus;

/** 
* @ClassName: ConnectionManager 
* @Description: TODO Xmpp 连接管理器,管理连接，断开连接等
* @author hepengcheng
* @date 2015年3月3日 下午2:54:57 
*  
*/
public class ConnectionManager implements ConnectionListener,ConnectionCreationListener{
	
	private String CONNECT_LOCK="lock";
	
	private static final int default_port=6222;
	//发布正式版本时，需要修改对应的默认服务器地址
//	private static final String default_host="www.goopai.cn";
	private static final String default_host="120.24.62.127";
	
	private XMPPConnection connection=null;
	private ConnectionConfiguration connectionConfig;
	public static int port =  default_port;
	public static String host = default_host;
	public static boolean DEBUG_LOG=true;
	private List<XmppConnectionListener> listeners=new ArrayList<XmppConnectionListener>();;
	private static ConnectionManager instance=null;
	
	private ConnectionManager(){}
	
	public static ConnectionManager getInstance(){
		if(instance==null){
			instance=new ConnectionManager();
			//监听连接建立
			Connection.addConnectionCreationListener(instance);
		}
		return instance;
	}
	
	public static void destory(){
		if(instance!=null){
			Connection.removeConnectionCreationListener(instance);
			instance=null;
		}
	}
	
	private ConnectionConfiguration configure(){
		java.lang.System.setProperty("java.net.preferIPv4Stack", "true");  
        java.lang.System.setProperty("java.net.preferIPv6Addresses", "false");  
		org.jivesoftware.smack.Connection.DEBUG_ENABLED = DEBUG_LOG;
		ProviderManager pm = ProviderManager.getInstance();
		configure(pm);
		SmackConfiguration.setKeepAliveInterval(30000 * 1); // 1 mins
		SmackConfiguration.setLocalSocks5ProxyEnabled(false);
		
		connectionConfig = new ConnectionConfiguration(host,port, ""+host);
		connectionConfig.setSASLAuthenticationEnabled(true);
		connectionConfig.setSecurityMode(SecurityMode.disabled); // SecurityMode.required/disabled
		connectionConfig.setReconnectionAllowed(true);
		connectionConfig.setSendPresence(false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			connectionConfig.setTruststoreType("AndroidCAStore");
			connectionConfig.setTruststorePassword(null);
			connectionConfig.setTruststorePath(null);
		} else {
			connectionConfig.setTruststorePath("/system/etc/security/cacerts.bks");
			connectionConfig.setTruststorePassword("changeit");
			connectionConfig.setTruststoreType("BKS");
		}
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
		return connectionConfig;
	}
	
	public XMPPConnection getConnection(){
		return connection;
	}
	
	/**
	 * 断开连接
	 */
	public synchronized  void disconnect(){
		destory();
		if(connection!=null){
			connection.removeConnectionListener(this);
			new Thread(){
				public void run(){
					try{
						if(connection.isConnected()){
							connection.disconnect();
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					connection=null;
					synchronized (CONNECT_LOCK) {
						CONNECT_LOCK.notifyAll();
					}
				}
			}.start();
			
			synchronized (CONNECT_LOCK) {
				try {
					CONNECT_LOCK.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/** 
	 * 建立连接，如果第一次连接，则直接返回连接对象，且不进行自动连接，否则进行自动连接，
	 * 而且这个过程是线程同步的
	 * @return
	 */
	public synchronized XMPPConnection connect() {
		if (connection == null) {
			XMPPConnection conn = null;
			conn = new XMPPConnection(configure());
			connection=conn;
			return connection;
		} else {
			Thread thread = new Thread() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					super.run();
					try {
//						Log.d("conn", "connection is connect....");
						connection.connect();
						if(connection.isConnected()){
							connection.addConnectionListener(ConnectionManager.this);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						synchronized (CONNECT_LOCK) {
							CONNECT_LOCK.notifyAll();
						}
					}
				}
			};
			thread.setName("XMPP Connect Thread for ConnectionManager");
			thread.setDaemon(true);
			thread.start();
			return connection;
		}
	}
	
	private static void configure(ProviderManager pm) {

		// Private Data Storage
		pm.addIQProvider("query", "jabber:iq:private",
				new PrivateDataManager.PrivateDataIQProvider());

		// Time
		try {
			pm.addIQProvider("query", "jabber:iq:time",
					Class.forName("org.jivesoftware.smackx.packet.Time"));
		} catch (ClassNotFoundException e) {
		}

		// XHTML
		pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
				new XHTMLExtensionProvider());

		// Roster Exchange
		pm.addExtensionProvider("x", "jabber:x:roster",
				new RosterExchangeProvider());
		// Message Events
		pm.addExtensionProvider("x", "jabber:x:event",
				new MessageEventProvider());
		// Chat State
		pm.addExtensionProvider("active",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("composing",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("paused",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("inactive",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());
		pm.addExtensionProvider("gone",
				"http://jabber.org/protocol/chatstates",
				new ChatStateExtension.Provider());

		// FileTransfer
		pm.addIQProvider("si", "http://jabber.org/protocol/si",
				new StreamInitiationProvider());
		
		pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
				new BytestreamsProvider());
		pm.addIQProvider("open", "http://jabber.org/protocol/ibb",
				new OpenIQProvider());
		pm.addIQProvider("close", "http://jabber.org/protocol/ibb",
				new CloseIQProvider());
		pm.addExtensionProvider("data", "http://jabber.org/protocol/ibb",
				new DataPacketProvider());

		// Group Chat Invitations
		pm.addExtensionProvider("x", "jabber:x:conference",
				new GroupChatInvitation.Provider());
		// Service Discovery # Items
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
				new DiscoverItemsProvider());
		// Service Discovery # Info
		pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
				new DiscoverInfoProvider());
		// Data Forms
		pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());
		// MUC XmppUser
		pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
				new MUCUserProvider());
		// MUC Admin
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
				new MUCAdminProvider());
		// MUC Owner
		pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
				new MUCOwnerProvider());
		// Delayed Delivery
		pm.addExtensionProvider("x", "jabber:x:delay",
				new DelayInformationProvider());
		// Version
		try {
			pm.addIQProvider("query", "jabber:iq:version",
					Class.forName("org.jivesoftware.smackx.packet.Version"));
		} catch (ClassNotFoundException e) {
		}
		// VCard
		pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());
		// Offline Message Requests
		pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
				new OfflineMessageRequest.Provider());
		// Offline Message Indicator
		pm.addExtensionProvider("offline",
				"http://jabber.org/protocol/offline",
				new OfflineMessageInfo.Provider());
		// Last Activity
		pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());
		// XmppUser Search
		pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());
		// SharedGroupsInfo
		pm.addIQProvider("sharedgroup",
				"http://www.jivesoftware.org/protocol/sharedgroup",
				new SharedGroupsInfo.Provider());
		// JEP-33: Extended Stanza Addressing
		pm.addExtensionProvider("addresses",
				"http://jabber.org/protocol/address",
				new MultipleAddressesProvider());
	}
	
	/**
	 * @param namespace
	 */
	public void addFeature(String namespace){
		 // Obtain the ServiceDiscoveryManager associated with my XMPPConnection
        ServiceDiscoveryManager discoManager;
		try {
			discoManager = ServiceDiscoveryManager.getInstanceFor(connection);
			// Register that a new feature is supported by this XMPP entity
			discoManager.addFeature(namespace);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param namespace
	 */
	public  void removeFeature(String namespace){
		ServiceDiscoveryManager discoManager;
		 try {
			discoManager=ServiceDiscoveryManager.getInstanceFor(connection);
			discoManager.removeFeature(namespace);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	/**
	 * @param listener
	 */
	public void addXmppConnectionListener(XmppConnectionListener listener){
		if(listener==null) return;
		synchronized (listeners) {
			if(!listeners.contains(listener))
				listeners.add(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public void removeXmppConnectionListener(XmppConnectionListener listener){
		if(listener==null) return;
		synchronized (listeners) {
			listeners.remove(listener);
		}
	}
	
	/**
	 */
	public void triggedXmppConnectionClose(){
		XmppConnectionListener[] listenerArray=null;
		 // Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
               listenerArray=new XmppConnectionListener[listeners.size()];
               this.listeners.toArray(listenerArray);
		}
		
		 // ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].connectionClosed();
		}
	}
	
	/**
	 */
	public void triggedXmppConnectionCloseOnNetworkError(Exception e){
		XmppConnectionListener[] listenerArray=null;
		// Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
			listenerArray=new XmppConnectionListener[listeners.size()];
			this.listeners.toArray(listenerArray);
		}
		
		// ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].connectionClosedOnNetworkError(e);
		}
	}
	
	/**
	 */
	public void triggedXmppConnectionCloseOnUserConflict(Exception e){
		XmppConnectionListener[] listenerArray=null;
		// Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
			listenerArray=new XmppConnectionListener[listeners.size()];
			this.listeners.toArray(listenerArray);
		}
		
		// ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].connectionClosedOnUserConflict(e);
		}
	}
	
	/**
	 */
	public void triggedXmppReconnectingIn(){
		XmppConnectionListener[] listenerArray=null;
		// Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
			listenerArray=new XmppConnectionListener[listeners.size()];
			this.listeners.toArray(listenerArray);
		}
		
		// ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].reconnectingIn();
		}
	}
	
	/**
	 */
	public void triggedXmppreconnectionSuccessful(){
		XmppConnectionListener[] listenerArray=null;
		// Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
			listenerArray=new XmppConnectionListener[listeners.size()];
			this.listeners.toArray(listenerArray);
		}
		
		// ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].reconnectionSuccessful();
		}
	}
	
	/**
	 */
	public void triggedXmppreconnectionFailed(Exception e){
		XmppConnectionListener[] listenerArray=null;
		// Make a synchronized copy of the listenerJingles
		synchronized (listeners) {
			listenerArray=new XmppConnectionListener[listeners.size()];
			this.listeners.toArray(listenerArray);
		}
		
		// ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].reconnectionFailed(e);
		}
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
//		Log.d("login", "ConnectionManager connectionClosed");
		LoginManager.getInstance().setLoginStatus(LoginStatus.unlogined);
		triggedXmppConnectionClose();
	}

	@Override
	public void connectionClosedOnError(Exception e) {
		// TODO Auto-generated method stub
		if(e.toString().contains("stream:error (conflict)")){
			LoginManager.getInstance().setLoginStatus(LoginStatus.loginconfilcted);
			triggedXmppConnectionCloseOnUserConflict(e);
		}
//		else if(e.toString().contains("Connection timed out")){
		else {
			triggedXmppConnectionCloseOnNetworkError(e);
		}
	}

	@Override
	public void reconnectingIn(int seconds) {
		// TODO Auto-generated method stub
		LoginManager.getInstance().setLoginStatus(LoginStatus.reconnecting);
		triggedXmppReconnectingIn();
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		LoginManager.getInstance().setLoginStatus(LoginStatus.logined);
		triggedXmppreconnectionSuccessful();
	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub
		LoginManager.getInstance().setLoginStatus(LoginStatus.reconnectfailured);
		triggedXmppreconnectionFailed(new Exception("reconnect failed..."));
	}

	@Override
	public void connectionCreated(Connection conn) {
		// TODO Auto-generated method stub
		if(connection!=null){
			connection.removeConnectionListener(this);
			connection=null;
		}
		connection=(XMPPConnection) conn;
		connection.addConnectionListener(this);
	}
}
