/**
 * 
 */
package me.ele.talaris.service.auth;

/**
 * @author shaorongfei
 */
public class AuthConstant {
	// * 用户状态 ：status 1有效；0无效
	// * 配送员是否工作: online 1工作；0不工作
	// * 配送单状态：status 0:待取餐,1:待配送,2:配送中,3:已送达,4:已取消,-1:异常
	// * payment
	public  static int deviceIsValid = 1;// 设备是否有效
	public  static int statusValid = 1;// 状态有效
	public  static int statusInValid = 0;// 状态无效
	public  static int online = 1;// 工作
	public  static int outline = 0;// 不工作
	public  static int isUserInfoComplete = 1; // 是否补全个人信息 1：完整，0 不完整
	public  static int isAgree = 1; // 0没有同意，1同意

}
