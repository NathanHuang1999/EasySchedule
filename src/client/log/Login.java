package client.log;

import client.socket.SocketBase;
import share.LoginFeedback;
import share.User;

/**
 * 用于处理登录事务的逻辑类
 * @author huang
 * @date 2020-05-08
 *
 */
public class Login {
	
	private SocketBase socket;
	private String permission;
	private String authorityCode;
	
	public Login() {
		socket = null;
		permission = null;
		authorityCode = null;
	}
	
	public int login(int port, String account, String password) {
		int cond = 0;
		/**
		 * 试图与服务器建立连接，并发送登录请求
		 */
		User user = new User(account, password);
		socket = new SocketBase(port);
		socket.sendData(user);
		/**
		 * 获取服务器的反馈
		 */
		LoginFeedback loginFeedback = (LoginFeedback) socket.recvDataObj();				
		//TODO 对登录许可的判断和后续处理
		System.out.println(loginFeedback.getPermission());
		System.out.println(loginFeedback.getAuthorityCode());
		
		return cond;
	}
	
	public SocketBase getSocket() {
		return socket;
	}
	
	public String getAuthorityCode() {
		return authorityCode;
	}
}
