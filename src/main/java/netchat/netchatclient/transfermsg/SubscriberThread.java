package netchat.netchatclient.transfermsg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.zeromq.ZMQ;

import com.google.gson.Gson;

import netchat.netchatclient.util.ClientInfo;
import netchat.netchatclient.view.chat.FriendChatFrame;
import netchat.netchatclient.view.chat.GroupChatFrame;
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
			System.out.println(msg);  //TODO
			switch (msg.get("msgType")) {
			case "friendOnline":
				friendOnline(msg);
				break;
			case "friendOutline":
				friendOutline(msg);
				break;

			case "personalChat":
				personalChat(msg);
				break;
			case "groupChat":
				groupChat(msg);
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

	private void personalChat(Map<String, String> msg) {
		Map friendMsgMap = FriendFrame.getInstance().getFriendMsgMap();
		List chatMsgList = FriendFrame.getInstance().getFriendMsgMap().get(msg.get("fromAccount"));
		Map<String, String> oneMsg = new HashMap<>();
		oneMsg.put("fromUsername", msg.get("fromUsername"));
		oneMsg.put("fromAccount", msg.get("fromAccount"));
		oneMsg.put("toUsername", msg.get("toUsername"));
		oneMsg.put("toAccount", msg.get("toAccount"));
		oneMsg.put("msg", msg.get("msg"));
		chatMsgList.add(oneMsg);

		FriendChatFrame friendChatFrame = FriendFrame.getInstance().getFriendFrameMap().get(msg.get("fromAccount"));
		if (null != friendChatFrame) {
			friendChatFrame.displayMsg(msg.get("fromUsername"), msg.get("msg"));
		}
	}
	
	private void groupChat(Map<String, String> msg) {
		Map groupMsgMap = FriendFrame.getInstance().getGroupMsgMap();
		List chatMsgList = FriendFrame.getInstance().getGroupMsgMap().get(msg.get("toGroup"));
		Map<String, String> oneMsg = new HashMap<>();
		oneMsg.put("fromUsername", msg.get("fromUsername"));
		oneMsg.put("fromAccount", msg.get("fromAccount"));
		oneMsg.put("toGroup", msg.get("toGroup"));
		oneMsg.put("msg", msg.get("msg"));
		chatMsgList.add(oneMsg);

		GroupChatFrame groupChatFrame = FriendFrame.getInstance().getGroupFrameMap().get(msg.get("toGroup"));
		if (null != groupChatFrame) {
			groupChatFrame.displayMsg(msg.get("fromUsername"), msg.get("msg"));
		}
		
	}
}
