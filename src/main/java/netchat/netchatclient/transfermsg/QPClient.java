package netchat.netchatclient.transfermsg;

import java.util.HashMap;
import java.util.Map;

import org.zeromq.ZMQ;

public class QPClient {
	public static ZMQ.Context context;
	public static ZMQ.Socket requester;

	static {
		context = ZMQ.context(1);
		// 连接服务器
		System.out.println("Connecting to 请求应答 server…");
		requester = context.socket(ZMQ.REQ);
		requester.connect("tcp://127.0.0.1:5555");
	}

	public static String sendAndReceiveMsg(String msg) {
		requester.send(msg.getBytes(), 0);
		return new String(requester.recv(0));
	}
}
