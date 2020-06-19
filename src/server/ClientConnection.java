package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;

import server.business.DeleteRecord;
import server.business.InsertRecord;
import server.business.QuireRecord;
import server.business.Scheduler;
import server.business.UpdateRecord;
import share.instruction.InstUpDelInsRecord;
import share.instruction.InstQuireRecord;
import share.message.InstructionMsg;
import share.message.SimpleFeedbackMsg;
import share.message.User;

/**
 * 一个用于管理一个客户端-服务器连接的类
 * @author huang
 * @date 2020-06-19
 *
 */
public class ClientConnection implements Runnable{
	
	private Thread thread;
	
	private Socket client;
	private OutputStream out;
	private InputStream in;
	private ObjectOutputStream os;
	private ObjectInputStream is;
	private Boolean closeConnection;
	private Connection conn;
	
	public ClientConnection(Socket client, Connection conn) throws IOException {
		
		this.client = client;
		out=client.getOutputStream(); 
		in=client.getInputStream(); 
		os=new ObjectOutputStream(out); 
		is=new ObjectInputStream(in);
		
		this.conn = conn;
		
	}
	
	/**
	 * 用于处理客户端登录的函数。用户验证部分尚未完成，在之后的迭代中再完成
	 * @return 返回一个两元素的Boolean型数组，第一个元素代表是否允许登录，第二个元素代表是否有更新/删除/新增数据库记录的权限
	 */
	private Boolean[] login() {
		Boolean[] authority = new Boolean[2];
		//接收登录信息
		Object obj = recvDataObj();
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
		sendData(simpleFeedbackMsg);
	}
	
	/**
	 * 用于在客户端登录失败时返回失败信息并关闭socket的函数
	 * @throws IOException 
	 */
	private void loginFailCloseConnection() throws IOException {
		SimpleFeedbackMsg simpleFeedbackMsg = new SimpleFeedbackMsg(false, "用户名或密码错误");
		sendData(simpleFeedbackMsg);
		client.close();
	}
	
	/**
	 * 使用socket向服务器发送一个对象
	 * @param obj 待发送的对象
	 * @return 若发送正常返回0,否则返回1
	 */
	public int sendData(Object obj) {
		int cond = 0;
		try {
            os.writeObject(obj);
            os.flush();
		}catch(Exception e) {
			e.printStackTrace();
			cond = 1;
		}
		return cond;
	}
	
	/**
	 * 使用socket从服务器接收一个对象
	 * @return 接收到的对象
	 */
	public Object recvDataObj() {
		Object obj = null;
		try {
			obj = is.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 使用socket向服务器发送一个字节数组
	 * @param output 待发送的字节数组
	 * @return 若发送正常返回0,否则返回1
	 */
	public int sendData(byte[] output){
		int cond = 0;
		try {
			out.write(output);
		}catch(Exception e) {
			e.printStackTrace();
			cond = 1;
		}
		return cond;
	}
	
	/**
	 * 使用socket从服务器接收一个字节数组
	 * @return 接收到的字节数组
	 */
	public byte[] recvDataByte(){
		byte[] inBuffer = null;
		try {
			Thread.sleep(100); 
			int size = in.available(); 
			inBuffer = new byte[size];
			in.read(inBuffer);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return inBuffer;
	}
	
	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		Boolean[] authority = login();
		if(authority[0]) {
			loginSuccessReturnMsg();
			closeConnection = false;
			while(!closeConnection) {
				Object msg = recvDataObj();
				InstructionMsg inst = null;
				if(msg != null) {
					inst = (InstructionMsg)msg;
					switch(inst.getType()) {
					case InstructionMsg.QUIRE_RECORD:
						QuireRecord busiQuireRecord = new QuireRecord((InstQuireRecord)inst.getInstruction(), conn);
						sendData(busiQuireRecord.getQuiryResultMsg());
						break;
					case InstructionMsg.DELETE_RECORD:
						DeleteRecord busiDeleteRecord = new DeleteRecord((InstUpDelInsRecord)inst.getInstruction(), conn, authority[1]);
						sendData(busiDeleteRecord.getFeedBackMsg());
						break;
					case InstructionMsg.UPDATE_RECORD:
						UpdateRecord busiUpdateRecord = new UpdateRecord((InstUpDelInsRecord)inst.getInstruction(), conn, authority[1]);
						sendData(busiUpdateRecord.getFeedBackMsg());
						break;
					case InstructionMsg.INSERT_RECORD:
						InsertRecord busiInsertRecord = new InsertRecord((InstUpDelInsRecord)inst.getInstruction(), conn, authority[1]);
						sendData(busiInsertRecord.getFeedBackMsg());
						break;
					case InstructionMsg.RUN_SCHEDULER:
						//TODO 完善排课模块
						Scheduler scheduler = new Scheduler(conn);
					}
				}else {
					closeConnection = true;
				}
			}
			try {
				client.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			try {
				loginFailCloseConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
