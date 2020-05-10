package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import share.LoginFeedback;
import share.User;

/**
 * 服务器的程序入口类
 * @author huang
 * @date 2020-05-10
 * 
 */
public class Main {

	public static void main(String[] args) throws IOException {
		
		//TODO 这块代码目前只是用来调试客户端的桩代码，未来在开发服务器时需要更换
		
	    ServerSocket serverSocket=null;
	    Socket socket=null;
	    InputStream in=null;
	    OutputStream out=null;
        //创建输入流对象
        ObjectInputStream is=null;
        //创建输出流对象
        ObjectOutputStream os=null;
	    int port=1200;
	    serverSocket = new ServerSocket(port);    //创建服务器套接字
	    System.out.println("服务器开启，等待连接。。。");
	    socket=serverSocket.accept();    //获得连接
	    //接收客户端发送的内容
        is=new ObjectInputStream(socket.getInputStream());
        os=new ObjectOutputStream(socket.getOutputStream());
        //读取一个对象
        Object obj;
		try {
			obj = is.readObject();
			//将对象转换为User类型
	        User user=(User) obj;
	      //在服务器端输出name成员和password成员信息
	        System.out.println("user: "+user.getAccount()+"/"+user.getPassword());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}     
	    LoginFeedback loginFeedback = new LoginFeedback();
        os.writeObject(loginFeedback);
        os.flush();
        
	    serverSocket.close();
	    socket.close();

	}

}
