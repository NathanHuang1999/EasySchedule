package client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 客户端的socket类，用于处理和服务器的通信
 * @author huang
 * @date 2020-05-08
 *
 */
public class SocketBase {
	private Socket socket;
	private OutputStream out;
	private InputStream in;
    private ObjectOutputStream os;
    private ObjectInputStream is;
	private byte[] inBuffer;
	
	/**
	 * 建立一个socket
	 * @param portNumber 服务器的端口号
	 */
	public SocketBase(int portNumber){
		try {
			socket = new Socket("localhost", portNumber);
			out=socket.getOutputStream();
			in=socket.getInputStream();
			inBuffer = null;
			os=new ObjectOutputStream(socket.getOutputStream());
			is=new ObjectInputStream(socket.getInputStream());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用socket向服务器发送一个对象
	 * @param obj 待发送的对象
	 * @return 若发送正常返回0,否则返回1
	 */
	public int sendData(Object obj) {
		int cond = 0;
		try {
            os.writeObject(obj);
            os.flush();
		}catch(Exception e) {
			e.printStackTrace();
			cond = 1;
		}
		return cond;
	}
	
	/**
	 * 使用socket从服务器接收一个对象
	 * @return 接收到的对象
	 */
	public Object recvDataObj() {
		Object obj = null;
		try {
			obj = is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 使用socket向服务器发送一个字节数组
	 * @param output 待发送的字节数组
	 * @return 若发送正常返回0,否则返回1
	 */
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
	
	/**
	 * 使用socket从服务器接收一个字节数组
	 * @return 接收到的字节数组
	 */
	public byte[] recvDataByte(){
		try {
			Thread.sleep(100); 
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
