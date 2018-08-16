package netchat.netchatclient.view.chat;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;

import netchat.netchatclient.transfermsg.QPClient;
import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.view.friend.FriendFrame;
import netchat.netchatclient.view.friend.FriendPanel;

public class FriendChatFrame extends JFrame {
	private String chatUsername;
	private String chatAccount;
	private JTextArea taDiaplay;
	private JTextArea taInput;
	
	public FriendChatFrame(String username, String account) {
		System.out.println(username + ":" + account);
		this.chatUsername = username;
		this.chatAccount = account;
		
		setTitle("与 " + username + " 聊天");
		setBounds(200, 200, 600, 500);
		getContentPane().setLayout(null);
		
		JScrollPane spDisplay = new JScrollPane();
		spDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spDisplay.setBounds(10, 10, 560, 300);
		getContentPane().add(spDisplay);
		
		taDiaplay = new JTextArea();
		taDiaplay.setFont(new Font("Monospaced", Font.PLAIN, 15));
		taDiaplay.setEditable(false);
		taDiaplay.setLineWrap(true);
		taDiaplay.setWrapStyleWord(true);
		spDisplay.setViewportView(taDiaplay);
		
		JScrollPane spInput = new JScrollPane();
		spInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spInput.setBounds(10, 320, 560, 100);
		getContentPane().add(spInput);
		
		
		taInput = new JTextArea();
		taInput.setFont(new Font("Monospaced", Font.PLAIN, 15));
		taInput.setLineWrap(true);
		taInput.setWrapStyleWord(true);
		spInput.setViewportView(taInput);

		JButton btSend = new JButton("发送");
		btSend.setBounds(477, 430, 93, 23);
		getContentPane().add(btSend);
	
		//事件
		btSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//获取信息
				String msg = taInput.getText();
				
				//处理信息
				Map<String, Object> msgMap = new HashMap<>();
				msgMap.put("cmd", "personalChat");
				msgMap.put("fromAccount", ClientInfo.account);
				msgMap.put("fromUsername", ClientInfo.username);
				msgMap.put("toAccount", chatAccount);
				msgMap.put("msg", msg);
				String rep = QPClient.sendAndReceiveMsg(new Gson().toJson(msgMap));
				Map<String, Object> map = new Gson().fromJson(rep, Map.class);
				if ((boolean) map.get("flag")) {
					System.out.println("发送成功");
					List chatMsgList = FriendFrame.getInstance().getFriendMsgMap().get(chatAccount);
					Map<String, String> oneMsg = new HashMap<>();
					oneMsg.put("fromUsername", ClientInfo.username);
					oneMsg.put("fromAccount", ClientInfo.account);
					oneMsg.put("toUsername", chatUsername);
					oneMsg.put("toAccount", chatAccount);
					oneMsg.put("msg", msg);
					chatMsgList.add(oneMsg);
					displayMsg(ClientInfo.username, msg);
				}
				
				//重置输入框
				taInput.setText("");
			}
		});
		
		//刷新聊天信息
		Map friendMsgMap = FriendFrame.getInstance().getFriendMsgMap();
		List<Map<String, String>> chatMsgList = FriendFrame.getInstance().getFriendMsgMap().get(chatAccount);
		for (Map<String, String> oneMsg : chatMsgList) {
			displayMsg(oneMsg.get("fromUsername"), oneMsg.get("msg"));
		}
	}
	
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			FriendPanel friendPanel = FriendFrame.getInstance().getFriendMap().get(chatAccount);
			friendPanel.setFlag(false);
			FriendFrame.getInstance().getFriendFrameMap().put(chatAccount, null);
			this.dispose();
		}
	}
	
	public void displayMsg(String fromUsername, String msg) {
		taDiaplay.append(fromUsername + "  :\n");
		taDiaplay.append(msg + "\n");
		taDiaplay.append("\n");
	}

	public JTextArea getTaDiaplay() {
		return taDiaplay;
	}

	public void setTaDiaplay(JTextArea taDiaplay) {
		this.taDiaplay = taDiaplay;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(lookAndFeel);
		new FriendChatFrame("张三", "123456789").setVisible(true);
	}
}
