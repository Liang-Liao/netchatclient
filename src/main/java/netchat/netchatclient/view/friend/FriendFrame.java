package netchat.netchatclient.view.friend;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;

import netchat.netchatclient.transfermsg.QPClient;
import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.view.chat.FriendChatFrame;
import netchat.netchatclient.view.chat.GroupChatFrame;

public class FriendFrame extends JFrame {
	private static FriendFrame friendFrame;
	private JLabel lblUsername;
	private JLabel lblAccount;
	private JPanel groupPanel;
	private JPanel friendsPanel;
	private JScrollPane scrollPane;
	private Map<String, FriendPanel> friendMap;
	private Map<String, FriendChatFrame> friendFrameMap;
	private Map<String, List> friendMsgMap;
	private Map<String, GroupPanel> groupMap;
	private Map<String, GroupChatFrame> groupFrameMap;
	private Map<String, List> groupMsgMap;
	private List<Map> applyList;

	private FriendFrame() {
		setTitle("net chat");
		setBounds(600, 200, 250, 500);
		getContentPane().setLayout(null);

		JPanel personPanel = new JPanel();
		personPanel.setBackground(null);
		personPanel.setOpaque(false);
		personPanel.setBounds(10, 10, 215, 75);
		getContentPane().add(personPanel);
		personPanel.setLayout(null);

		lblUsername = new JLabel("Text");
		lblUsername.setFont(new Font("宋体", Font.PLAIN, 15));
		lblUsername.setBounds(115, 10, 72, 25);
		personPanel.add(lblUsername);

		JLabel lblUser = new JLabel("昵称:");
		lblUser.setFont(new Font("宋体", Font.PLAIN, 15));
		lblUser.setBounds(60, 10, 44, 25);
		personPanel.add(lblUser);

		JLabel lblAcc = new JLabel("账号:");
		lblAcc.setFont(new Font("宋体", Font.PLAIN, 15));
		lblAcc.setBounds(60, 45, 44, 25);
		personPanel.add(lblAcc);

		lblAccount = new JLabel("Text");
		lblAccount.setFont(new Font("宋体", Font.PLAIN, 15));
		lblAccount.setBounds(115, 45, 72, 25);
		personPanel.add(lblAccount);

		// 好友和群聊选项
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 110, 215, 315);
		getContentPane().add(scrollPane);

		// 好友聊天
		friendsPanel = new JPanel();
		scrollPane.setViewportView(friendsPanel);
		friendsPanel.setLayout(null);

		// 群聊
		groupPanel = new JPanel();
		groupPanel.setLayout(null);

		groupMap = new HashMap<>();
		groupMap.put("1", new GroupPanel("1"));
		groupMap.get("1").setBounds(0, 45 * 0, 215, 45);
		groupMap.get("1").addMouseListener(new MyGroupMouseAdapter(groupMap.get("1")));
		groupPanel.add(groupMap.get("1"));

		// 好友和群面板转换
		JButton btFriend = new JButton("好友");
		btFriend.setBounds(10, 85, 60, 23);
		getContentPane().add(btFriend);

		JButton btGroup = new JButton("群");
		btGroup.setBounds(80, 85, 60, 23);
		getContentPane().add(btGroup);

		// 申请好友
		JButton btAddFriend = new JButton("添加好友");
		btAddFriend.setBounds(30, 430, 80, 20);
		getContentPane().add(btAddFriend);

		JButton btApply = new JButton("申请列表");
		btApply.setBounds(120, 430, 80, 20);
		getContentPane().add(btApply);

		// 事件
		btFriend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.setViewportView(friendsPanel);
			}
		});

		btGroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scrollPane.setViewportView(groupPanel);
			}
		});
		
		//初始化变量
		friendMap = new HashMap<>();
		friendMsgMap = new HashMap<>();
		friendFrameMap = new HashMap<>();
		
		groupMsgMap = new HashMap<>();
		groupFrameMap = new HashMap<>();
		
		groupMsgMap.put("1", new ArrayList());
	}

	public void generateFriendList(List<Map<String, String>> friendList) {
		int i = 0;
		for (Map<String, String> friend : friendList) {
//			System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName() + ":" + friend.toString());
			friendMap.put(friend.get("account"),
					new FriendPanel(friend.get("username"), friend.get("account"), friend.get("online")));
			friendMap.get(friend.get("account")).setBounds(0, 45 * i, 215, 45);
			friendMap.get(friend.get("account"))
					.addMouseListener(new MyFriendMouseAdapter(friendMap.get(friend.get("account"))));
			friendsPanel.add(friendMap.get(friend.get("account")));
			i += 1;
		}
	}
	
	public void generateFriendMsgList(List<Map<String, String>> friendList) {
		int i = 0;
		for (Map<String, String> friend : friendList) {
			List temp = new ArrayList();
			friendMsgMap.put(friend.get("account"), temp);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 绘制背景图
		Toolkit tool = Toolkit.getDefaultToolkit();
		Image image = tool.getImage("resources/images/personalInfo.png");
		g.drawImage(image, 20, 60, 45, 45, this);
	}

	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cmd", "signout");
			map.put("account", ClientInfo.account);
			String sendMsg = new Gson().toJson(map);
			while (true) {
				String rep = QPClient.sendAndReceiveMsg(sendMsg);
				// 解析接收信息
				map = new Gson().fromJson(rep, Map.class);
				String flag = (String) map.get("flag");
				System.out.println(flag); // TODO
				if ("true".equals(flag)) {
					System.exit(0);
				}
			}
		}
	}

	public static FriendFrame getInstance() {
		if (null == friendFrame) {
			friendFrame = new FriendFrame();
		}
		return friendFrame;
	}

	public void changePersonalInfo() {
		lblUsername.setText(ClientInfo.username);
		lblAccount.setText(ClientInfo.account);
	} 

	public Map<String, FriendPanel> getFriendMap() {
		return friendMap;
	}

	public void setFriendMap(Map<String, FriendPanel> friendMap) {
		this.friendMap = friendMap;
	}

	public Map<String, List> getFriendMsgMap() {
		return friendMsgMap;
	}

	public void setFriendMsgMap(Map<String, List> friendMsgMap) {
		this.friendMsgMap = friendMsgMap;
	}

	public Map<String, FriendChatFrame> getFriendFrameMap() {
		return friendFrameMap;
	}

	public void setFriendFrameMap(Map<String, FriendChatFrame> friendFrameMap) {
		this.friendFrameMap = friendFrameMap;
	}

	public Map<String, GroupPanel> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, GroupPanel> groupMap) {
		this.groupMap = groupMap;
	}

	public Map<String, GroupChatFrame> getGroupFrameMap() {
		return groupFrameMap;
	}

	public void setGroupFrameMap(Map<String, GroupChatFrame> groupFrameMap) {
		this.groupFrameMap = groupFrameMap;
	}

	public Map<String, List> getGroupMsgMap() {
		return groupMsgMap;
	}

	public void setGroupMsgMap(Map<String, List> groupMsgMap) {
		this.groupMsgMap = groupMsgMap;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(lookAndFeel);
		FriendFrame.getInstance().setVisible(true);
	}
}
