package com.ihengtu.xmpp.core.group;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;

import com.ihengtu.xmpp.core.manager.ConnectionManager;
import com.ihengtu.xmpp.core.manager.GroupManager;

/** 
* @ClassName: GroupOptionImpl 
* @Description: TODO 群组操作实现
* @author hepengcheng
* @date 2015年3月3日 下午2:19:17 
*  
*/
public class GroupOptionImpl implements IGroupOption{

	@Override
	public XmppGroup getGroupInfoFromName(String groupName) {
		// TODO Auto-generated method stub
		if(groupName==null)
			return null;
		XmppGroupIQ result=null;
		XmppGroupIQ send=new XmppGroupIQ();
		send.setAction(XmppGroupAction.ACTION_UPDATE);
		send.setRoomname(groupName);
		//whether this packet has response from server
	       PacketCollector collector;
			try {
//					collector = ConnectionManager.getConnection().createPacketCollector(
//						        new PacketIDFilter(send.getPacketID()));
//					ConnectionManager.getConnection().sendPacket(send);
					collector = ConnectionManager.getInstance().getConnection().createPacketCollector(
						        new PacketIDFilter(send.getPacketID()));
					ConnectionManager.getInstance().getConnection().sendPacket(send);
			    	IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
			    	collector.cancel();
			    	if (response == null) {
			    	    throw new XMPPException("No response from the server.");
			    	}
			    	// If the server replied with an error, throw an exception.
			    	else if (response.getType() == IQ.Type.ERROR) {
			    	    throw new XMPPException(response.getError());
			    	}
			    	// trans IQ to XmppGroupIQ
			    	if(response!=null&&response.getType()==IQ.Type.RESULT)
			    		result=(XmppGroupIQ) response;
			    	
			   } catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
			
		return GroupManager.getInstance().transEntryToGroup(result);
	}

	@Override
	public XmppGroup getGroupInfoFromNumber(String groupNumber) {
		// TODO Auto-generated method stub
		if(groupNumber==null)
			return null;
		XmppGroupIQ result=null;
		XmppGroupIQ send=new XmppGroupIQ();
		send.setAction(XmppGroupAction.ACTION_UPDATE);
		send.setNumber(groupNumber);
		//whether this packet has response from server
	       PacketCollector collector;
			try {
//					collector = ConnectionManager.getConnection().createPacketCollector(
//						        new PacketIDFilter(send.getPacketID()));
//					ConnectionManager.getConnection().sendPacket(send);
					collector = ConnectionManager.getInstance().getConnection().createPacketCollector(
						        new PacketIDFilter(send.getPacketID()));
					ConnectionManager.getInstance().getConnection().sendPacket(send);
			    	IQ response = (IQ) collector.nextResult(SmackConfiguration.getPacketReplyTimeout());
			    	collector.cancel();
			    	if (response == null) {
			    	    throw new XMPPException("No response from the server.");
			    	}
			    	// If the server replied with an error, throw an exception.
			    	else if (response.getType() == IQ.Type.ERROR) {
			    	    throw new XMPPException(response.getError());
			    	}
			    	// trans IQ to XmppGroupIQ
			    	if(response!=null&&response.getType()==IQ.Type.RESULT)
			    		result=(XmppGroupIQ) response;
			    	
			   } catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
			
		return GroupManager.getInstance().transEntryToGroup(result);
	}

	@Override
	public XmppGroup getGroupMembers(String groupNumber) {
		// TODO Auto-generated method stub
		if(groupNumber==null)
			return null;
		XmppGroupIQ result =null;
		XmppGroupIQ iq=new XmppGroupIQ();
		iq.setAction(XmppGroupAction.ACTION_QUERY);
		iq.setNumber(groupNumber);
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
			    	// trans IQ to XmppGroupIQ
			    	if(response!=null&&response.getType()==IQ.Type.RESULT)
			    		result=(XmppGroupIQ) response;
			    	
			   } catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			  }
			
		return GroupManager.getInstance().transEntryToGroup(result);
	}

}
