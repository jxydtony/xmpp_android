package com.ihengtu.xmpp.core.group;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;

import android.util.Log;


/** 
* @ClassName: XmppGroupIQProvider 
* @Description: TODO 解析群消息IQ的Provider
* @author hepengcheng
* @date 2015年3月3日 下午2:23:57 
*  
*/
public class XmppGroupIQProvider implements IQProvider{

	/**
     * Creates a new provider. ProviderManager requires that every
     * PacketExtensionProvider has a public, no-argument constructor
     */
    public XmppGroupIQProvider() {
        super();
    }
   
	/**
     * Parse a iq/conferenceIQ element.
     */
	@Override
	public IQ parseIQ(XmlPullParser parser) throws Exception {
		// TODO Auto-generated method stub
		XmppGroupIQ iq=new XmppGroupIQ();
		boolean done=false;
		XmppGroup group=null;
		
//		//sub-elements provider
//		ConferencePhotoProvider photoProvider=new ConferencePhotoProvider();
//		ConferenceUserProvider userProvider=new ConferenceUserProvider();
		
		/**
		 * 获取解析事件
		 * START_DOCUMENT  开始解析
		 * START_TAG  开始元素
		 * TEXT  解析文本
		 * END_TAG  结束元素
		 * END_DOCUMENT 结束解析
		 * 
		 * */
		int eventType;
		String elementName;
		while(!done)
		{
		  eventType = parser.next(); 
	      elementName = parser.getName();
			
	      if(eventType==XmlPullParser.START_TAG)
	      {
				if(elementName.equals("item"))
				{
					group=new XmppGroup();
					group.setNumber(parser.getAttributeValue("", "number"));
					group.setName(parser.getAttributeValue("", "name"));
					group.setSize(Integer.valueOf(parser.getAttributeValue("", "size")));
					group.setDescription(parser.getAttributeValue("", "description"));
									 
				}
				//Get some attributes for the <Conference> element
				if(elementName.equals(XmppGroupIQ.getElementName()))
				{
					
					iq.setNumber(parser.getAttributeValue("", "number"));
					iq.setRoomname(parser.getAttributeValue("", "name"));
					String size=parser.getAttributeValue("", "size");
					if(size!=null)
						iq.setRoomsize(Integer.valueOf(size));
					iq.setDescription(parser.getAttributeValue("", "description"));
					String action=parser.getAttributeValue("", "action");
					
					//parser this action for Conference
					if(action!=null)
					{
						Log.d("get action",""+action);
						iq.setAction(XmppGroupAction.getAction(action));
					}
					
					iq.setStatus(parser.getAttributeValue("", "status"));
				}
//						else if(elementName.equals(ConferencePhoto.NODENAME))
//						{
//							photo=(ConferencePhoto) photoProvider.parseExtension(parser);
//							if(group==null)
//								iq.setPhoto(photo);
//							 
//							 else
//							 {
//								 String path=Constant.IM_GROUP_LOGO_PATH+group.getNumber()+".png";
//								 group.setPath(path);
//								 AsyncTaskImpl asyncTask=new AsyncTaskImpl();
//								 asyncTask.execute(path,photo);
//								
//							 }
//						}
//						else if(elementName.equals(ConferenceUser.NODENAME)){
//							user=(ConferenceUser) userProvider.parseExtension(parser);
//							iq.addConferenceUser(user);
//						}
	      }	
			else if(eventType==XmlPullParser.END_TAG)
			{
//				if(ConferencePhoto.NODENAME.equals(elementName))
//					photo=null;
//				if(ConferenceUser.NODENAME.equals(elementName))
//					user=null;
				if("item".equals(elementName))
				{
					iq.addGroup(group);
					group=null;
				}
				if(XmppGroupIQ.NODENAME.equals(elementName))
					done=true;	
			}
			 
		}
		return iq;
	}

	
	
}
