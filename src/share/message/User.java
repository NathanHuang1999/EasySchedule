package share.message;

import java.io.Serializable;

/**
 * 用户登录信息类
 * @author huang
 * @date 2020-05-08
 *
 */
public class User implements Serializable{
    private String account;
    private String password;
    public User(String account,String password)
    {
        this.account=account;
        this.password=password;
    }
    public String getAccount()
    {
        return account;
    }
    public void setAccount(String account)
    {
        this.account=account;
    }
    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password=password;
    }
}
