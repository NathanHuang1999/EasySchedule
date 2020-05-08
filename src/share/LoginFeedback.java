package share;

import java.io.Serializable;

/**
 * 服务器对客户端登录请求的反馈信息类，包含许可和权限码
 * @author huang
 * @date 2020-05-08
 *
 */
public class LoginFeedback implements Serializable{
	private Boolean permission = null;
	private String authorityCode = null;
	
	public LoginFeedback(Boolean permission, String authorityCode) {
		this.permission = permission;
		this.authorityCode = authorityCode;
	}
	
	public Boolean getPermission() {
		return permission;
	}
	
	public String getAuthorityCode() {
		return authorityCode;
	}

}
