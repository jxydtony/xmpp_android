package com.ihengtu.xmpp.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import android.util.Log;

import com.ihengtu.xmpp.core.model.XmppUser;
import com.ihengtu.xmpp.core.model.XmppUser.UserStatus;


/** 
* @ClassName: ContacterManager 
* @Description: TODO 联系人管理器，管理花名册
* @author hepengcheng
* @date 2015年3月3日 下午2:55:29 
*  
*/
public class ContacterManager{

	public static final String TAG = "mContacterManaer";
	
	private static ContacterManager instance;

	public Map<String, XmppUser> contacters = new HashMap<String, XmppUser>();
    
	private List<XmppUser> xmppUsers = new ArrayList<XmppUser>();

	private ContacterManager(){}
	
	public static ContacterManager getInstance(){
		if(instance==null){
			instance=new ContacterManager();
		}
		return instance;
	}
	
	public static void destory(){
		instance=null;
	}
	
	public void init(XMPPConnection connection) {
		if(connection!=null&&connection.getRoster()!=null){
			for (RosterEntry entry : connection.getRoster().getEntries()) {
				contacters.put(entry.getUser(), transEntryToUser(entry, connection.getRoster()));
			}
			
			synchronized (contacters) {
				for (String key : contacters.keySet()) {
					xmppUsers.add(contacters.get(key));
				}
			}
		}
	}
	
	/**
	 * @return
	 */
	private List<XmppUser> getUserListFromMap() {
		if (contacters == null)
			throw new RuntimeException("contacters is null");

		if (xmppUsers != null)
			xmppUsers.clear();
		synchronized (contacters) {
			for (String key : contacters.keySet()) {
				xmppUsers.add(contacters.get(key));
			}
		}

		return xmppUsers;
	}

	public List<XmppUser> getContacterList() throws RuntimeException {
		getUserListFromMap();
		List<XmppUser> copy = new ArrayList<XmppUser>();
		synchronized (xmppUsers) {
			for (XmppUser user : xmppUsers) {
				copy.add(user.clone());
			}
		}

		return copy;
	}

	/**
	 * @param user
	 */
	public void updateContacterUser(XmppUser user) {
		if (user == null)
			return;
		synchronized (contacters) {
			for (String key : contacters.keySet()) {
				if (user.getJid() == key)
					contacters.put(key, user);
			}
		}
	}

	/**
	 * @return
	 */
	public List<XmppUser> getNoGroupUserList(Roster roster) {
		List<XmppUser> userList = new ArrayList<XmppUser>();
		for (RosterEntry entry : roster.getUnfiledEntries()) {
			userList.add(contacters.get(entry.getUser()).clone());
		}
		return userList;
	}

	/**
	 * @return
	 */
	public List<MRosterGroup> getGroups(Roster roster) {
		if (contacters == null)
			throw new RuntimeException("contacters is null");
		List<ContacterManager.MRosterGroup> groups = new ArrayList<ContacterManager.MRosterGroup>();
		for (RosterGroup group : roster.getGroups()) {
			List<XmppUser> groupUsers = new ArrayList<XmppUser>();
			for (RosterEntry entry : group.getEntries()) {
				groupUsers.add(contacters.get(entry.getUser()));
			}
			groups.add(new MRosterGroup(group.getName(), groupUsers));
		}
		return groups;
	}

	/**
	 * @param entry
	 * @return
	 */
	public XmppUser transEntryToUser(RosterEntry entry, Roster roster) {
		XmppUser user = new XmppUser();
		if (entry.getName() == null) {
			user.setUsername(entry.getUser());
			// user :iphone1@goopai.cn
			Log.d("user", entry.getUser());
		} else {
			user.setUsername(entry.getName());
			// entry.getName() the nickname e.g :heyu
			Log.d("user name", entry.getName());
		}
		user.setJid(entry.getUser());
		Log.d("user jid", entry.getUser());
		Presence presence = roster.getPresence(entry.getUser());

		if (presence.isAvailable())
			user.setStatus(UserStatus.ONLINE);
		else
			user.setStatus(UserStatus.OFFLINE);

		for (RosterGroup group : entry.getGroups()) {
			if (group.contains(entry))
				user.setGroupname(group.getName());
		}
		return user;
	}

	/**
	 * @param user
	 */
	public synchronized void addUser(XmppUser user) {
		if (user == null)
			return;
		synchronized (contacters) {
			contacters.put(user.getJid(), user);
		}
	}

	/**
	 * @param the
	 *            jid of the jabber user
	 */
	public synchronized XmppUser getUser(String JID) {
		if (JID == null)
			return null;
		synchronized (contacters) {
			for (String key : contacters.keySet()) {
				if (JID.equals(key))
					return contacters.get(key);
			}
		}

		return null;
	}

	/**
	 * @param user
	 * @param nickname
	 */
	public void setNickname(XmppUser user, String nickname, XMPPConnection connection) {
		RosterEntry entry = connection.getRoster().getEntry(user.getJid());
		entry.setName(nickname);
	}

	/**
	 * @param user
	 * @param groupName
	 */
	public void addUserToGroup(final XmppUser user, final String groupName, final XMPPConnection connection) {
		if (groupName == null || user == null)
			return;
		new Thread() {
			@Override
			public void run() {
				RosterGroup group = connection.getRoster().getGroup(groupName);
				RosterEntry entry = connection.getRoster().getEntry(user.getJid());
				try {
					if (group != null) {
						if (entry != null)
							group.addEntry(entry);
					} else {
						RosterGroup newGroup = connection.getRoster().createGroup(groupName);
						if (entry != null)
							newGroup.addEntry(entry);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	/**
	 * @param user
	 * @param groupName
	 */
	public void removeUserFromGroup(final XmppUser user, final String groupName, final XMPPConnection connection) {
		if (groupName == null || user == null)
			return;
		new Thread() {
			@Override
			public void run() {
				RosterGroup group = connection.getRoster().getGroup(groupName);
				if (group != null) {
					try {
						RosterEntry entry = connection.getRoster().getEntry(user.getJid());
						if (entry != null)
							group.removeEntry(entry);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public static void setDefaultSubscriptionMode() {
		Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual);
		// Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.accept_all);
	}

	public static class MRosterGroup {
		private String name;
		private List<XmppUser> users;

		public MRosterGroup(String name, List<XmppUser> users) {
			this.name = name;
			this.users = users;
		}

		public int getCount() {
			if (users != null)
				return users.size();
			return 0;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<XmppUser> getUsers() {
			return users;
		}

		public void setUsers(List<XmppUser> users) {
			this.users = users;
		}

	}

}
