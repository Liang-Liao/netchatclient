package netchat.netchatclient.view.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class RegisterPanel extends JPanel {
	private static RegisterPanel registerPanel;
	private Image loginBgPic;
	private JTextField tfName;
	private JTextField tfPassword;
	private JTextField textField;

	private RegisterPanel() {
		// 加载背景图片
		loginBgPic = new ImageIcon(new File("resources/images/login_bg.jpg").getAbsolutePath()).getImage();
		//设置登录布局
		setBounds(0, 0, 400, 300);
		setLayout(new BorderLayout(0, 0));
		
		//北面组件
		JPanel northPanel = new JPanel();
		cancelBackground(northPanel);
		FlowLayout flowLayout = (FlowLayout) northPanel.getLayout();
		flowLayout.setVgap(20);
		add(northPanel, BorderLayout.NORTH);
		
		JLabel lblRegister = new JLabel("注册");
		lblRegister.setFont(new Font("宋体", Font.PLAIN, 30));
		northPanel.add(lblRegister);
		
		//中间组件
		JPanel centerPanel = new JPanel();
		cancelBackground(centerPanel);
		add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(4, 1, 0, 0));
		//呢称面板
		JPanel namePanel = new JPanel();
		FlowLayout fl_namePanel = (FlowLayout) namePanel.getLayout();
		cancelBackground(namePanel);
		centerPanel.add(namePanel);
		
		JLabel lblName = new JLabel("昵称：");
		namePanel.add(lblName);
		lblName.setFont(new Font("宋体", Font.PLAIN, 15));
		
		tfName = new JTextField();
		namePanel.add(tfName);
		tfName.setColumns(30);
		
		//密码面板
		JPanel passwordPanel = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) passwordPanel.getLayout();
		cancelBackground(passwordPanel);
		centerPanel.add(passwordPanel);
		
		JLabel lblPassword = new JLabel("密码：");
		lblPassword.setFont(new Font("宋体", Font.PLAIN, 15));
		passwordPanel.add(lblPassword);
		
		tfPassword = new JTextField();
		passwordPanel.add(tfPassword);
		tfPassword.setColumns(30);
		
		//确认密码
		JPanel rePasswordPanel = new JPanel();
		cancelBackground(rePasswordPanel);
		centerPanel.add(rePasswordPanel);
		
		JLabel label = new JLabel("确认密码：");
		label.setFont(new Font("宋体", Font.PLAIN, 15));
		rePasswordPanel.add(label);
		
		textField = new JTextField();
		rePasswordPanel.add(textField);
		textField.setColumns(30);
		
		JLabel lblTemp = new JLabel("    ");
		rePasswordPanel.add(lblTemp);
		
		JLabel lblAccount = new JLabel("提示信息：");
		lblAccount.setForeground(Color.RED);
		lblAccount.setFont(new Font("宋体", Font.PLAIN, 15));
		lblAccount.setHorizontalAlignment(SwingConstants.CENTER);
		centerPanel.add(lblAccount);
		
		//南面组件
		JPanel southPanel = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) southPanel.getLayout();
		flowLayout_1.setVgap(20);
		cancelBackground(southPanel);
		add(southPanel, BorderLayout.SOUTH);
		
		JButton btnBack = new JButton("返回");
		btnBack.setPreferredSize(new Dimension(100, 25));
		southPanel.add(btnBack);
		
		JButton btnLogin = new JButton("确认");
		btnLogin.setPreferredSize(new Dimension(100, 25));
		southPanel.add(btnLogin);
		
		//事件监听
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LoginFrame.getInstance().changePanel("login");
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
						| UnsupportedLookAndFeelException e1) {
					e1.printStackTrace();
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
		//绘制背景图
		g.drawImage(loginBgPic, 0, 0, this.getWidth(), this.getHeight(), this);
	}
	
	//单例设计模式，懒加载
	public static RegisterPanel getInstance() {
		if (null == registerPanel) {
			registerPanel = new RegisterPanel();
		}
		return registerPanel;
	}
}
