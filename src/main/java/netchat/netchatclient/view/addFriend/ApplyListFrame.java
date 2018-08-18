package netchat.netchatclient.view.addFriend;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;

import netchat.netchatclient.transfermsg.QPClient;
import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.view.friend.FriendFrame;

public class ApplyListFrame extends JFrame {
	private static ApplyListFrame applyListFrame;
	private JList ltApplyList;
	private DefaultListModel dlm = new DefaultListModel();

	private ApplyListFrame() {
		setBounds(200, 200, 300, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		JScrollPane spApplyList = new JScrollPane();
		spApplyList.setBounds(10, 10, 260, 300);
		getContentPane().add(spApplyList);

		ltApplyList = new JList();
		ltApplyList.setFont(new Font("宋体", Font.PLAIN, 15));
		ltApplyList.setModel(dlm);
		spApplyList.setViewportView(ltApplyList);

		JButton btAgree = new JButton("同意");
		btAgree.setBounds(156, 320, 93, 23);
		getContentPane().add(btAgree);

		JButton btDisAgree = new JButton("不同意");
		btDisAgree.setBounds(36, 320, 93, 23);
		getContentPane().add(btDisAgree);

		// 事件
		btDisAgree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ltApplyList.isSelectionEmpty()) {
					return;
				}

				String selectedItem = (String) dlm.getElementAt(ltApplyList.getSelectedIndex());
				String[] userInfo = selectedItem.split("#");
				Map<String, String> sendMsg = new HashMap<>();
				sendMsg.put("cmd", "disAgree");
				sendMsg.put("fromAccount", userInfo[0]);
				sendMsg.put("toAccount", ClientInfo.account);
				String rep = QPClient.sendAndReceiveMsg(new Gson().toJson(sendMsg));
				Map<String, Object> repMap = new Gson().fromJson(rep, Map.class);
				boolean flag = (boolean) repMap.get("flag");
				if (flag) {
					dlm.removeElementAt(ltApplyList.getSelectedIndex());
				}
			}
		});

		btAgree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ltApplyList.isSelectionEmpty()) {
					return;
				}

				String selectedItem = (String) dlm.getElementAt(ltApplyList.getSelectedIndex());
				String[] userInfo = selectedItem.split("#");
				Map<String, String> sendMsg = new HashMap<>();
				sendMsg.put("cmd", "agree");
				sendMsg.put("fromAccount", userInfo[0]);
				sendMsg.put("toAccount", ClientInfo.account);
				sendMsg.put("toUsername", ClientInfo.username);
				String rep = QPClient.sendAndReceiveMsg(new Gson().toJson(sendMsg));
				Map<String, Object> repMap = new Gson().fromJson(rep, Map.class);
				boolean flag = (boolean) repMap.get("flag");
				if (flag) {
					FriendFrame.getInstance().addFriend(userInfo[0], userInfo[1], "在线".equals(userInfo[2]) ? "1" : "0");
					dlm.removeElementAt(ltApplyList.getSelectedIndex());
				}
			}
		});
	}

	public void generateApplyList(List<Map<String, Object>> applyList) {
		dlm.clear();
		String online = null;
		for (Map<String, Object> tempMap : applyList) {
			if ("1".equals(tempMap.get("online"))) {
				online = "在线";
			} else {
				online = "不在线";
			}
			dlm.addElement(tempMap.get("account") + "#" + tempMap.get("username") + "#" + online);
		}
	}

	public static ApplyListFrame getInstance() {
		if (null == applyListFrame) {
			applyListFrame = new ApplyListFrame();
		}
		return applyListFrame;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(lookAndFeel);
		AddFriendFrame.getInstance().setVisible(true);
	}
}
