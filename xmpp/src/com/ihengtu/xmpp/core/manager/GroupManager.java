package com.ihengtu.xmpp.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.util.Log;

import com.ihengtu.xmpp.core.group.XmppGroup;
import com.ihengtu.xmpp.core.group.XmppGroupAction;
import com.ihengtu.xmpp.core.group.XmppGroupIQ;
import com.ihengtu.xmpp.core.group.XmppOnGroupAdminOptionListener;
import com.ihengtu.xmpp.core.group.XmppOnGroupClientEventListener;

/** 
* @ClassName: GroupManager 
* @Description: TODO 群组管理器
* @author hepengcheng
* @date 2015年3月3日 下午2:55:44 
*  
*/
public class GroupManager {
	
    private static GroupManager instance =null;
    
    private  List<XmppOnGroupAdminOptionListener> adminEventListeners=null;
    
    private  List<XmppOnGroupClientEventListener> clientEventListeners=null;
    
    public static Map<String,XmppGroup> maps=null;
    
    private List<XmppGroup> grouplist=new ArrayList<XmppGroup>();
    
    boolean Initialized=false;
   
    private String requestPacketId;
    
    private String lock="lock";
    
    /**
	 * private consturtor
	 * */
    private GroupManager(){
    	maps=new HashMap<String, XmppGroup>();
    	//first request server for XmppGroup List
    	reload();
    	init();
    	 // Listen for any XmppGroupIQ packets.
        PacketFilter ImGroupFilter = new PacketTypeFilter(XmppGroupIQ.class);
        PacketFilter ImGroupClientFilter=new PacketTypeFilter(Message.class);
        try {
//			ConnectionManager.getConnection().addPacketListener(new ConferencePacketListener(), ImGroupFilter);
//			ConnectionManager.getConnection().addPacketListener(new ImGroupQueryListener(), ImGroupFilter);
//			ConnectionManager.getConnection().addPacketListener(new ImGroupClientMsgListener(), ImGroupClientFilter);
			ConnectionManager.getInstance().getConnection().addPacketListener(new ConferencePacketListener(), ImGroupFilter);
			ConnectionManager.getInstance().getConnection().addPacketListener(new ImGroupQueryListener(), ImGroupFilter);
			ConnectionManager.getInstance().getConnection().addPacketListener(new ImGroupClientMsgListener(), ImGroupClientFilter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /**
     * get single instance for GroupManager
     * */  
    public static GroupManager getInstance(){
    	if(instance==null)
    		instance=new GroupManager();
    	return instance;
    }
    
    public void init(){
    	if (!Initialized) 
    	{
            try {
                synchronized (lock) { 
                	Log.d("get group", "get group load start");
                    long waitTime = SmackConfiguration.getPacketReplyTimeout();
                    long start = System.currentTimeMillis();
                    while (!Initialized) {
                        if (waitTime <= 0) {
                            break;
                        }
                        lock.wait(waitTime);
                        long now = System.currentTimeMillis();
                        waitTime -= now - start;
                        start = now;
                    }
                }
            }
            catch (InterruptedException ie) {
                // Ignore.
            }
    	}
    	
    }
    
    public  boolean isInitalized(){
    	/*synchronized () {
			
		}*/
    	return false;
    }
    
    public void reload(){
    	XmppGroupIQ iq=new XmppGroupIQ();
    	requestPacketId=iq.getPacketID();
    	iq.setIsLogin(true);
//    	iq.setFrom(YmYApplication.user);
    	PacketFilter idFilter = new PacketIDFilter(requestPacketId);
    	try {
//    		ConnectionManager.getConnection().addPacketListener(new ImGroupResultListener(),idFilter);
//			ConnectionManager.getConnection().sendPacket(iq);
    		ConnectionManager.getInstance().getConnection().addPacketListener(new ImGroupResultListener(),idFilter);
			ConnectionManager.getInstance().getConnection().sendPacket(iq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<XmppGroup> getImGroupList(){
    	if(maps==null)
    	    throw new RuntimeException("get group list is null");
    	//clear this data list
    	if(grouplist!=null)
    		grouplist.clear();
    	 List<XmppGroup> copy=null;
	    	synchronized (maps) {
	    		for(String key:maps.keySet())
	        	{
	        		XmppGroup group=maps.get(key);
		        		synchronized (grouplist) {
		    			    grouplist.add(group);	
		    			}
	        	}
			}
    	synchronized (grouplist) {
		  copy=new ArrayList<XmppGroup>(grouplist);
		}
    	return copy; 
    }   
    
    /** 
     * @param groupNumber Ⱥ���ID
     * @return
     */
    public static XmppGroup getGroup(String groupNumber){
    	if(groupNumber==null)
    		return null;
    	//when group map is null throw RuntimeException
    	if(maps==null)
    		throw new RuntimeException("get group is null");
    	XmppGroup group=null;
    	
    	synchronized (maps) {
		   	for(String key:maps.keySet()){
		   		if(key.equals(groupNumber))
		   			group=maps.get(key);
		   	}
		}
    	
    	return group;
    }
    
    public void addXmppGroupAdminEventListener(XmppOnGroupAdminOptionListener listener){
		if(listener==null)
			return;
		if(adminEventListeners==null)
	    	adminEventListeners=new ArrayList<XmppOnGroupAdminOptionListener>();
		synchronized (adminEventListeners) {
			adminEventListeners.add(listener);
		}
    	
    }
    
    public void removeXmppGroupAdminEventListener(XmppOnGroupAdminOptionListener listener){
    	if(listener==null)
    		return;
    	synchronized (adminEventListeners) {
			if(adminEventListeners.contains(listener))
				adminEventListeners.remove(listener);
		}
    }
    
    public void addXmppGroupClientEventListener(XmppOnGroupClientEventListener listener){
    	if(listener==null)
    		return;
    	 if(clientEventListeners==null)
			   clientEventListeners=new ArrayList<XmppOnGroupClientEventListener>();
    	 
    	synchronized (clientEventListeners) {  
		   clientEventListeners.add(listener);
		}
    	
    }
    
    public void removeXmppGroupClientEventListener(XmppOnGroupClientEventListener listener){
    	  if(listener==null)
    		  return;
    	  
    	  synchronized (clientEventListeners) {
		    if(clientEventListeners.contains(listener))
		    	clientEventListeners.remove(listener);
		}
    }
    
    public void triggerGroupCreated(XmppGroup group){
    	XmppOnGroupAdminOptionListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (adminEventListeners) {
		  listeners=new XmppOnGroupAdminOptionListener[adminEventListeners.size()];
		   adminEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onGroupCreated(group);
    			}
    }
    
    public void triggerGroupDeleted(XmppGroup group){
    	XmppOnGroupAdminOptionListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (adminEventListeners) {
		  listeners=new XmppOnGroupAdminOptionListener[adminEventListeners.size()];
		   adminEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onGroupDeleted(group);
    			}
    }
    
    public void triggerGroupMemberAdded(XmppGroup group){
    	XmppOnGroupAdminOptionListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (adminEventListeners) {
		  listeners=new XmppOnGroupAdminOptionListener[adminEventListeners.size()];
		   adminEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onGroupMemberAdded(group);
    			}
    }
    
    public void triggerGroupMemberRemoved(XmppGroup group){
    	XmppOnGroupAdminOptionListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (adminEventListeners) {
		  listeners=new XmppOnGroupAdminOptionListener[adminEventListeners.size()];
		   adminEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onGroupMemberRemoved(group);
    			}
    }
    
    public void triggerGroupUpdated(XmppGroup group){
    	XmppOnGroupAdminOptionListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (adminEventListeners) {
		  listeners=new XmppOnGroupAdminOptionListener[adminEventListeners.size()];
		   adminEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onGroupInfoUpdated(group);
    			}
    }
    
    public void triggerMemberJoined(String groupNumber){
    	
    	XmppOnGroupClientEventListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (clientEventListeners) {
		  listeners= new XmppOnGroupClientEventListener[clientEventListeners.size()];
		   clientEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onJoinGroup(groupNumber);
    			}
    }
    
    public void triggerMemberDropouted(String groupNumber){
    	XmppOnGroupClientEventListener[] listeners=null;
    	// Make a synchronized copy of the listenerJingles
    	synchronized (clientEventListeners) {
		  listeners= new XmppOnGroupClientEventListener[clientEventListeners.size()];
		   clientEventListeners.toArray(listeners);
		}
    	
    	// ... and let them know of the event
    			for(int i=0;i<listeners.length;i++){
    				listeners[i].onDropoutGroup(groupNumber);
    			}
    }
    
	/**
	 * @author Administrator
	 */
	private class ConferencePacketListener implements PacketListener{

			@Override
			public void processPacket(Packet packet) {
				// TODO Auto-generated method stub
				XmppGroupIQ iq=(XmppGroupIQ) packet;
				XmppGroupAction action=iq.getAction();
				 if(action!=null)
				 {
					 if(XmppGroupAction.ACTION_ADD==action)
					 {
						 XmppGroup group=transEntryToGroup(iq);
						 synchronized (maps) {
						    if(maps.containsKey(group.getNumber()))
						    {
						    	maps.remove(group.getNumber());
						    	maps.put(group.getNumber(), group);
						    }
						}
						 triggerGroupMemberAdded(group);
					 }
					 
					 if(XmppGroupAction.ACTION_REMOVE==action)
					 {
						 XmppGroup group=transEntryToGroup(iq);
						 synchronized (maps) {
							    if(maps.containsKey(group.getNumber()))
							    {
							    	maps.remove(group.getNumber());
							    	maps.put(group.getNumber(), group);
							    }
							}
						 triggerGroupMemberRemoved(group);
					 }
					 if(XmppGroupAction.ACTION_CREATE==action)
					 {
						 XmppGroup group=transEntryToGroup(iq);
						 synchronized (maps) {
						     maps.put(group.getNumber(), group);	
						}
						 triggerGroupCreated(group);
					 }
					 if(XmppGroupAction.ACTION_DELETE==action)
					 {
						 XmppGroup group=transEntryToGroup(iq);
						 synchronized (maps) {
						   if(group==null)
							   return;
						   if(maps.containsKey(group.getNumber()))
							   maps.remove(group.getNumber());
						}
						 triggerGroupDeleted(group);
					 }
					 
					 if(XmppGroupAction.ACTION_UPDATE==action)
					 {
						 XmppGroup group=transEntryToGroup(iq);
						 synchronized (maps) {
							    if(maps.containsKey(group.getNumber()))
							    {
							    	maps.remove(group.getNumber());
							    	maps.put(group.getNumber(), group);
							    }
							}
						 triggerGroupUpdated(group);
					 }
					 
				 }
				 //notify all thread as initialized
				 synchronized (lock) {
				    Initialized=true;
				    lock.notifyAll();
				}
			}
			
		}
		
    private class ImGroupResultListener implements PacketListener{
		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			if(packet instanceof IQ)
			{
				if(packet.getPacketID().equals(requestPacketId))
				{
						XmppGroupIQ iq=(XmppGroupIQ) packet;
						if(iq.getType().equals(IQ.Type.RESULT))
						{
							synchronized (maps) {
							   if(maps!=null&&maps.size()>0)
								   maps.clear();
									   for(XmppGroup group:iq.getImGroupList())
									   {
										   maps.put(group.getNumber(), group);
									   }
							}
							synchronized (lock) {
			                    Initialized = true;
			                    lock.notifyAll();
			                }
						}
						ConnectionManager.getInstance().getConnection().removePacketListener(this);
//						try {
//							ConnectionManager.getConnection().removePacketListener(this);
//						} catch (XMPPException e) {    
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
				}
					
			}
		}
    	
    }
    
    /**
     * @author Administrator
     */
    private class ImGroupQueryListener implements PacketListener{

		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			
			if(packet instanceof IQ)
			{
				XmppGroupIQ iq=(XmppGroupIQ) packet;
				XmppGroupAction action=iq.getAction();
				//return this Action as Query
				if(XmppGroupAction.ACTION_QUERY==action)
				{
					XmppGroup group=transEntryToGroup(iq);
//			        XmppFileHelper.createFolderOnSDcard(XmppAction.IM_USER_LOGO_PATH);
			        /*synchronized (group) {
			        	new AsyncTaskThread(group).start();
			        	try {
							group.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}*/
					
					synchronized (maps) {
					   if(maps.containsKey(iq.getNumber()))
					   {
						   maps.put(iq.getNumber(), group);
					   }
					}
				}
			}
		}
    	
    }
    
    /**
     * @author Administrator
     */
    private class ImGroupClientMsgListener implements PacketListener{

		@Override
		public void processPacket(Packet packet) {
			// TODO Auto-generated method stub
			if(packet instanceof Message)
			{
				Message msg=(Message) packet;
				if(Message.Type.conferenceWarn==msg.getType())
				{
					String groupNumber=msg.getFrom().substring(0,msg.getFrom().indexOf("@"));
					synchronized (maps) {
					   if(maps.containsKey(groupNumber))
						   maps.remove(groupNumber);
					}
					triggerMemberDropouted(groupNumber);
				}
			}
		}
    	
    }
    
    public XmppGroup transEntryToGroup(XmppGroupIQ packet){
    	XmppGroup group=null;
    	if(packet==null||packet.getNumber()==null)
    		return null;
    	group=new XmppGroup();
    	if(packet.getNumber()!=null)
    		group.setNumber(packet.getNumber());
    	if(packet.getRoomname()!=null)
    		group.setName(packet.getRoomname());
    	if(packet.getRoomsize()>0)
    		group.setSize(packet.getRoomsize());
    	if(packet.getDescription()!=null)
    		group.setDescription(packet.getDescription());
    	if(packet.getTime()!=null)
    		group.setCreatetime(packet.getTime());
    	return group;
    }
    
   /* public static void transPhotoToLocalpath(String file,ConferencePhoto photo){
    	
        XmppFileHelper.createFolderOnSDcard(XmppAction.IM_GROUP_LOGO_PATH);
        File save=new File(file);
        try {
				save.createNewFile();
		    } catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
		   }
    	Bitmap bitmap=XmppImageHelper.StringToBitmap(photo.getBinval());
    	try {
			    FileOutputStream fos=new FileOutputStream(save);
			    bitmap.compress(CompressFormat.PNG, 100, fos);
		    }
	    	catch(NullPointerException exception){
	    		exception.printStackTrace();
	    		
	    	}
	    	catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			   }
    	
    }*/
    
//    /**
//     * @param path
//     * @return ConferencePhoto ����ConferencePhoto����
//     */
//    public static ConferencePhoto transLocalpathToPhoto(String path){
//    	ConferencePhoto photo=null;
//    	Bitmap bitmap=null;
//    	String binval=null;
//    	if(path==null)
//    		return null;
//    	bitmap=BitmapFactory.decodeFile(path);
//    	if(bitmap!=null)
//    	{
//    		binval=ImageHelper.BitmapToString(bitmap);
//    		photo=new ConferencePhoto();
//    		photo.setBinval(binval);
//    	}
//    	
//    	return photo;
//    }
    
    
    /*private class AsyncTaskThread extends Thread{

	//�첽�߳�Ҫ������Ⱥ�����
    private XmppGroup group=null;
    
    // exit this thread
//    private boolean isRunning=true;
    
     public AsyncTaskThread(XmppGroup object){
        	this.group=object;
      }
     
	  @Override
	  public void run() {
		// TODO Auto-generated method stub
		super.run();
		if (NetworkStatus.getAPNType(YmYApplication.getYmYApplicationContext()) > 0) {
		    //ѭ����ȡÿ��ȺԱ�ĸ�����Ϣ 
			for(final ConferenceUser user:group.getConferenceUsers()){
		    		 loadingUserInfo(user);
				}
		     }
		  synchronized (group) {
		    group.notifyAll();
		}
		}
		
//		private void loadingUserInfo(ConferenceUser user){
//			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//			String jid=user.getUserJid();
//			params.add(new BasicNameValuePair("mid", jid.substring(0, jid.indexOf("@"))));
//			String str = HttpUtil.PostSubmitForm(MyURL.getFriend,params);
//			Entity entity=JsonReader.parserJSONToEntity(str);
//			user.setSex(0);
//			String logoURL=entity.getLogoUrl();
//			String path=XmppAction.IM_USER_LOGO_PATH+jid.substring(0, jid.indexOf("@"))+".png";
//			user.setLogo(path);
//			File file=new File(path);
//			if(!file.exists())
//			   doloadPic(logoURL, path);
//		}
    	
		*//**
		 * �����̼�ͷ��
		 * @param url
		 * @param path
		 *//*
		private void doloadPic(final String url, final String path) {
			Log.d("download picture", "downlaod picture...");
			if (!StringFromatUtil.isEmpty(url)
					&& NetworkStatus.getAPNType(YmYApplication.getYmYApplicationContext()) > 0) {
						try {
							InputStream is = (new URL(url).openStream());
							Bitmap temp_bitmap = BitmapFactory.decodeStream(is);
							File file = new File(path);
							file.createNewFile();
							FileOutputStream fos = new FileOutputStream(file);
							temp_bitmap.compress(Bitmap.CompressFormat.PNG, 50,
									fos);
							fos.close();
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

			}
		}
		
    }  // AsyncTaskThread end
*/   
}
