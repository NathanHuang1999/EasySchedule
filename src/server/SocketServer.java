package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import share.SocketBase;

/**
 * 服务器的socket类
 * @author huang
 * @date 2020-05-15
 *
 */
public class SocketServer extends SocketBase {

	private ServerSocket serverSocket;
	
	public SocketServer(int portNumber) {
		super();
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(portNumber);
			System.out.println("服务器开启，等待连接...");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void connectClient() throws IOException {
		socket=serverSocket.accept();    //获得连接
	    /**
	     * 建立输入输出流
	     */
		super.out=socket.getOutputStream(); 
		super.in=socket.getInputStream(); 
		super.os=new ObjectOutputStream(super.out); 
		super.is=new ObjectInputStream(super.in);
	}
	
	
}
