/**
 * 
 */
package me.ele.talaris.exception;

/**
 * @author shaorongfei
 * @date 2015年2月6日
 * @version v1.0
 * @description
 */
public enum ExceptionCode implements IExceptionCode {
	DELIVERY_ORDER_STATUS_UPDATE_FAIL("配送信息更新失败"),
	USER_ERROR_540("补全个人信息失败"),
	USER_ERROR_530("您没有权限或者系统异常"),
	AUTH_ERROR_400("手机号不合法"),
	APP_ERROR_550("获取版本信息失败"),
	AUTH_ERROR_500("您的手机号码未绑定到餐厅"),
	AUTH_ERROR_501("您输入手机号码有误"),
	AUTH_ERROR_502("点击过快，请稍后再试"),
	AUTH_ERROR_503("您的手机号目前绑定了多个餐厅，本系统暂时不支持"),
	AUTH_ERROR_504("发送语音验证码失败"),
	AUTH_ERROR_505("发送短信验证码失败"),
	AUTH_ERROR_506("验证失败"),
	AUTH_ERROR_511("您的手机号或者验证码不正确"),
	AUTH_ERROR_512("您不是配送员"),
	AUTH_ERROR_513("系统异常"),
	AUTH_ERROR_514("权限异常"),
	AUTH_ERROR_515("管理员身份不能登陆App"),
	AUTH_ERROR_516("该餐厅已存在姓名为XXX的配送员，请重新填写"),
	AUTH_ERROR_517("未填写该餐厅配送员姓名，请重新填写"),
	AUTH_ERROR_518("已存在餐厅信息"),
	AUTH_ERROR_519("Walle开通蜂鸟配送失败"),
	AUTH_ERROR_520("已存在餐厅信息"),
	AUTH_ERROR_521("您已经是配送员或者管理员，无法再开通蜂鸟配送"),
	SETTLEMENT_ERROR_130("结算异常"),
	SETTLEMENT_ERROR_131("餐厅不存在"),
	SETTLEMENT_ERROR_132("餐厅不合法"),
	SETTLEMENT_ERROR_133("删除配送员结算初始化异常"),
	BASE_SYSTEM_PARAMETER_ERR_02("更新系统配置异常"),
	BASE_SYSTEM_PARAMETER_ERR_03("获取系统配置异常"),
	STATION_ERROR_370("系统异常"),
	APP_ERROR_552("登出失败"),
	BASE_SYSTEM_PARAMETER_ERR("系统配置异常"),
//	BASE_SYSTEM_PARAMETER_ERR("系统参数查询失败"),
//	BASE_SYSTEM_PARAMETER_NOT_FOUND("没有找到系统参数"),
//	BASE_SYSTEM_PARAMETER_FORMAT("系统参数格式错误"),
    DELIVERY_ORDER_ERROR_501("该用户已有订单在配送中"),
	DELIVERY_ORDER_ERROR_500("通知用户失败"),
	DELIVERY_ORDER_ERROR_560("获取配送单失败"),
	DELIVERY_ORDER_ERROR_580("系统异常，请重试或者手工拨打"),
	DELIVERY_ORDER_ERROR_610("系统异常，请重试"),
	HERMES_ERROR_001("发送验证码次数超过限制"),
	NAPOS_ERROR_002("发送app下载链接失败"),
	NAPOS_ERROR_404("系统异常"),
	STATION_ERROR_001("创建站点失败"),
	STATION_ERROR_002("获取配送员失败"),
	STATION_ERROR_003("没有增加配送员的权限"),
	STATION_ERROR_004("重复添加配送员"),
	STATION_ERROR_005("添加配送员失败"),
	STATION_ERROR_006("移除配送员失败"),
	STATION_ERROR_007("数据访问出错"),
    STATION_ERROR_008("站点不存在"),
	STATION_ERROR_400("系统异常"),
	STATION_ERROR_403("获取管理员失败"),
	WALLE_POST_ERROR_001("数据库异常"),
	WALLE_POST_ERROR_002("餐厅不存在"),
	BANK_CARD_ERROR_001("银行列表获取失败"),
	BANK_CARD_ERROR_002("重复绑定银行账号"),
	BANK_CARD_ERROR_003("添加银行账号绑定信息失败"),
	BANK_CARD_ERROR_004("银行账号绑定信息为空"),
	BANK_CARD_ERROR_005("更新银行账号绑定信息失败"),
	BANK_CARD_ERROR_006("查询银行账号绑定信息失败"),
	BANK_CARD_ERROR_007("新增银行账号信息后查询失败"),
	BANK_CARD_ERROR_008("修改银行账号信息后查询失败"),
	BANK_CARD_ERROR_009("新增银行账号信息后绑定失败"),
	BANK_CARD_ERROR_010("修改银行账号信息后绑定失败"),
	BANK_CARD_ERROR_011("每周只允许修改一次银行账号"),
    RESTAURANT_ERROR_001("商店id不能为空"),
    ORDER_ERROR_001("订单号不能为空");

	
	private String message;

	private ExceptionCode(String msg) {
		this.message = msg;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public String getCode() {
		return this.name();
	}

}
