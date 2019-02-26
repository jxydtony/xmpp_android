package com.ihengtu.xmpp.core.manager;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import com.ihengtu.xmpp.core.XmppSubscriptedListener;
import com.ihengtu.xmpp.core.XmppSubscriptionRequestListener;
import com.ihengtu.xmpp.core.XmppUnSubscriptedListener;
import com.ihengtu.xmpp.core.manager.PresenceManager.PresenceChangedLiestener;


/** 
* @ClassName: SubscripterManager 
* @Description: TODO 好友请求管理
* @author hepengcheng
* @date 2015年3月3日 下午2:57:32 
*  
*/
public class SubscripterManager {

	private static SubscripterManager instance=null;
	
	private List<XmppSubscriptionRequestListener> subscriptRequestListeners=null;
	
	private List<XmppSubscriptedListener> subscriptedListeners=null;
	
	private List<XmppUnSubscriptedListener> unsubscriptedListeners=null;
	
	private List<PresenceChangedLiestener> presencechangedListeners=null;
	
	/**
	 * private consturtor
	 * */
	private SubscripterManager(){}
	
	public static SubscripterManager getInstance(){
		if(instance==null)
			instance=new SubscripterManager();
		
		return instance;
	}
	
	 
	public void addSubscripRequestListener(XmppSubscriptionRequestListener listener){
		
		if(listener!=null)
		{
			if(subscriptRequestListeners==null)
				initSubscriptRequestListener();
		
			synchronized (subscriptRequestListeners) {
			     		subscriptRequestListeners.add(listener);
			}
		}
	}
	
	public void removeSubscripRequestListener(XmppSubscriptionRequestListener listener){
		if(listener==null)
			return;
		synchronized (subscriptRequestListeners) {
				 subscriptRequestListeners.remove(listener);
		}
	}
	
	 public void addSubscriptedListener(XmppSubscriptedListener listener){
		 if(listener!=null)
		 {
			 if(subscriptedListeners==null)
				 subscriptedListeners=new ArrayList<XmppSubscriptedListener>();
			 
			 synchronized (subscriptedListeners) {
				subscriptedListeners.add(listener);
			}
		 }
	 }
	 
	 
	 /**
	 * @param listener
	 */
	public void removeSubscriptedListener(XmppSubscriptedListener listener){
		 if(listener==null)
			 return;
		  synchronized (subscriptedListeners) {
			 subscriptedListeners.remove(listener);
		}
	 }
	
	/**
	 * @param listener
	 */
	public void addUnSubscriptedListener(XmppUnSubscriptedListener listener){
		if(listener!=null)
		{
			if(unsubscriptedListeners==null)
				unsubscriptedListeners=new ArrayList<XmppUnSubscriptedListener>();
			
			synchronized (unsubscriptedListeners) {
			    unsubscriptedListeners.add(listener);
			}
		}
		
	}
	
	/**
	 * @param listener
	 */
	public void removeUnSubscriptedListener(XmppUnSubscriptedListener listener){
		if(listener==null)
			return;
		synchronized (unsubscriptedListeners) {
		    if(unsubscriptedListeners.contains(listener))
		    	unsubscriptedListeners.remove(listener);
		}
	}
	
	
	/**
	 * @param listener
	 */
	public void addPresenceChangedListener(PresenceChangedLiestener listener){
		if(listener!=null)
		{
			if(presencechangedListeners==null)
				presencechangedListeners=new ArrayList<PresenceChangedLiestener>();
			synchronized (presencechangedListeners) {
			    presencechangedListeners.add(listener);	
			}
		}
	}
	
	/**
	 * @param listener
	 */
	public void removePresenceChagendLisetener(PresenceChangedLiestener listener){
		if(listener==null)
			return;
		synchronized (presencechangedListeners) {
			if(presencechangedListeners.contains(listener))
				presencechangedListeners.remove(listener);
		}
	}
	
	/**
	 * @param packet
	 */
	private void triggerSubscripRequest(Presence packet){
		XmppSubscriptionRequestListener[] listeners=null;
		 // Make a synchronized copy of the listenerJingles
		synchronized (subscriptRequestListeners) {
                listeners=new XmppSubscriptionRequestListener[subscriptRequestListeners.size()];
                this.subscriptRequestListeners.toArray(listeners);
		}
		
		 // ... and let them know of the event
		for(int i=0;i<listeners.length;i++){
			listeners[i].onReceiveSubscriptoinRequest(packet);
		}
		
	}
	
	/**
	 * @param packet
	 */
	private void triggerSubscripted(Presence packet){
		XmppSubscriptedListener[] listeners=null;
		// Make a synchronized copy of the listenerJingles
		
		synchronized (subscriptedListeners) {
			listeners=new XmppSubscriptedListener[subscriptedListeners.size()];
			this.subscriptedListeners.toArray(listeners);
		}
		
		// ... and let them know of the event
		
		for(int i=0;i<listeners.length;i++){
			
			listeners[i].subscripted(packet);
		}
		
	}
	
	public void triggerUnSubscripted(String user){
		XmppUnSubscriptedListener[] listeners=null;
		// Make a synchronized copy of the listenerJingles
		
		synchronized (unsubscriptedListeners) {
			listeners=new XmppUnSubscriptedListener[unsubscriptedListeners.size()];
			this.unsubscriptedListeners.toArray(listeners);
		}
		
		// ... and let them know of the event
		
		for(int i=0;i<listeners.length;i++){
			
			listeners[i].unsubscripted(user);
		}
	}
	
	
//	/**
//	 * @param user
//	 */
//	public void triggerPresenceChanged(XmppUser user){
//		PresenceChangedLiestener[] listeners=null;
//		// Make a synchronized copy of the listenerJingles
//		
//		synchronized (presencechangedListeners) {
//			listeners=new PresenceChangedLiestener[presencechangedListeners.size()];
//			this.presencechangedListeners.toArray(listeners);
//		}
//		
//		// ... and let them know of the event
//		
//		for(int i=0;i<listeners.length;i++){
//			
//			listeners[i].onPresenceChanged(user);
//		}
//	}
	

	private void initSubscriptRequestListener(){
		
		PacketListener subscriptionPacketListener = new PacketListener() {
			public void processPacket(Packet packet) {
				Presence presence=(Presence) packet;
				
				if(presence.getType().equals(Presence.Type.subscribe))
				{
					triggerSubscripRequest(presence);

				}
				else if(presence.getType().equals(Presence.Type.subscribed)){
					triggerSubscripted(presence);
				}
				else if(presence.getType().equals(Presence.Type.unsubscribe)){
					
				}
					
			}
		};
		
		subscriptRequestListeners=new ArrayList<XmppSubscriptionRequestListener>();
		
		PacketFilter filter = new PacketFilter() {
			public boolean accept(Packet packet) {
				if (packet instanceof Presence) {
					Presence presence = (Presence) packet;
					if (presence.getType().equals(Presence.Type.subscribe)
							||presence.getType().equals(Presence.Type.subscribed)
							||presence.getType().equals(Presence.Type.unsubscribe)
					          ) {
						return true;
					}
				}
				return false;
			}
		};
		ContacterManager.setDefaultSubscriptionMode();
		ConnectionManager.getInstance().getConnection().addPacketListener(subscriptionPacketListener, filter);
//		try {
//			ConnectionManager.getConnection().addPacketListener(subscriptionPacketListener, filter);
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
}


