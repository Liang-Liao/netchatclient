package netchat.netchatclient.transfermsg;

import java.util.Map;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;

import com.google.gson.Gson;

import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.view.friend.FriendFrame;
import netchat.netchatclient.view.friend.FriendPanel;

public class SubscriberThread implements Runnable {

	@Override
	public void run() {
		ZMQ.Context context = ZMQ.context(1);

		// Socket to talk to server
		System.out.println("Collecting message from netchat server");
		ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
		subscriber.connect("tcp://127.0.0.1:5556");

		// Subscribe to zipcode, default is NYC, 10001
		String filter = ClientInfo.account;
		subscriber.subscribe(filter.getBytes());

		// Process 100 updates
		int update_nbr;
		long total_temp = 0;
		while (true) {
			// 获取信息
			String string = subscriber.recvStr(0).trim();
			StringTokenizer sscanf = new StringTokenizer(string, " ");
			String zipcode = sscanf.nextToken();
			String data = sscanf.nextToken();
			Map<String, String> msg = new Gson().fromJson(data, Map.class);
			switch (msg.get("msgType")) {
			case "friendOnline":
				friendOnline(msg);
				break;
			case "friendOutline":
				friendOutline(msg);
				break;
			}
		}
	}

	private void friendOnline(Map<String, String> msg) {
		String account = msg.get("onlineAccount");
		Map<String, FriendPanel> friendMap = FriendFrame.getInstance().getFriendMap();
		FriendPanel friend = friendMap.get(account);
		friend.changeOnlineStatus("1");
	}
	
	private void friendOutline(Map<String, String> msg) {
		String account = msg.get("outlineAccount");
		Map<String, FriendPanel> friendMap = FriendFrame.getInstance().getFriendMap();
		FriendPanel friend = friendMap.get(account);
		friend.changeOnlineStatus("0");
	}
}
