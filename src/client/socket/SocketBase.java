package client.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端的socket类，用于处理和服务器的通信
 * @author huang
 * @date 2020-05-07
 *
 */
public class SocketBase {
	private Socket socket;
	private OutputStream out;
	private InputStream in;
	private byte[] inBuffer;
	
	public SocketBase(int portNumber){
		try {
			socket = new Socket("localhost", portNumber);
			out=socket.getOutputStream();
			in=socket.getInputStream();
			inBuffer = null;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int sendData(byte[] output){
		int cond = 0;
		try {
			out.write(output);
		}catch(Exception e) {
			e.printStackTrace();
			cond = 1;
		}
		return cond;
	}
	
	public byte[] recvData(){
		try {
			int size = in.available();
			inBuffer = new byte[size]; 
			in.read(inBuffer);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inBuffer;
	}
	
	public int closeSocket() {
		int cond = 0;
		try {
			in.close();
			out.close();
			socket.close();
		}catch(Exception e) {
			e.printStackTrace();
			cond = 1;
		}
		return cond;
	}

}
