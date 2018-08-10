package netchat.netchatclient.view;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import netchat.netchatclient.view.login.LoginPanel;
import netchat.netchatclient.view.login.RegisterPanel;

public class PanelTest {
	static String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
	static JFrame jFrame = null;

	public static void before() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(lookAndFeel);
		jFrame = new JFrame();
		jFrame.setBounds(400, 300, 400, 300);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void after() {
		jFrame.setVisible(true);
	}

	public static void loginPanelTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		jFrame.getContentPane().add(LoginPanel.getInstance());
	}

	public static void registerPanelTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		jFrame.getContentPane().add(RegisterPanel.getInstance());
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		before();

		loginPanelTest();
//		registerPanelTest();

		after();
	}
}
