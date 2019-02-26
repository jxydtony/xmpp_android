package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ;

public class PingPacket extends IQ {

	 private String resource = null;
	 private String jid = null;
	   
		public PingPacket() {
	        setType(IQ.Type.GET);
	    }

	    public String getResource() {
	        return resource;
	    }

	    public void setResource(String resource) {
	        this.resource = resource;
	    }

	    public String getJid() {
	        return jid;
	    }

	    public void setJid(String jid) {
	        this.jid = jid;
	    }

	    
	@Override
	public String getChildElementXML() {
		// TODO Auto-generated method stub
		 StringBuilder buf = new StringBuilder();
	        buf.append("<ping xmlns=\"urn:xmpp:ping\"/>");
	        if (resource != null) {
	            buf.append("<resource>").append(resource).append("</resource>");
	        }
	        if (jid != null) {
	            buf.append("<jid>").append(jid).append("</jid>");
	        }
	     
	        return buf.toString();
	}

}
