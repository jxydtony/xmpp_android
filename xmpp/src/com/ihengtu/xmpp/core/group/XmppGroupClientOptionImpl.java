package com.ihengtu.xmpp.core.group;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;

import com.ihengtu.xmpp.core.manager.ConnectionManager;

/** 
* @ClassName: XmppGroupClientOptionImpl 
* @Description: TODO 群组普通用户操作接口实现者
* @author hepengcheng
* @date 2015年3月3日 下午2:22:53 
*  
*/
public class XmppGroupClientOptionImpl extends GroupOptionImpl implements IGroupClientOption{

	@Override
	public void joinGroup(XmppGroup group) {
		// TODO Auto-generated method stub
		if(group==null)
			return;
		XmppGroupIQ iq=new XmppGroupIQ();
//		iq.setFrom(YmYApplication.user);
		iq.setAction(XmppGroupAction.ACTION_JOIN);
		iq.setNumber(group.getNumber());
		iq.setRoomname(group.getName());
		//whether this packet has response from server
	       PacketCollector collector;
			try {
//					collector = ConnectionManager.getConnection().createPacketCollector(
//						        new PacketIDFilter(iq.getPacketID()));
//					ConnectionManager.getConnection().sendPacket(iq);
					collector = ConnectionManager.getInstance().getConnection().createPacketCollector(
						        new PacketIDFilter(iq.getPacketID()));
					ConnectionManager.getInstance().getConnection().sendPacket(iq);
			    	IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
			    	collector.cancel();
			    	if (response == null) {
			    	    throw new XMPPException("No response from the server.");
			    	}
			    	// If the server replied with an error, throw an exception.
			    	else if (response.getType() == IQ.Type.ERROR) {
			    	    throw new XMPPException(response.getError());
			    	}
		    	
			   } catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
	}

	@Override
	public void dropoutGroup(XmppGroup group) {
		// TODO Auto-generated method stub
		if(group==null)
			return;
		XmppGroupIQ iq=new XmppGroupIQ();
//		iq.setFrom(YmYApplication.user);
		iq.setAction(XmppGroupAction.ACTION_QUIT);
		iq.setNumber(group.getNumber());
		 //whether this packet has response from server
	       PacketCollector collector;
			try {
//					collector = ConnectionManager.getConnection().createPacketCollector(
//						        new PacketIDFilter(iq.getPacketID()));
//					ConnectionManager.getConnection().sendPacket(iq);
					collector = ConnectionManager.getInstance().getConnection().createPacketCollector(
						        new PacketIDFilter(iq.getPacketID()));
					ConnectionManager.getInstance().getConnection().sendPacket(iq);
			    	IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
			    	collector.cancel();
			    	if (response == null) {
			    	    throw new XMPPException("No response from the server.");
			    	}
			    	// If the server replied with an error, throw an exception.
			    	else if (response.getType() == IQ.Type.ERROR) {
			    	    throw new XMPPException(response.getError());
			    	}
		    	
			   } catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
	}

	

}
