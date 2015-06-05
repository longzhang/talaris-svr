package me.ele.talaris.response;

import me.ele.talaris.model.User;

/**
 * 
 * @author zhengwen
 * 登录返回的数据
 *
 */
public class LoginResult {
    private String access_token;
    private int expire_minutes;
    // 用户信息是否完整。填1/0;
    private int is_userInfo_whole;
    private User user;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LoginResult() {
        super();
    }

    public int getIs_userInfo_whole() {
        return is_userInfo_whole;
    }

    public void setIs_userInfo_whole(int is_userInfo_whole) {
        this.is_userInfo_whole = is_userInfo_whole;
    }

    public LoginResult(String access_token, int expire_minutes, int is_userInfo_whole, User user) {
        this.access_token = access_token;
        this.expire_minutes = expire_minutes;
        this.is_userInfo_whole = is_userInfo_whole;
        this.user = user;
    }

    public int getExpire_minutes() {
        return expire_minutes;
    }

    public void setExpire_minutes(int expire_minutes) {
        this.expire_minutes = expire_minutes;
    }

}
