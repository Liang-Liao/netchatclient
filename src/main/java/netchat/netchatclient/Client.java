package netchat.netchatclient;

import java.util.Scanner;
import java.util.UUID;

import org.zeromq.ZMQ;

/**
 * Hello world! 客户端实验类
 */
public class Client {
//	public 
	
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);

		//连接服务器 
		System.out.println("Connecting to server…");
		ZMQ.Socket requester = context.socket(ZMQ.REQ);
		requester.connect("tcp://127.0.0.1:5555");

		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		System.out.println("该客户端的uuid号为：" + uuid);
		
		String request = null;
		Scanner sc = new Scanner(System.in);
		byte[] reply = null;
		//发送信息，并接收服务器的放回信息
		while (true) {
			System.out.println("请输入发送字符串：");
			request = sc.nextLine();
			//如果输入字符串  exit  ，则表示退出连接
			if ("exit".equals(request)) {
				break;
			}
			requester.send((uuid + ":" + request).getBytes(), 0);
			reply = requester.recv(0);
			System.out.println("Received " + new String(reply));
		}

//		for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
//			System.out.println("Sending Hello " + requestNbr);
//			requester.send(request.getBytes(), 0);
//
//			byte[] reply = requester.recv(0);
//			System.out.println("Received " + new String(reply) + " " + requestNbr);
//		}
	}
}
