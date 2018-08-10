package netchat.netchatclient.view.chat;

import javax.swing.JFrame;

public class NetChatFrame extends JFrame{
	private static NetChatFrame netChatFrame;
	
	private NetChatFrame() {
		setBounds(200, 100, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(NetChatPanel.getInstance());
	}
	
	//单例设计模式
	public static NetChatFrame getInstance() {
		if (null == netChatFrame) {
			netChatFrame = new NetChatFrame();
		}
		return netChatFrame;
	}
}
