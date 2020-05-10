package share;

import java.io.Serializable;

/**
 * 服务器对客户端登录请求的反馈信息类，包含许可和权限码
 * @author huang
 * @date 2020-05-10
 *
 */
public class LoginFeedback implements Serializable{
	private Boolean permission = null;  //是否允许登录的标记
	private String errorMsg = null;  //用户名/验证码不通过时的详细错误信息
	private String authorityCode = null;  //对数据库内容进行修改的权限码
	
	/**
	 * LoginFeedback类的构造函数，当用户名/密码验证通过时使用
	 * @param authorityCode 对数据库内容进行修改的权限码。若有则填，若没有则填null
	 */
	public LoginFeedback(String authorityCode) {
		this.permission = true;
		this.authorityCode = authorityCode;
	}
	
	/**
	 * LoginFeedback类的构造函数，当用户名/密码验证未通过时使用
	 */
	public LoginFeedback() {
		this.permission = false;
		this.errorMsg = "用户名或密码错误";
	}
	
	public Boolean getPermission() {
		return permission;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public String getAuthorityCode() {
		return authorityCode;
	}

}
