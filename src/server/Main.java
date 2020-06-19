package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

import server.business.Scheduler;

/**
 * 服务器的程序入口类
 * @author huang
 * @date 2020-06-19
 * 
 */
public class Main {
	
	private String sqlAccount;
	private String DBName;
	private Connection conn;

	public static void main(String[] args){
		
		Main main = new Main(); 
		
		//尝试连接本地的Mysql服务器
		int port = main.setUpServer(); 
		//建立socket，等待客户端连接
		ServerSocket server;
		try {
			server = new ServerSocket(port);
			System.out.println("正在等待客户端连接");
			while(true) {
				Socket newClient = server.accept();
				ClientConnection clientConnection = new ClientConnection(newClient, main.getConnection());
				clientConnection.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		 

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
	private int setUpServer() {
		Boolean accessSuccess = false; 
		int port = 0;
		String sqlPassword;
		while(!accessSuccess) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				System.out.println("请输入服务器端口号：");
				port = Integer.parseInt(br.readLine());
				System.out.println("请输入mysql客户端的用户名：");
				sqlAccount = br.readLine();
				System.out.println("请输入mysql客户端的密码：");
				sqlPassword = br.readLine();
				System.out.println("请输入数据库的名称：");
				DBName = br.readLine();
				Class.forName("com.mysql.cj.jdbc.Driver");
				/**
				 * 目前与mysql服务器的连接是不使用SSL的，未来考虑修改这个设定
				 */
				String URL = "jdbc:mysql://localhost/" + DBName + "?useSSL=false&useUnicode=true&characterEncoding=utf8";
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
			} catch (NumberFormatException e) {
				System.out.println("端口号格式错误，请重新输入！");
			}
		}
		return port;
	}
	
	public Connection getConnection() {
		return conn;
	}

}
