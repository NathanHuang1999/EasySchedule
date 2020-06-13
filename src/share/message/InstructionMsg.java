package share.message;

import java.io.Serializable;

/**
 * 客户端向服务器发送的指令消息类
 * @author huang
 * @date 2020-05-28
 *
 */
public class InstructionMsg implements Serializable{
	
	public static final int QUIRE_RECORD = 1;
	public static final int DELETE_RECORD = 2;
	
	private int type = 0;
	private Object instruction = null;
	
	public InstructionMsg(int type, Object instruction) {
		this.type = type;
		this.instruction = instruction;
	}
	
	public int getType() {
		return type;
	}
	
	public Object getInstruction() {
		return instruction;
	}

}
