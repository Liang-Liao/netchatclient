package netchat.netchatclient.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestPort {
	/*** 
     * 测试主机Host的port端口是否被使用
     * @param host 
     * @param port 
     * @throws UnknownHostException  
     */  
    public static boolean isPortUsing(String host,int port) throws UnknownHostException{  
        boolean flag = false;  
        InetAddress Address = InetAddress.getByName(host);  
        try {  
            Socket socket = new Socket(Address,port);  //建立一个Socket连接
            flag = true;  
        } catch (IOException e) {  

        }  
        return flag;  
    }
}
