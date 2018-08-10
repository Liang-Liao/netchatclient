package netchat.netchatclient.transfermsg;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.zeromq.ZMQ;

import com.google.gson.Gson;

import netchat.netchatclient.util.TestPort;

public class RegisterServer {
	public static Integer port = 10000;

	// 端口注册服务
	public static class chatThread1 implements Runnable {
		@Override
		public void run() {
			ZMQ.Context context = ZMQ.context(1);

			// Socket to talk to clients
			ZMQ.Socket responder = null;
			responder = context.socket(ZMQ.REP);
			responder.bind("tcp://localhost:5565");

			System.out.println("客户端注册服务器启动");
			while (!Thread.currentThread().isInterrupted()) {
				// 等待信息
				byte[] request = responder.recv(0);
				System.out.println("接受信息： " + new String(request));

				// 工作
				if (port > 11000) {
					port = 10000;
				}
				try {
					while (TestPort.isPortUsing("localhost", port)) {
						port++;
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				Map<String, String> map = new HashMap<String, String>();
				map.put("port", port.toString());
				port++;
				String reply = new Gson().toJson(map);
				// 发送回复信息
				responder.send(reply.getBytes(), 0);
			}
			responder.close();
			context.term();
		}
	}

	public static void main(String[] args) throws InterruptedException, UnknownHostException {
		if (!TestPort.isPortUsing("127.0.0.1", 5565)) {
			new Thread(new chatThread1()).start();
		}
	}

}
