package client.uiLogic;

import client.SocketClient;
import client.ui.FrameLogin;
import client.ui.FrameMain;
import share.message.SimpleFeedbackMsg;
import share.message.User;

/**
 * 用于处理登录事务的逻辑类
 * @author huang
 * @date 2020-05-27
 *
 */
public class LogicLogin {
	
	private FrameLogin uiController;
	
	private SocketClient socket;
	private String errorMsg;
	
	public LogicLogin() {
		socket = null;
		errorMsg = null;
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
		SimpleFeedbackMsg simpleFeedbackMsg = (SimpleFeedbackMsg) socket.recvDataObj();				
		if(simpleFeedbackMsg.getPermission()) {
			FrameMain frameMain = new FrameMain();
			LogicMain logicMain = new LogicMain(socket);
			frameMain.setLogicController(logicMain);
			logicMain.setUIController(frameMain);
			frameMain.setVisible(true);
		}else {
			errorMsg = simpleFeedbackMsg.getTextMsg();
			cond = 1;
		}
		return cond;
	}
	
	public void setUIController(FrameLogin uiController) {
		this.uiController = uiController;
	}
	
	public SocketClient getSocket() {
		return socket;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
}
