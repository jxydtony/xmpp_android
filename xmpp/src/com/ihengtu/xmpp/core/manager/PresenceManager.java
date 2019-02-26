package com.ihengtu.xmpp.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @ClassName: PresenceManager 
* @Description: TODO 监听好友上下线管理
* @author hepengcheng
* @date 2015年3月3日 下午2:57:00 
*  
*/
public class PresenceManager {
	
	private List<PresenceChangedLiestener> presencechangedListeners=null;
	
	private static PresenceManager instance;
	
	/**
	 * ��ŵ�ǰ������״̬���б�
	 * key=jid  eg:10061  not eg:10061@goopai.cn
	 * value: jid
	 * */
	private Map<String,String> presences=new HashMap<String, String>();
	
	/**
	 * �û������б�
	 * value=jid eg:10061@goopai.cn/android
	 * */
	private List<String> onlines=new ArrayList<String>();
	
	/**
	 * private consturtor
	 * */
	private PresenceManager(){}
	
	/**
	 * ��ȡ����ʵ��ģʽ
	 * @return SubscripterManager
	 */
	public static PresenceManager getInstance(){
		if(instance==null)
			instance=new PresenceManager();
		
		return instance;
	}
	
	public static void destroy(){
		instance=null;
	}
	
	/**
	 * ��Ӻ��������߼�����
	 * @param listener
	 */
	public void addPresenceChangedListener(PresenceChangedLiestener listener){
		if(listener==null)
			return;
		if(presencechangedListeners==null)
			presencechangedListeners=new ArrayList<PresenceChangedLiestener>();
		synchronized (presencechangedListeners) {
		    presencechangedListeners.add(listener);	
		}
	}
	
	/**
	 * �Ƴ���������߼�����
	 * @param listener
	 */
	public void removePresenceChagendLisetener(PresenceChangedLiestener listener){
		if(listener==null||presencechangedListeners==null)
			return;
		synchronized (presencechangedListeners) {
			if(presencechangedListeners.contains(listener))
				presencechangedListeners.remove(listener);
		}
	}
	
	/**
	 * @param user
	 */
	public void triggerPresenceChanged(String jid,boolean bool){
		PresenceChangedLiestener[] listeners=null;
		// Make a synchronized copy of the listenerJingles
		
		synchronized (presencechangedListeners) {
			listeners=new PresenceChangedLiestener[presencechangedListeners.size()];
			this.presencechangedListeners.toArray(listeners);
		}
		
		// ... and let them know of the event
		
		for(int i=0;i<listeners.length;i++){
			
			listeners[i].onPresenceChanged(jid,bool);
		}
	}
	
	/**
	 * ����µ����߻��������û�
	 * @param jid eg:10061@goopai.cn/android
	 */
	public void put(String jid){
		if(jid==null||!jid.contains("/")) return;
		synchronized (presences) {
			presences.put(jid.split("@")[0], jid);
		}
	}
	
	/**
	 * ��ȡָ���û�����Դ״̬
	 * ���һֱ�����ߣ���return NULL
	 * @param jid eg:10061
	 */
	public String get(String jid){
		if(jid==null) return null;
		synchronized (presences) {
			return presences.get(jid.split("@")[0]);
		}

	}
	
	/**
	 * �û���������û��������б�
	 * @param jid eg:10061@goopai.cn/android
	 */
	public void addToOnlineList(String jid){
		if(jid==null||!jid.contains("/")) return ;
		synchronized (onlines) {
			onlines.add(jid);
		}
	}
	
	/**
	 * �û����ߺ��Ƴ������б�
	 * @param jid eg:10061@goopai.cn/android
	 */
	public void removeFromOnlineList(String jid){
		if(jid==null||!jid.contains("/")) return ;
		
		synchronized (onlines) {
			if(onlines.contains(jid)){
				onlines.remove(jid);
			}
			
		}
	}
	
	/**
	 * �жϵ�ǰ�û��Ƿ�����
	 * @param jid
	 * @return
	 */
	public boolean ispresenced(String jid){
		if(jid==null||!jid.contains("/")) return false;
		synchronized (onlines) {
			return onlines.contains(jid);
		}
	}
	
	/**
	 * �����û���״̬�ı�
	 * ���߻�������״̬
	 * */
	public interface PresenceChangedLiestener {

		/**
		 * @param jid �û�ΨһJid eg:10061@goopai.cn/android
		 * @param bool ������״̬ true ���ߣ���֮����
		 * */
		void onPresenceChanged(String jid,boolean bool);
	}
	
}
