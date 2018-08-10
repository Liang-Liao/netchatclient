package netchat.netchatclient.view.chat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.File;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import netchat.netchatclient.util.ClientInfo;

public class NetChatPanel extends JPanel {
	private static NetChatPanel netChatPanel;
	
	private JTree friendTree;
	

	class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer{
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			ImageIcon friendNodeImg = new ImageIcon(new File("resources/images/friendNode.jpg").getAbsolutePath());
			friendNodeImg = new ImageIcon(friendNodeImg.getImage().getScaledInstance(10, 10, Image.SCALE_DEFAULT));
			ImageIcon presonalNodeImg = new ImageIcon(new File("resources/images/personalInfo.png").getAbsolutePath());
			presonalNodeImg = new ImageIcon(presonalNodeImg.getImage().getScaledInstance(10, 10, Image.SCALE_DEFAULT));
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;  
			if (node.isLeaf()) {
				String text = node.toString();
				switch(text) {
				case "个人信息":
					this.setIcon(presonalNodeImg);
					break;
				default:
					this.setIcon(friendNodeImg);
				}
			}else {
				this.setIcon(null);
			}
			return this;
		}
		
	}
	private NetChatPanel() {
		setBounds(0, 0, 800, 600);
		setLayout(new BorderLayout(0, 0));
		// 西面面板
		JPanel westPanel = new JPanel();
		westPanel.setLayout(new BorderLayout(0, 0));
		westPanel.setSize(100, this.getHeight());
		add(westPanel, BorderLayout.WEST);

		friendTree = new JTree();

		DefaultMutableTreeNode personalInfo = new DefaultMutableTreeNode("个人信息");// 创建根节点
		DefaultMutableTreeNode pubChatRoom = new DefaultMutableTreeNode("公共聊天室");
		DefaultMutableTreeNode room1 = new DefaultMutableTreeNode("聊天室1");
		pubChatRoom.add(room1);

		DefaultMutableTreeNode privFriend = new DefaultMutableTreeNode("我的好友");

		DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
		root.add(personalInfo);
		root.add(pubChatRoom);
		root.add(privFriend);

		friendTree.setModel(new DefaultTreeModel(root));
		friendTree.setCellRenderer(new MyDefaultTreeCellRenderer());
		friendTree.setRootVisible(false);

		JScrollPane scrollPane = new JScrollPane(friendTree);
		scrollPane.setPreferredSize(new Dimension(150, this.getHeight()));
		westPanel.add(scrollPane);
		
		//聊天界面
		JPanel chatPanel = new JPanel();
		add(chatPanel, BorderLayout.CENTER);
		chatPanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea taChat = new JTextArea();
		taChat.setBorder(BorderFactory.createEtchedBorder());
		taChat.setEditable(false);
		chatPanel.add(taChat, BorderLayout.CENTER);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(BorderFactory.createEtchedBorder());
		chatPanel.add(inputPanel, BorderLayout.SOUTH);
		inputPanel.setLayout(new BorderLayout(0, 0));
		
		JTextArea taInput = new JTextArea();
		taInput.setPreferredSize(new Dimension(inputPanel.getWidth(), 150));
		inputPanel.add(taInput);
		
		JPanel btPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) btPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		inputPanel.add(btPanel, BorderLayout.SOUTH);
		
		JButton btnSend = new JButton("发送");
		btPanel.add(btnSend);
	}
	/**
	 * 修改节点信息
	 */
	public void changeLoginInfo() {
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) friendTree.getModel().getRoot();
		Enumeration e = root.breadthFirstEnumeration();
		while (e.hasMoreElements()) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
			if (node.toString().matches("个人信息")) {
				node.setUserObject("个人信息:" + ClientInfo.username);
				break;
			}
		}
	}

	public static NetChatPanel getInstance() {
		if (null == netChatPanel) {
			netChatPanel = new NetChatPanel();
		}
		return netChatPanel;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		UIManager.setLookAndFeel(lookAndFeel);
		JFrame jFrame = new JFrame();
		jFrame.setBounds(200, 100, 800, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.getContentPane().add(NetChatPanel.getInstance());
		jFrame.setVisible(true);
	}
}
