package client.uiLogic.log;

import client.SocketClient;
import client.ui.FrameMain;
import share.message.LoginFeedback;
import share.message.User;

/**
 * 用于处理登录事务的逻辑类
 * @author huang
 * @date 2020-05-10
 *
 */
public class LogicLogin {
	
	private SocketClient socket;
	private String errorMsg;
	private String authorityCode;
	private FrameMain frameMain;
	
	public LogicLogin() {
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
		socket = new SocketClient(port);
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
	
	public SocketClient getSocket() {
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
