package netchat.netchatclient.view.friend;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFriendMouseAdapter extends MouseAdapter {
	private FriendPanel friendPanel;
	public MyFriendMouseAdapter(FriendPanel friendPanel) {
		this.friendPanel = friendPanel;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			System.out.println(friendPanel.getAccount());
		}
		super.mouseClicked(e);
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
