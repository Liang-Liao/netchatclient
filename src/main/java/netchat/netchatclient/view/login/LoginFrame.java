package netchat.netchatclient.view.login;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class LoginFrame extends JFrame {
	private static LoginFrame loginFrame;

	private LoginFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		setBounds(400, 300, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		add(LoginPanel.getInstance(), BorderLayout.CENTER);
		setVisible(true);
	}

	public void changePanel(String cmd) {
		switch (cmd) {
		case "login":
			loginFrame.remove(RegisterPanel.getInstance());
			loginFrame.add(LoginPanel.getInstance());
			loginFrame.revalidate();
			loginFrame.repaint();
			break;
		case "register":
			loginFrame.remove(LoginPanel.getInstance());
			loginFrame.add(RegisterPanel.getInstance());
			loginFrame.revalidate();
			loginFrame.repaint();
			break;
		}
	}

	// 单例设计模式，懒加载
	public static LoginFrame getInstance() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		if (null == loginFrame) {
			loginFrame = new LoginFrame();
		}
		return loginFrame;
	}
}
