package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

import server.business.DeleteRecord;
import server.business.QuireRecord;
import server.business.UpdateRecord;
import share.instruction.InstUpDelInsRecord;
import share.instruction.InstQuireRecord;
import share.message.InstructionMsg;
import share.message.SimpleFeedbackMsg;
import share.message.User;

/**
 * 一个用于管理一个客户端-服务器连接的类
 * @author huang
 * @date 2020-06-13
 *
 */
public class ClientConnection {
	
	private SocketServer socketServer;
	private Boolean closeConnection;
	private Connection conn;
	
	public ClientConnection(SocketServer socketServer, Connection conn) {
		
		this.socketServer = socketServer;
		this.conn = conn;
		closeConnection = false;
		try {
			socketServer.connectClient();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean[] authority = login();
		if(authority[0]) {
			loginSuccessReturnMsg();
			while(!closeConnection) {
				// TODO 此处应有从服务器端接收并解析命令的模块
				Object msg = socketServer.recvDataObj();
				InstructionMsg inst = null;
				if(msg != null) {
					inst = (InstructionMsg)msg;
					switch(inst.getType()) {
					case InstructionMsg.QUIRE_RECORD:
						QuireRecord busiQuireRecord = new QuireRecord((InstQuireRecord)inst.getInstruction(), conn);
						socketServer.sendData(busiQuireRecord.getQuiryResultMsg());
						break;
					case InstructionMsg.DELETE_RECORD:
						DeleteRecord busiDeleteRecord = new DeleteRecord((InstUpDelInsRecord)inst.getInstruction(), conn, authority[1]);
						socketServer.sendData(busiDeleteRecord.getFeedBackMsg());
						break;
					case InstructionMsg.UPDATE_RECORD:
						UpdateRecord busiUpdateRecord = new UpdateRecord((InstUpDelInsRecord)inst.getInstruction(), conn, authority[1]);
						socketServer.sendData(busiUpdateRecord.getFeedBackMsg());
					}
				}else {
					closeConnection = true;
				}
			}
			socketServer.closeClient();
		}else {
			loginFailCloseConnection();
		}
		
	}
	
	/**
	 * 用于处理客户端登录的函数。用户验证部分尚未完成，在之后的迭代中再完成
	 * @return 返回一个两元素的Boolean型数组，第一个元素代表是否允许登录，第二个元素代表是否有更新/删除/新增数据库记录的权限
	 */
	private Boolean[] login() {
		//Boolean loginSuccess = null;
		Boolean[] authority = new Boolean[2];
		//接收登录信息
		Object obj = socketServer.recvDataObj();
		//将对象转换为User类型
		User user=(User) obj;
		String userAccount = user.getAccount();
		String userPassword = user.getPassword();
		// TODO 用户信息验证，目前这小段代码仅用来调试
		if(userAccount.equals("abc") && userPassword.equals("123")) {
			authority[0] = true;
			authority[1] = true;
		}else if(userAccount.equals("def") && userPassword.equals("456")){
			authority[0] = true;
			authority[1] = false;
		}else {
			authority[0] = false;
			authority[1] = false;
		}
		return authority;
	}
	
	/**
	 * 用于在客户端登录成功时返回成功信息的函数
	 */
	public void loginSuccessReturnMsg() {
		SimpleFeedbackMsg simpleFeedbackMsg = new SimpleFeedbackMsg(true, null);
		socketServer.sendData(simpleFeedbackMsg);
	}
	
	/**
	 * 用于在客户端登录失败时返回失败信息并关闭socket的函数
	 */
	private void loginFailCloseConnection() {
		SimpleFeedbackMsg simpleFeedbackMsg = new SimpleFeedbackMsg(false, "用户名或密码错误");
        socketServer.sendData(simpleFeedbackMsg);
        socketServer.closeClient();
	}

}
