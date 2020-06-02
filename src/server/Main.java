package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 * 服务器的程序入口类
 * @author huang
 * @date 2020-06-02
 * 
 */
public class Main {
	
	private String sqlAccount;
	private String DBName;
	private Connection conn;

	public static void main(String[] args){
		
		Main main = new Main(); 
		
		//尝试连接本地的Mysql服务器
		main.connectMysqlServer(); 
		//建立socket，等待客户端连接
		SocketServer server = new SocketServer(12000); 
		System.out.println("正在等待客户端连接");
		while(true) {
			ClientConnection clientConnection = new ClientConnection(server, main.getConnection());
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
	private void connectMysqlServer() {
		Boolean accessSuccess = false; 
		String sqlPassword;
		while(!accessSuccess) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
			}
		}
	}
	
	public Connection getConnection() {
		return conn;
	}

}
