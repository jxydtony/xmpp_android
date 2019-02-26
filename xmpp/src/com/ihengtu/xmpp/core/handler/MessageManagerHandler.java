package com.ihengtu.xmpp.core.handler;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.util.Log;

public class MessageManagerHandler {
	
	String tag = getClass().getSimpleName();
	
	private static MessageManagerHandler instance;
	
	private XmppMessageHandler handler;
	
	private List<XmppMessageReceiptListener> receipts=null;
	
	private MessageManagerHandler(Context context){
	}
	
	/**
	 * 
	 * @return single Instance
	 */
	public static MessageManagerHandler getInstance(Context context) {
		if (instance == null) 
			instance = new MessageManagerHandler(context);
		return instance;
	}
	
	public void setXmppMessageHandler(XmppMessageHandler handler){
		this.handler=handler;
	}
	
	public XmppMessageHandler getXmppMessageHandler(){
		return this.handler;
	}

	/**
	 * @param msg
	 */
	public synchronized void addMessage(Message msg) {
		if (msg == null) return;
		Log.d("msg", "manager handle msg...");
		if(handler!=null){
			Log.d("msg", "client handler handle msg...");
			handler.addXmppMessage(msg);
		}
	}
	
	/**
	 * @param messageid
	 */
	public synchronized void updateReceipt(String messageid,int status,boolean needtrigeed){
		if(messageid ==null) return ;
			boolean updated=false;
			if(handler!=null){
				updated=handler.updateReceipt(messageid, status, needtrigeed);
				Log.d("receipt", "handle receipt boolean:"+updated);
			}
			if(updated && needtrigeed){
				trigeedMessageReceiptReceived(messageid);
			}
	}
	
	/**
	 * @param listener
	 */
	public void addOnMessageReceiptListener(XmppMessageReceiptListener listener){
		if(listener==null) return;
		if(receipts==null){
			receipts=new ArrayList<XmppMessageReceiptListener>();
		}
		synchronized (receipts) {
			receipts.add(listener);
		}
	}
	
	/**
	 * @param listener
	 */
	public void removeOnMessageReceiptListener(XmppMessageReceiptListener listener){
		if(listener==null) return ;
		synchronized (receipts) {
			receipts.remove(listener);
		}
	}

	/**
	 * @param messageId
	 */
	private void trigeedMessageReceiptReceived(String messageId){
		if(receipts==null) return;
		XmppMessageReceiptListener[] listenerArray=null;
		 // Make a synchronized copy of the listenerJingles
		synchronized (receipts) {
               listenerArray=new XmppMessageReceiptListener[receipts.size()];
               this.receipts.toArray(listenerArray);
		}
		
		 // ... and let them know of the event
		for(int i=0;i<listenerArray.length;i++){
			listenerArray[i].onreceiptReceived(messageId);
		}
	}

}
