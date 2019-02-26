package com.ihengtu.xmpp.core.group;

import java.util.List;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;

import com.ihengtu.xmpp.core.manager.ConnectionManager;

/** 
* @ClassName: XmppGroupAdminOptionImpl 
* @Description: TODO 群主操作接口实现者
* @author hepengcheng
* @date 2015年3月3日 下午2:22:37 
*  
*/
public class XmppGroupAdminOptionImpl extends GroupOptionImpl implements IGroupAdminOption{

	@Override
	public void createGroup(XmppGroupIQ iq) {
		// TODO Auto-generated method stub
	   if(iq==null)
			return;
//       iq.setFrom(YmYApplication.user);
       iq.setRoomname(iq.getRoomname());
       iq.setAction(XmppGroupAction.ACTION_ADD);
       iq.setDescription(iq.getDescription());
       iq.setRoomsize(iq.getRoomsize());
//       iq.setPhoto(iq.getPhoto());
       //whether this packet has response from server
       PacketCollector collector;
		try {
//				collector = ConnectionManager.getConnection().createPacketCollector(
//					        new PacketIDFilter(iq.getPacketID()));
//				ConnectionManager.getConnection().sendPacket(iq);
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
	public void deleteGroup(XmppGroup group) {
		// TODO Auto-generated method stub
		if(group==null)
			return;
		XmppGroupIQ iq=new XmppGroupIQ();
//		iq.setFrom(YmYApplication.user);
		iq.setAction(XmppGroupAction.ACTION_DELETE);
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
	public void addGroupMember(XmppGroup group,ConferenceUser user) {
		// TODO Auto-generated method stub
         if(group==null||user==null)
        	 return;
//         XmppGroupIQ iq=new XmppGroupIQ(user);
         XmppGroupIQ iq=new XmppGroupIQ();
//         iq.setFrom(YmYApplication.user);
         iq.setAction(XmppGroupAction.ACTION_ADD);
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

	@Override
	public void removeGroupMember(XmppGroup group,ConferenceUser user) {
		// TODO Auto-generated method stub
        if(group==null||user==null)
        	return;
//        XmppGroupIQ iq=new XmppGroupIQ(user);
        XmppGroupIQ iq=new XmppGroupIQ();
//        iq.setFrom(YmYApplication.user);
        iq.setNumber(group.getNumber());
        iq.setRoomname(group.getName());
        iq.setAction(XmppGroupAction.ACTION_DELETE);
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
	public void updateGroup(XmppGroup group) {
		// TODO Auto-generated method stub
        if(group==null)
        	return;
        XmppGroupIQ iq=new XmppGroupIQ();
//        iq.setFrom(YmYApplication.user);
        iq.setAction(XmppGroupAction.ACTION_UPDATE);
        iq.setNumber(group.getNumber());
        iq.setRoomname(group.getName());
        iq.setDescription(group.getDescription());
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
	public void addGroupMemberList(XmppGroup group,List<ConferenceUser> users) {
		// TODO Auto-generated method stub
		if(group==null||users==null)
			return;
//		XmppGroupIQ iq=new XmppGroupIQ(users);
		XmppGroupIQ iq=new XmppGroupIQ();
//		iq.setFrom(YmYApplication.user);
		iq.setAction(XmppGroupAction.ACTION_ADD);
		iq.setNumber(group.getNumber());
	       //whether this packet has response from server
		       PacketCollector collector;
				try {
//						collector = ConnectionManager.getConnection().createPacketCollector(
//							        new PacketIDFilter(iq.getPacketID()));
//						ConnectionManager.getConnection().sendPacket(iq);
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
	public void removeGroupMemberList(XmppGroup group,List<ConferenceUser> users) {
		// TODO Auto-generated method stub
		 if(group==null||users==null)
	        	return;
	        XmppGroupIQ iq=new XmppGroupIQ();
//	        iq.setFrom(YmYApplication.user);
	        iq.setNumber(group.getNumber());
	        iq.setRoomname(group.getName());
	        iq.setAction(XmppGroupAction.ACTION_DELETE);
	        //whether this packet has response from server
		       PacketCollector collector;
				try {
//						collector = ConnectionManager.getConnection().createPacketCollector(
//							        new PacketIDFilter(iq.getPacketID()));
//						ConnectionManager.getConnection().sendPacket(iq);
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
