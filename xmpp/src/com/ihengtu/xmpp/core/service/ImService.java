package com.ihengtu.xmpp.core.service;

import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ihengtu.xmpp.core.ISubscriptionOption;
import com.ihengtu.xmpp.core.XmppAction;
import com.ihengtu.xmpp.core.XmppConnectionListener;
import com.ihengtu.xmpp.core.XmppSubscriptedListener;
import com.ihengtu.xmpp.core.XmppSubscriptionOptionImpl;
import com.ihengtu.xmpp.core.XmppSubscriptionRequestListener;
import com.ihengtu.xmpp.core.handler.MessageManagerHandler;
import com.ihengtu.xmpp.core.helper.XmppTimeHelper;
import com.ihengtu.xmpp.core.login.XMPPConnectStatus;
import com.ihengtu.xmpp.core.login.XmppLoginSp;
import com.ihengtu.xmpp.core.login.XmpploginEntity;
import com.ihengtu.xmpp.core.manager.ConnectionManager;
import com.ihengtu.xmpp.core.manager.ContacterManager;
import com.ihengtu.xmpp.core.manager.LoginManager;
import com.ihengtu.xmpp.core.manager.LoginManager.LoginStatus;
import com.ihengtu.xmpp.core.manager.PresenceManager;
import com.ihengtu.xmpp.core.manager.SubscripterManager;
import com.ihengtu.xmpp.core.model.XmppReceiptMessage;
import com.ihengtu.xmpp.core.model.XmppUser;

/** 
* @ClassName: ImService 
* @Description: TODO Xmpp 后台运行服务,核心服务类
* @author hepengcheng
* @date 2015年3月3日 下午3:02:00 
*  
*/
public class ImService extends Service implements XmppConnectionListener,
		XmppSubscriptionRequestListener, XmppSubscriptedListener {

	private static final String TAG = "ImService";

	private ChatManager chatManager = null;

	private LoginManager mLoginManager = null;

	private PresenceManager presenceManager = null;

	private ContacterManager contacter = null;
	
	private ConnectionManager mConnectionManager=null;

	private static SubscripterManager subscripterManager = null;
	
	private LoginServerThread loginThread =null;

	Roster roster;

	XmpploginEntity loginEntity;
	
	boolean isCanceled=false; //是否登出
	
	private XMPPConnection connection; //xmpp 连接对象

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		log("ImService onCreate");
		mConnectionManager=ConnectionManager.getInstance();
		mLoginManager = LoginManager.getInstance();
		presenceManager = PresenceManager.getInstance();
		contacter = ContacterManager.getInstance();
		createConnection();
	}

	private void createConnection(){
		log("CreateConnection...");
		if(mConnectionManager==null){
			mConnectionManager=ConnectionManager.getInstance();
		}
		connection=mConnectionManager.connect();
		if(connection!=null){
			log("connection is Created!");
			mConnectionManager.addXmppConnectionListener(this);
		}
	}
	
	private void destroyConnection(){
		if(mConnectionManager!=null){
			mConnectionManager.removeXmppConnectionListener(this);
			mConnectionManager.disconnect();
			connection=null;
			mConnectionManager=null;
		}
		ConnectionManager.destory();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			stopSelf();
			return Service.START_NOT_STICKY;
		}
		log("ImService getLogin entity...");
		XmpploginEntity entity = intent
				.getParcelableExtra(XmpploginEntity.XMPP_USER_KEY);
		if (entity != null) {
			loginEntity = entity;
			if (loginEntity.getOptionType() == XmpploginEntity.XMPP_LOGIN) {
				if (isNotAllowedConnected()) {
					return Service.START_REDELIVER_INTENT;
				}
				
				if (loginEntity.getPlatname() == null
						|| "".equals(loginEntity.getPlatname())) {
					loginEntity.setPlatname("android");
				}
				//是否需要保存登录信息
				log("ImService getLogin entity is boot:"+intent.getBooleanExtra("boot",false));
				if(!intent.getBooleanExtra("boot", false)){
					XmppLoginSp.saveXmppLoginInfo(getApplicationContext(), 
							loginEntity.getUsername(),loginEntity.getPassword()
							,loginEntity.getPlatname(),loginEntity.getTag());
				}
				login(loginEntity.getUsername(), loginEntity.getPassword(),
						loginEntity.getPlatname(),loginEntity.getTag());
			} else if (loginEntity.getOptionType() == XmpploginEntity.XMPP_CANCEL) {
				try {
					log("ImService cancel mConnectionConfict=="
							+ (mLoginManager.getLoginStatus()==LoginStatus.loginconfilcted));
					cancel();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// ImService 被异常kill掉后，会自动重启服务进行再次登录
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		serviceHandler.removeCallbacksAndMessages(null);
		if(loginThread!=null){
			loginThread.stoplogin();
			loginThread=null;
		}
		serviceHandler=null;
		LoginManager.destroy();
		PresenceManager.destroy();
		ContacterManager.destory();
		try {
			destroyChatService();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			sendConnectionStatusAction(XMPPConnectStatus.CONNECT_CLOSED);
			System.gc();
		}
	}
	
	private boolean isNotAllowedConnected(){
		if(mLoginManager.getLoginStatus()==LoginStatus.logined){
			return true;
		}
		if(mLoginManager.getLoginStatus()==LoginStatus.reconnecting){
			return true;
		}
		if(mLoginManager.getLoginStatus()==LoginStatus.loginconfilcted){
			return true;
		}
		return false;
	}
	
	private boolean isAllowedLogin(){
		return !isCanceled&&connection!=null
				&&mLoginManager.getLoginStatus()!=LoginStatus.loginning;
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 */
	protected void login(String username, String password,String platname,String tag) {
		 if(isAllowedLogin()){
			 if(loginThread!=null){
					loginThread.stoplogin();
					loginThread=null;
			 }
			 loginThread=new LoginServerThread(connection,username, password, platname,tag);
			 loginThread.setOnLoginResponse(new OnLoginResponse() {
				
				@Override
				public void onloginSuccessful() {
					// TODO Auto-generated method stub
					sendHandlerMsg(0, null);
				}
				
				@Override
				public void onloginFailure(Exception e) {
					// TODO Auto-generated method stub
					if(e instanceof XMPPException){
						sendConnectionStatusAction(XMPPConnectStatus.CONNECT_EXCEPTION_ERROR);
					}
					//IllegalStateException handle
					else if(e instanceof IllegalStateException){
						
					}
					//other exception handle
					else{
						sendConnectionStatusAction(XMPPConnectStatus.CONNECT_CLOSED);
						stopSelf(Service.START_FLAG_REDELIVERY);
					}
				}
			});
			 loginThread.setDaemon(true);
			 loginThread.start();
		 }
	}

	/**
	 * 注销
	 * 
	 * @throws XMPPException
	 */
	private void cancel() throws XMPPException {
		//如果正在登陆，则中断登陆线程
		isCanceled=true;
		XmppLoginSp.clearXmppLoginInfo(getApplicationContext());
		sendConnectionStatusAction(XMPPConnectStatus.CONNECT_CLOSED);
		log("ImService cacel xmppServer");
		resetXmppManager();
		destroyChatService();
		stopSelf();
	}
	
	private void resetXmppManager() throws XMPPException{
		LoginManager.destroy();
		mLoginManager = LoginManager.getInstance();
		PresenceManager.destroy();
		presenceManager = PresenceManager.getInstance();
		ContacterManager.destory();
		contacter = ContacterManager.getInstance();
	}

	/**
	 * 
	 * @throws XMPPException
	 */
	private void initChatService() throws XMPPException {
		roster = connection.getRoster();
		contacter.init(connection);
		roster.addRosterListener(rosterListener);
		chatManager = connection.getChatManager();
		chatManager.removeChatListener(chatListener);
		chatManager.addChatListener(chatListener);
		subscripterManager = SubscripterManager.getInstance();
		subscripterManager.addSubscripRequestListener(this);
		subscripterManager.addSubscriptedListener(this);
//		mConnectionManager.addXmppConnectionListener(this);
		addPresenceListener();
		sendPresence();
	}

	/**
	 * @throws XMPPException
	 */
	private void destroyChatService() throws XMPPException {
		log("ImService destroy xmpp Service");
		if (chatManager != null) {
			chatManager.removeChatListener(chatListener);
			chatManager = null;
		}
		if (roster != null) {
			roster.removeRosterListener(rosterListener);
			roster = null;
		}
		if (subscripterManager != null) {
			subscripterManager.removeSubscripRequestListener(this);
			subscripterManager.removeSubscriptedListener(this);
			subscripterManager = null;
		}
		
		if(connection!=null){
			connection.removePacketListener(presencePacketListener);
		}
		log("ImService xmpp disconnect");
		destroyConnection();
	}
	
	private void sendPresence(){
		if(connection!=null){
			Presence online=new Presence(Type.available);
			try{
				connection.sendPacket(online);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void doLoginSuccessful() {
		// TODO Auto-generated method stub
		if(isCanceled){
			return ;
		}
		try {
			if(connection!=null&&connection.isConnected()){
				synchronized (connection) {
					mLoginManager.setImuser(connection.getUser());
					mLoginManager.setLoginStatus(LoginStatus.logined);
					log("ImService "+mLoginManager.getImuser()+"login succ ");
					log("ImService login succ initXmpp Service");
					initChatService();
				}
			}
			else{
				sendConnectionStatusAction(XMPPConnectStatus.CONNECT_CLOSED);
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @throws XMPPException
	 */
	private void addPresenceListener() throws XMPPException {
		if (presenceManager == null) {
			presenceManager = PresenceManager.getInstance();
		}
		PacketFilter filter = new PacketFilter() {
			public boolean accept(Packet packet) {
				if (packet instanceof Presence) {
					Presence presence = (Presence) packet;
					if (presence.getType().equals(Presence.Type.unavailable)
							|| presence.getType().equals(
									Presence.Type.available)) {
						return true;
					}
				}
				return false;
			}
		};
		connection.addPacketListener(
				presencePacketListener, filter);
	}
	
	/**
	 */
	private PacketListener presencePacketListener = new PacketListener() {
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			Presence presence = (Presence) packet;
			String jid = presence.getFrom();
			if (presenceManager != null) {
				presenceManager.put(jid);
				if (presence.isAvailable()) {
					presenceManager.addToOnlineList(jid);
				} else if (!presence.isAvailable()) {
					presenceManager.removeFromOnlineList(jid);
				}
				presenceManager.triggerPresenceChanged(jid,
						presence.isAvailable());
			}
		}
	};

	/**
	 * Roster listener
	 */
	private RosterListener rosterListener = new RosterListener() {

		/** JID: hpc@goopai.im/android */
		public void presenceChanged(Presence presence) {
			Intent intent = new Intent();
			intent.setAction(XmppAction.ROSTER_PRESENCE_CHANGED);
			String subscriber = presence.getFrom().substring(0,
					presence.getFrom().indexOf("/"));
			RosterEntry entry = roster.getEntry(subscriber);
			if (contacter.contacters.containsKey(subscriber)) {
				intent.putExtra(XmppUser.userKey,
						contacter.contacters.get(subscriber));
				contacter.contacters.remove(subscriber);
				contacter.contacters.put(subscriber,
						contacter.transEntryToUser(entry, roster));
			}
			sendBroadcast(intent);
			// subscripterManager.triggerPresenceChanged(ContacterService.contacters.get(subscriber));
		}

		public void entriesUpdated(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(XmppAction.ROSTER_UPDATED);
				RosterEntry userEntry = roster.getEntry(address);
				XmppUser user = contacter.transEntryToUser(userEntry, roster);
				if (contacter.contacters.get(address) != null) {
					intent.putExtra(XmppUser.userKey,
							contacter.contacters.get(address));
					contacter.contacters.remove(address);
					contacter.contacters.put(address, user);
				}
				sendBroadcast(intent);
			}
		}

		public void entriesDeleted(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(XmppAction.ROSTER_DELETED);
				XmppUser user = null;
				if (contacter.contacters.containsKey(address)) {
					user = contacter.contacters.get(address);
					contacter.contacters.remove(address);
				}
				intent.putExtra(XmppUser.userKey, user);
				sendBroadcast(intent);
				// subscripterManager.triggerUnSubscripted(user.getName());
			}
		}

		public void entriesAdded(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(XmppAction.ROSTER_ADDED);
				RosterEntry userEntry = roster.getEntry(address);
				XmppUser user = contacter.transEntryToUser(userEntry, roster);
				contacter.contacters.put(address, user);
				intent.putExtra(XmppUser.userKey, user);
				sendBroadcast(intent);
			}
		}
	};

	/**
	 * chat message listener
	 */
	private ChatManagerListener chatListener = new ChatManagerListener() {

		public void chatCreated(Chat chat, boolean createdLocally) {
//			 MessageManager.chatThreads.put(chat.getParticipant(),
//			 chat.getThreadID());
			chat.addMessageListener(new MessageListener() {

				public void processMessage(Chat chat, final Message message) {
					Log.d("msg", "process Message");
					// handle message receipt
					if (message.isReceipts()) {
						String receiptId = message.getReceiptId();
						Log.d("receipt", "" + receiptId);
						MessageManagerHandler.getInstance(ImService.this)
								.updateReceipt(receiptId, 1, true);
						return;
					}
					//handle message demand
					if(message.getDemand()!=null){
						Log.d("msg", "process demand msg...");
						processMyMessage(message);
						return ;
					}
					// handle message body
					if (message.getBody() == null
							||message.getBody().equals("")
							||message.getType()==Message.Type.error)
						return;
					Log.d("msg", "process chat msg...");
					processMyMessage(message);

				}
			});
		}
	};

	private synchronized void processMyMessage(final Message message) {
		if (message.isIsrequestReceipts()) {
			sendReceiptMessage(message.getPacketID(), message.getTo(),
					message.getFrom());
		}
		// playMedia();
		long servertime = XmppTimeHelper.getStringtoLong(message
				.getServertime());
		mLoginManager.setTimedifference(XmppTimeHelper
				.getTimeDifference(servertime));
		MessageManagerHandler.getInstance(ImService.this).addMessage(message);
	}
	
	private void notifyConnectSuccessful(){
		if(connection!=null&&connection.isConnected()){
			connection.addConnectionListener(mConnectionManager);
		}
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doLoginSuccessful();
				log("login succ");
			}
		});
		thread.setDaemon(true);
		thread.setName("Connect Succesful Thread");
		thread.start();
	}
	
	private void notifyReconnectServer() throws XMPPException {
		// stopSelfResult(Service.START_REDELIVER_INTENT);
		if (connection != null) {
			try {
				resetXmppManager();
				destroyChatService();
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} finally {
				LoginManager.getInstance().setLoginStatus(LoginStatus.unlogined);
			}
		}
		Thread thread = new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {
					Thread.sleep(DELAY_LOGIN_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (loginEntity!=null) {
					createConnection();
					login(loginEntity.getUsername(), loginEntity.getPassword(), loginEntity.getPlatname(),loginEntity.getTag());
				}
			}
		};
		thread.setDaemon(true);
		thread.setName("Reconnect Server Thread");
		thread.start();
	}
	
	private void notifyReconnectSuccessful(){
		if(connection!=null&&connection.isConnected()){
			connection.addConnectionListener(mConnectionManager);
		}
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				sendPresence();
				log("login reconnect succ");
			}
		});
		thread.setDaemon(true);
		thread.setName("Reconnect Succesful Thread");
		thread.start();
	}
	
	private Handler serviceHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			// 连接成功
			case 0:
				 sendConnectionStatusAction(XMPPConnectStatus.CONNECT_SUCC);
				 notifyConnectSuccessful();
				 log("login succ");
				break;
			// 连接关闭
			case 1:
				 sendConnectionStatusAction(XMPPConnectStatus.CONNECT_CLOSED);
				try {
					notifyReconnectServer();
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 log("login closed");
				break;
			// 网络异常连接关闭
			case 2:
				 sendConnectionStatusAction(XMPPConnectStatus.CONNECT_NETWORK_ERROR);
				 log("login colsed because network error");
				break;
			// 登录冲突连接关闭
			case 3:
				sendConflictAction();
				Intent dialog = new Intent();
				dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				dialog.setAction(XmppAction.ACTION_LOGIT_CONFLICT);
				startActivity(dialog);
				log("login confilt");
				break;
			// 重连成功
			case 4:
				 sendConnectionStatusAction(XMPPConnectStatus.CONNECT_RECONNECT_SUCC);
				 notifyReconnectSuccessful();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * @param what
	 * @param obj
	 */
	private void sendHandlerMsg(int what, Object obj) {
		android.os.Message msg = serviceHandler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		serviceHandler.sendMessage(msg);
	}

	private void sendConnectionStatusAction(int status) {
		Intent conn = new Intent();
		conn.setAction(XmppAction.ACTION_CONNECTION_STATUS_CHANGED);
		conn.putExtra("status", status);
		LocalBroadcastManager localBroadcastManager=LocalBroadcastManager.getInstance(this);
		localBroadcastManager.sendBroadcast(conn);
		log("send broadcast status=="+status);
	}
	
	private void sendConflictAction(){
		Intent conn = new Intent();
		conn.setAction(XmppAction.ACTION_LOGIT_CONFLICT);
		sendBroadcast(conn);
		log("send broadcast conflict action");
	}

	/**
	 * 发送消息回执
	 * 
	 * @param receiptId
	 * @param fromjid
	 * @param tojid
	 */
	private void sendReceiptMessage(final String receiptId,
			final String fromjid, final String tojid) {
		new Thread() {
			public void run() {
				XmppReceiptMessage receipt = new XmppReceiptMessage();
				receipt.setFrom(fromjid);
				receipt.setTo(tojid);
				receipt.setRepId(receiptId);
				try {
					connection.sendPacket(receipt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	@Override
	public void connectionClosed() {
		// TODO Auto-generated method stub
		log("connectionClosed");
		if(loginThread!=null){
			loginThread.stoplogin();
			loginThread=null;
		}
		sendHandlerMsg(1, null);
	}

	@Override
	public void reconnectingIn() {
		// TODO Auto-generated method stub
		log("connectionReconnectingIn");
	}

	@Override
	public void reconnectionSuccessful() {
		// TODO Auto-generated method stub
		log("connectionReconnectSuccess");
		sendHandlerMsg(4, null);
	}

	@Override
	public void reconnectionFailed(Exception e) {
		// TODO Auto-generated method stub
		log("connectionReconnectFailed");
	}

	@Override
	public void connectionClosedOnNetworkError(Exception e) {
		// TODO Auto-generated method stub
		log("connectionClosedOnNetworkError");
		sendHandlerMsg(2, null);
	}

	@Override
	public void connectionClosedOnUserConflict(Exception e) {
		// TODO Auto-generated method stub
		log("connectionClosedOnUserConflict");
		sendHandlerMsg(3, null);
	}

	private static final int DELAY_LOGIN_TIME = 3000;

	@Override
	public void subscripted(Presence presence) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveSubscriptoinRequest(Presence packet) {
		// TODO Auto-generated method stub
		String msg = packet.getFrom();
		if (contacter.getUser(packet.getFrom()) == null) {
			ISubscriptionOption subscripted = new XmppSubscriptionOptionImpl();
			subscripted.acceptSubscription(packet.getFrom());
			ISubscriptionOption subscripte = new XmppSubscriptionOptionImpl();
			subscripte.addSubscription(packet.getFrom());
		} else {
			ISubscriptionOption subscripted = new XmppSubscriptionOptionImpl();
			subscripted.acceptSubscription(packet.getFrom());
		}
	}

	void log(String msg) {
		if (Connection.DEBUG_ENABLED) {
			android.util.Log.d(TAG, "" + msg);
		}
	}

}
