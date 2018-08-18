package netchat.netchatclient.view.addFriend;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;

import netchat.netchatclient.transfermsg.QPClient;
import netchat.netchatclient.util.ClientInfo;

public class AddFriendFrame extends JFrame {
	private static AddFriendFrame addFriendFrame;
	private JTextField tfSearchAccount;
	private JList ltUser;
	private DefaultListModel dlm = new DefaultListModel();
	private JLabel lblPrompt;

	private AddFriendFrame() {
		setBounds(200, 200, 300, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		tfSearchAccount = new JTextField();
		tfSearchAccount.setToolTipText("");
		tfSearchAccount.setFont(new Font("宋体", Font.PLAIN, 15));
		tfSearchAccount.setBounds(10, 20, 200, 20);
		getContentPane().add(tfSearchAccount);
		tfSearchAccount.setColumns(20);

		JButton btnSearch = new JButton("查找");
		btnSearch.setBounds(215, 20, 60, 20);
		getContentPane().add(btnSearch);

		JScrollPane spAimUser = new JScrollPane();
		spAimUser.setBounds(10, 50, 260, 250);
		getContentPane().add(spAimUser);

		ltUser = new JList();
		ltUser.setFont(new Font("宋体", Font.PLAIN, 15));
		ltUser.setModel(dlm);
		spAimUser.setViewportView(ltUser);

		JButton btConfirm = new JButton("确认添加");
		btConfirm.setBounds(85, 310, 90, 20);
		getContentPane().add(btConfirm);

		lblPrompt = new JLabel("");
		lblPrompt.setForeground(Color.RED);
		lblPrompt.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrompt.setBounds(10, 340, 260, 15);
		getContentPane().add(lblPrompt);

		// 事件
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String account = tfSearchAccount.getText().trim();

				if (null != account || !"".equals(account)) {
					Map<String, String> sendMsg = new HashMap<>();
					sendMsg.put("cmd", "searchUsers");
					sendMsg.put("account", account);
					String rep = QPClient.sendAndReceiveMsg(new Gson().toJson(sendMsg));
					Map<String, Object> repMap = new Gson().fromJson(rep, Map.class);
					List<Map<String, Object>> usersList = (List<Map<String, Object>>) repMap.get("usersList");
					System.out.println(usersList);
					String online = null;
					dlm.clear();
					for (Map<String, Object> tempMap : usersList) {
						if (1.0 ==  (double) tempMap.get("online")) {
							online = "在线";
						} else {
							online = "不在线";
						}
						dlm.addElement(tempMap.get("account") + "#" + tempMap.get("username") + "#" + online);
					}
				}
			}
		});
		
		btConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ltUser.isSelectionEmpty()) {
					return ;
				}
				
				String selectedItem = (String) dlm.getElementAt(ltUser.getSelectedIndex());
				String[] userInfo = selectedItem.split("#");
				if (ClientInfo.account.equals(userInfo[0])) {
					lblPrompt.setText("提示：不可添加用户自身");
					return ;
				}
				
				Map<String, String> sendMsg = new HashMap<>();
				sendMsg.put("cmd", "addFriend");
				sendMsg.put("fromAccount", ClientInfo.account);
				sendMsg.put("fromUsername", ClientInfo.username);
				sendMsg.put("toAccount", userInfo[0]);
				
				String rep = QPClient.sendAndReceiveMsg(new Gson().toJson(sendMsg));
				Map<String, Object> repMap = new Gson().fromJson(rep, Map.class);
				boolean flag = (boolean) repMap.get("flag");
				if (flag) {
					lblPrompt.setText("提示：发送申请成功");
				}else {
					String promptMsg = (String) repMap.get("promptMsg");
					switch(promptMsg) {
					case "alreadySendApply":
						lblPrompt.setText("提示：已发送申请，不可重复发送");
						break;
					case "alreadyFriend":
						lblPrompt.setText("提示：你们已经是好友关系");
						break;
					default:
						lblPrompt.setText("提示：发送失败");
					}
				}
			}
		});
	}

	public static AddFriendFrame getInstance() {
		if (null == addFriendFrame) {
			addFriendFrame = new AddFriendFrame();
		}
		return addFriendFrame;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(lookAndFeel);
		AddFriendFrame.getInstance().setVisible(true);
	}
}
