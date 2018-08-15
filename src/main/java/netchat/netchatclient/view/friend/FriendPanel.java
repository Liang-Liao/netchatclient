package netchat.netchatclient.view.friend;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class FriendPanel extends JPanel {
	private String username, account, online;
	private JLabel lblOnline;

	public FriendPanel(String username, String account, String online) {
		this.username = username;
		this.account = account;
		this.online = online;
		
		setLayout(null);
		setBackground(Color.WHITE);
		setBounds(0, 0, 215, 45);

		JLabel lblUsername = new JLabel(username);
		lblUsername.setBounds(55, 0, 100, 20);
		add(lblUsername);

		JLabel lblAccount = new JLabel(account);
		lblAccount.setBounds(55, 25, 100, 20);
		add(lblAccount);
		
		lblOnline = new JLabel("");
		lblOnline.setBounds(151, 28, 54, 15);
		add(lblOnline);
		
		changeOnlineStatus(online);
	}
	
	public void changeOnlineStatus(String online) {
		this.online = online;
		if ("1".equals(online)) {
			lblOnline.setText("在线");
		}else {
			lblOnline.setText("");
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// 绘制背景图
		Toolkit tool = Toolkit.getDefaultToolkit();
		Image image = tool.getImage("resources/images/friendNode.jpg");
		g.drawImage(image, 0, 0, 45, 45, this);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}
}
