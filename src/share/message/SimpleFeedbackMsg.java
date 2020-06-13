package share.message;

import java.io.Serializable;

/**
 * 服务器对客户端登录请求的简单反馈信息类
 * @author huang
 * @date 2020-06-13
 *
 */
public class SimpleFeedbackMsg implements Serializable{
	
	private Boolean permission = null;  //客户端请求是否被允许的标记
	private String textMsg = null;  //详细描述信息
	
	/**
	 * SimpleFeedbackMsg类的构造函数
	 */
	public SimpleFeedbackMsg(Boolean authority, String textMsg) {
		this.permission = authority;
		this.textMsg = textMsg;
	}
	
	public Boolean getPermission() {
		return permission;
	}
	
	public String getTextMsg() {
		return textMsg;
	}

}
