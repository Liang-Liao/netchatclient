package netchat.netchatclient.view.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;

import com.google.gson.Gson;

import netchat.netchatclient.transfermsg.QPClient;
import netchat.netchatclient.transfermsg.SubscriberThread;
import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.util.InputVerification;
import netchat.netchatclient.view.friend.FriendFrame;

/**
 * 登录面板
 * 
 * @author liang
 *
 */
public class LoginPanel extends JPanel {
	private static LoginPanel loginPanel;
	private Image loginBgPic;
	private JTextField tfAccount;
	private JPasswordField tfPassword;
	private JLabel lblMsg;

	private LoginPanel() {
		// 加载背景图片
		loginBgPic = new ImageIcon(new File("resources/images/login_bg.jpg").getAbsolutePath()).getImage();
		// 设置登录布局
		setBounds(0, 0, 400, 300);
		setLayout(new BorderLayout(0, 0));

		// 北面组件
		JPanel northPanel = new JPanel();
		cancelBackground(northPanel);
		FlowLayout flowLayout = (FlowLayout) northPanel.getLayout();
		flowLayout.setVgap(20);
		add(northPanel, BorderLayout.NORTH);

		JLabel lblLogin = new JLabel("登录");
		lblLogin.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLogin.setFont(new Font("宋体", Font.PLAIN, 30));
		northPanel.add(lblLogin);

		// 中间组件
		JPanel centerPanel = new JPanel();
		cancelBackground(centerPanel);
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(3, 1, 0, 0));
		// 账号面板
		JPanel accountPanel = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) accountPanel.getLayout();
		flowLayout_2.setVgap(10);
		cancelBackground(accountPanel);
		centerPanel.add(accountPanel);

		JLabel lblAccount = new JLabel("账号：");
		accountPanel.add(lblAccount);
		lblAccount.setFont(new Font("宋体", Font.PLAIN, 15));

		tfAccount = new JTextField();
		accountPanel.add(tfAccount);
		tfAccount.setColumns(30);

		// 密码面板
		JPanel passwordPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) passwordPanel.getLayout();
		flowLayout_3.setVgap(10);
		cancelBackground(passwordPanel);
		centerPanel.add(passwordPanel);

		JLabel lblPassword = new JLabel("密码：");
		lblPassword.setFont(new Font("宋体", Font.PLAIN, 15));
		passwordPanel.add(lblPassword);

		tfPassword = new JPasswordField();
		passwordPanel.add(tfPassword);
		tfPassword.setColumns(30);

		// 信息提示组件
		JPanel msgPanel = new JPanel();
		cancelBackground(msgPanel);
		FlowLayout flowLayout_4 = (FlowLayout) msgPanel.getLayout();
		flowLayout_4.setVgap(15);
		centerPanel.add(msgPanel);

		lblMsg = new JLabel("提示信息：");
		lblMsg.setForeground(Color.RED);
		lblMsg.setFont(new Font("宋体", Font.PLAIN, 15));
		msgPanel.add(lblMsg);

		// 南面组件
		JPanel southPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) southPanel.getLayout();
		flowLayout_1.setVgap(30);
		cancelBackground(southPanel);
		add(southPanel, BorderLayout.SOUTH);

		JButton btnRegister = new JButton("注册");
		btnRegister.setPreferredSize(new Dimension(100, 25));
		southPanel.add(btnRegister);

		JButton btnLogin = new JButton("登录");
		btnLogin.setPreferredSize(new Dimension(100, 25));
		southPanel.add(btnLogin);

		// 事件监听
		btnRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LoginFrame.getInstance().changePanel("register");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
				}
			}
		});

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String account = tfAccount.getText().trim();
				String password = tfPassword.getText().trim();

				if (InputVerification.accountVerfication(account) && InputVerification.passwordVerfication(password)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("cmd", "login");
					map.put("account", account);
					map.put("password", password);
					// 修改本地信息
					ClientInfo.account = account;
					// 发送服务器
					String sendMsg = new Gson().toJson(map);
					System.out.println(sendMsg); // TODO delete
					String rep = QPClient.sendAndReceiveMsg(sendMsg);
					System.out.println(rep); // TODO delete
					// 解析接收信息
					map = new Gson().fromJson(rep, Map.class);
					Boolean flag =  (Boolean) map.get("flag");
					System.out.println(flag); // TODO
					if (flag) {
						// 改变登录信息
						String username = (String) map.get("username");
						ClientInfo.username = username;
						System.out.println("username: " + username);
						List<Map<String, String>> friendList = (List<Map<String, String>>) map.get("friendList");
						System.out.println(friendList.toString());
						FriendFrame.getInstance().generateFriendList(friendList);
						FriendFrame.getInstance().generateFriendMsgList(friendList);
						FriendFrame.getInstance().changePersonalInfo();
						
						try {
							LoginFrame.getInstance().setVisible(false);
							FriendFrame.getInstance().setVisible(true);
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
								| UnsupportedLookAndFeelException e1) {
							e1.printStackTrace();
						}
						
						//开启订阅线程
						new Thread(new SubscriberThread()).start();
						
					} else {
						tfPassword.setText("");
						lblMsg.setText("提示：账号或密码错误");
					}
				} else {
					tfPassword.setText("");
					lblMsg.setText("提示：账号或密码为空");
				}
			}
		});
	}

	private void cancelBackground(JPanel centerPanel) {
		centerPanel.setBackground(null);
		centerPanel.setOpaque(false);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 绘制背景图
		g.drawImage(loginBgPic, 0, 0, this.getWidth(), this.getHeight(), this);
	}

	// 单例设计模式，懒加载
	public static LoginPanel getInstance() {
		if (null == loginPanel) {
			loginPanel = new LoginPanel();
		}
		return loginPanel;
	}
}
