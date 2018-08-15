package netchat.netchatclient.view.friend;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyGroupMouseAdapter extends MouseAdapter {
	private GroupPanel groupPanel;
	public MyGroupMouseAdapter(GroupPanel groupPanel) {
		this.groupPanel = groupPanel;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			System.out.println(groupPanel.getGroupId());
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