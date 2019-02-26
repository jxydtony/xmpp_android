package com.ihengtu.xmpp.core;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import com.ihengtu.xmpp.core.manager.ConnectionManager;

/** 
* @ClassName: XmppSubscriptionOptionImpl 
* @Description: TODO 好友添加，删除具体实现者
* @author hepengcheng
* @date 2015年3月3日 下午2:14:13 
*  
*/
public class XmppSubscriptionOptionImpl implements ISubscriptionOption{

	@Override
	public void addSubscription(String JID) {
		// TODO Auto-generated method stub
		
		if(JID==null ||"".equals(JID))
			return;
		Presence subscription = new Presence(Presence.Type.subscribe);
		if(!JID.contains("@"))
		subscription.setTo(JID+"@goopai.cn");
		else
			subscription.setTo(JID);
			ConnectionManager.getInstance().getConnection().sendPacket(subscription);
//		try {
//			ConnectionManager.getConnection().sendPacket(subscription);
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@Override
	public void removeSubscription(String JID) {
		// TODO Auto-generated method stub 
		
		if(JID==null||"".equals(JID))
			return;
		if(JID.contains("/"))
			JID=JID.substring(0, JID.indexOf("/"));
		/**ɾ�����*/
		try {
//			Roster roster=ConnectionManager.getConnection().getRoster();
			Roster roster=ConnectionManager.getInstance().getConnection().getRoster();
			RosterEntry entry=null;
			entry=roster.getEntry(JID);
			roster.removeEntry(entry);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		Presence deletescription=new Presence(Presence.Type.unsubscribe);
//		deletescription.setTo(JID);
//		try {
//			ConnSessionManager.getConnection().sendPacket(deletescription);
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	@Override
	public void acceptSubscription(String JID) {
		// TODO Auto-generated method stub
		Presence subscribed=new Presence(Presence.Type.subscribed);
		subscribed.setTo(JID);
		try {
//			ConnectionManager.getConnection().sendPacket(subscribed);
			ConnectionManager.getInstance().getConnection().sendPacket(subscribed);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	@Override
	public void rejuectSubscription(String JID) {
		// TODO Auto-generated method stub
		Presence unsubscribe=new Presence(Presence.Type.unsubscribe);
		unsubscribe.setTo(JID);
		try {
//			ConnectionManager.getConnection().sendPacket(unsubscribe);
			ConnectionManager.getInstance().getConnection().sendPacket(unsubscribe);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void querySubscription(String JID) {
		// TODO Auto-generated method stub
		
	}

}
