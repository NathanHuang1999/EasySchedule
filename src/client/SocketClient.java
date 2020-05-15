package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import share.SocketBase;

/**
 * 客户端的socket类
 * @author huang
 * @date 2020-05-15
 *
 */
public class SocketClient extends SocketBase {

	public SocketClient(int portNumber) {
		super();
		try { 
			super.socket = new Socket("localhost", portNumber);
			super.out=socket.getOutputStream(); 
			super.in=socket.getInputStream(); 
			super.os=new ObjectOutputStream(super.out); 
			super.is=new ObjectInputStream(super.in);
		}catch(IOException e) {
			e.printStackTrace(); 
		}
	}
	
}
