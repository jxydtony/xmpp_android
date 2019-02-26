package com.ihengtu.xmpp.core.model;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.util.StringUtils;

/** 
* @ClassName: XmppReceiptMessage 
* @Description: TODO xmpp 消息回执累，继承自架包Message
* @author hepengcheng
* @date 2015年3月3日 下午2:58:10 
*  
*/
public class XmppReceiptMessage  extends Message{

	private String repId="0";
	
	public String getRepId() {
		return repId;
	}

	public void setRepId(String repId) {
		this.repId = repId;
	}

	@Override
	public String toXML() {
		// TODO Auto-generated method stub
		StringBuilder buf=new StringBuilder();
		buf.append("<message");
		if (getPacketID() != null) {
            buf.append(" id=\"").append(getPacketID()).append("\"");
        }
        if (getTo() != null) {
            buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
        }
        if (getFrom() != null) {
            buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
        }
        buf.append(">");
        //<received xmlns='urn:xmpp:receipts' id='richard2-4.1.247'/>
        buf.append("<received xmlns=\"urn:xmpp:receipts\" ");
        buf.append("id=\""+this.repId+"\"");
        buf.append("/>");
		buf.append("</message>");
		return buf.toString();
	}
}
