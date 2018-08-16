package netchat.netchatclient.view.friend;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import netchat.netchatclient.view.chat.FriendChatFrame;

public class MyFriendMouseAdapter extends MouseAdapter {
	private FriendPanel friendPanel;

	public MyFriendMouseAdapter(FriendPanel friendPanel) {
		this.friendPanel = friendPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if ("1".equals(friendPanel.getOnline())) {
				if (!friendPanel.getFlag()) {
					FriendChatFrame friendChatFrame = new FriendChatFrame(friendPanel.getUsername(), friendPanel.getAccount());
					friendChatFrame.setVisible(true);
					friendPanel.setFlag(true);
					FriendFrame.getInstance().getFriendFrameMap().put(friendPanel.getAccount(), friendChatFrame);
				}
			}else {
				System.out.println("该用户不在线");
			}	
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		friendPanel.setBackground(Color.YELLOW);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		friendPanel.setBackground(Color.WHITE);
		super.mouseMoved(e);
	}
}
