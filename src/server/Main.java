package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import share.message.LoginFeedback;
import share.message.User;

/**
 * 服务器的程序入口类
 * @author huang
 * @date 2020-05-15
 * 
 */
public class Main {
	
	private String sqlAccount;
	private String DBName;
	private Connection conn;

	public static void main(String[] args){
		
		Main main = new Main();
		main.login();
		SocketServer server = new SocketServer(1200);
		
		/*
		 * try { testStubForClient(); } catch (IOException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

	}
	
	public Main() {
		System.out.println("欢迎来到 易排课 EasySchdule 服务器");
		sqlAccount = null;
		DBName = null;
		conn = null;
	}
	
	/**
	 * 服务器尝试建立与mysql服务器的连接
	 */
	private void login() {
		Boolean accessSuccess = false; 
		String sqlPassword;
		while(!accessSuccess) {
			try {
				System.out.println("请输入mysql客户端的用户名：");
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				sqlAccount = br.readLine();
				System.out.println("请输入mysql客户端的密码：");
				sqlPassword = br.readLine();
				System.out.println("请输入数据库的名称：");
				DBName = br.readLine();
				Class.forName("com.mysql.cj.jdbc.Driver");
				/**
				 * 目前与mysql服务器的连接是不使用SSL的，未来考虑修改这个设定
				 */
				String URL = "jdbc:mysql://localhost/" + DBName + "?useSSL=false";
				conn = DriverManager.getConnection(URL, sqlAccount, sqlPassword);
				System.out.println("登录成功！");
				accessSuccess = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				System.out.println("数据库连接失败，请检查用户名、密码、数据库名是否正确！");
				System.out.println("请重新登录！");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void testStubForClient() throws IOException {
		
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}     
	    LoginFeedback loginFeedback = new LoginFeedback(null);
        os.writeObject(loginFeedback);
        os.flush();
        
	    serverSocket.close();
	    socket.close();
	}

}
