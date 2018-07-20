package netchat.netchatclient;

import org.zeromq.ZMQ;

/**
 * Hello world!
 *  客户端实验类
 */
public class Client {
	public static void main(String[] args) {
		ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to hello world server…");

        ZMQ.Socket requester = context.socket(ZMQ.REQ);
        requester.connect("tcp://127.0.0.1:5555");

        for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
            String request = "Hello";
            System.out.println("Sending Hello " + requestNbr);
            requester.send(request.getBytes(), 0);

            byte[] reply = requester.recv(0);
            System.out.println("Received " + new String(reply) + " " + requestNbr);
        }
        requester.close();
        context.term();
		
		System.out.println("我的实验类");
		System.out.println("Hello World!");
	}
}
