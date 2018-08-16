package netchat.netchatclient.view.friend;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import netchat.netchatclient.view.chat.FriendChatFrame;
import netchat.netchatclient.view.chat.GroupChatFrame;

public class MyGroupMouseAdapter extends MouseAdapter {
	private GroupPanel groupPanel;

	public MyGroupMouseAdapter(GroupPanel groupPanel) {
		this.groupPanel = groupPanel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (!groupPanel.isFlag()) {
				GroupChatFrame groupChatFrame = new GroupChatFrame(groupPanel.getGroupId());
				groupChatFrame.setVisible(true);
				groupPanel.setFlag(true);
				FriendFrame.getInstance().getGroupFrameMap().put(groupPanel.getGroupId(), groupChatFrame);
			}
		}
		super.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		groupPanel.setBackground(Color.YELLOW);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		groupPanel.setBackground(Color.WHITE);
		super.mouseMoved(e);
	}
}