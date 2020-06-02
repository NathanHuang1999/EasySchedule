package share.message;

import java.io.Serializable;

/**
 * 服务器对客户端登录请求的反馈信息类
 * @author huang
 * @date 2020-05-27
 *
 */
public class LoginFeedback implements Serializable{
	private Boolean permission = null;  //是否允许登录的标记
	private String errorMsg = null;  //用户名/验证码不通过时的详细错误信息
	
	/**
	 * LoginFeedback类的构造函数
	 */
	public LoginFeedback(Boolean authority, String errorMsg) {
		this.permission = authority;
		this.errorMsg = errorMsg;
	}
	
	public Boolean getPermission() {
		return permission;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}

}
