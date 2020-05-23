package server;

import java.io.IOException;
import java.net.Socket;

import share.message.LoginFeedback;
import share.message.User;

/**
 * 一个用于管理一个客户端-服务器连接的类
 * @author huang
 * @date 2020-05-23
 *
 */
public class ClientConnection {
	
	private SocketServer socketServer;
	private Boolean closeConnection;
	private Socket socket=null;
	
	public ClientConnection(SocketServer socketServer) {
		
		this.socketServer = socketServer;
		closeConnection = false;
		try {
			socketServer.connectClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean loginSuccess = login();
		if(loginSuccess) {
			loginSuccessReturnMsg();
			while(!closeConnection) {
				// TODO 此处应有从服务器端接收并解析命令的模块
			}
		}else {
			loginFailCloseConnection();
		}
		
	}
	
	/**
	 * 用于处理客户端登录的函数。用户验证部分尚未完成，在之后的迭代中再完成
	 * @return 若登录成功返回true，否则返回false
	 */
	private Boolean login() {
		Boolean loginSuccess = null;
		//接收登录信息
		Object obj = socketServer.recvDataObj();
		//将对象转换为User类型
		User user=(User) obj;
		String userAccount = user.getAccount();
		String userPassword = user.getPassword();
		// TODO 用户信息验证，目前这小段代码仅用来调试
		if(userAccount.equals("hsy") && userPassword.equals("1999221")) {
			loginSuccess = true;
		}else {
			loginSuccess = false;
		}
		return loginSuccess;
	}
	
	/**
	 * 用于在客户端登录成功时返回成功信息的函数
	 */
	public void loginSuccessReturnMsg() {
		LoginFeedback loginFeedback = new LoginFeedback("abcd");
		socketServer.sendData(loginFeedback);
	}
	
	/**
	 * 用于在客户端登录失败时返回失败信息并关闭socket的函数
	 */
	private void loginFailCloseConnection() {
		LoginFeedback loginFeedback = new LoginFeedback();
        socketServer.sendData(loginFeedback);
        socketServer.closeClient();
	}

}
