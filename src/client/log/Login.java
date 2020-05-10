package client.log;

import client.socket.SocketBase;
import client.ui.FrameMain;
import share.LoginFeedback;
import share.User;

/**
 * 用于处理登录事务的逻辑类
 * @author huang
 * @date 2020-05-10
 *
 */
public class Login {
	
	private SocketBase socket;
	private String errorMsg;
	private String authorityCode;
	private FrameMain frameMain;
	
	public Login() {
		socket = null;
		errorMsg = null;
		authorityCode = null;
		frameMain = null;
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
		 * 获取服务器的反馈信息并处理
		 */
		LoginFeedback loginFeedback = (LoginFeedback) socket.recvDataObj();				
		if(loginFeedback.getPermission()) {
			frameMain = new FrameMain(socket ,loginFeedback.getAuthorityCode());
		}else {
			errorMsg = loginFeedback.getErrorMsg();
			cond = 1;
		}
		return cond;
	}
	
	public SocketBase getSocket() {
		return socket;
	}
	
	public FrameMain getFrameMain() {
		return frameMain;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getAuthorityCode() {
		return authorityCode;
	}
}
