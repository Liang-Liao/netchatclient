package netchat.netchatclient.transfermsg;

import java.util.HashMap;
import java.util.Map;

import org.zeromq.ZMQ;

public class QPClient {
	public static ZMQ.Context context;
	public static ZMQ.Socket requester1;
	public static ZMQ.Socket requester2;

	// 本地交互
	public static ZMQ.Socket requester11;

	public static Map<String, ZMQ.Socket> requesterMap;

	static {
		context = ZMQ.context(1);
		// 连接服务器
		System.out.println("Connecting to 请求应答 server…");
		requester1 = context.socket(ZMQ.REQ);
		requester1.connect("tcp://127.0.0.1:5555");

		// 连接服务器
		System.out.println("Connecting to 请求应答 + 发布订阅 server…");
		requester2 = context.socket(ZMQ.REQ);
		requester2.connect("tcp://127.0.0.1:5556");

		// 本机端口注册
		System.out.println("Connecting to 本地端口 server…");
		requester11 = context.socket(ZMQ.REQ);
		requester11.connect("tcp://localhost:5565");

		// 聊天信息发送
		requesterMap = new HashMap<String, ZMQ.Socket>();
	}

	public static String sendAndReceiveMsg1(String msg) {
		requester1.send(msg.getBytes(), 0);
		return new String(requester1.recv(0));
	}
	
	public static String sendAndReceiveMsg2(String msg) {
		requester2.send(msg.getBytes(), 0);
		return new String(requester2.recv(0));
	}

	public static String sendAndReceiveMsg11(String msg) {
		requester11.send(msg.getBytes(), 0);
		return new String(requester11.recv(0));
	}

	// 给指定账户发送信息
	public static String sendAndReceiveMsgFriend(String account, String msg) {
		ZMQ.Socket requesterTemp = requesterMap.get(account);
		requesterTemp.send(msg.getBytes(), 0);
		return new String(requesterTemp.recv(0));
	}

	// 创建指定连接
	public static void createRequesterByAccount(String account, String ip, String port) {
		ZMQ.Socket requesterTemp = context.socket(ZMQ.REQ);
		requesterTemp.connect("tcp://" + ip + ":" + port);
		requesterMap.put("account", requesterTemp);
	}
}
