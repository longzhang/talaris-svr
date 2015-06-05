package me.ele.talaris.constant;

public class DeliveryOrderContant {

    /**
     * 简易版 扫码
     */
    public static final String FETCH_ORDER_ACTION_TYPE_BARCODE = "0";

    /**
     * 简易版 列表
     */
    public static final String FETCH_ORDER_ACTION_TYPE_LIST = "1";
    /**
     * 系统自动拉单
     */
    public static final String FETCH_ORDER_ACTION_TYPE_BY_SYSTEM = "2";

    public static final String CREATE_ORDER_MANUALLY = "3";

    /**
     * 配送单状态,待取餐
     */
    public static int WAITTO_FETCH = 0;

    /**
     * 配送单状态，待配送
     */
    public static int WAITTO_DELIVERY = 1;

    /**
     * 配送员取消手动录入后的单子的状态
     * 
     */
    public static int NOT_ELE_ORDER_CANCELED = -10;
    /**
     * 配送中
     */
    public static int DELIVERYING = 2;
    /**
     * 配送完成
     */
    public static int DELIVERIED = 3;
    /**
     * 取消的
     */
    public static int CANCEL = 4;
    /**
     * 异常：配送员标记异常
     */
    public static int EXCEPTION = -1;
    /**
     * 异常：配送中超时
     */
    public static int DELIVERING_EXCEPTION = -2;
    /**
     * 异常：待配送超时
     */
    public static int WAIT_TO_DELIVER_EXCEPTION = -3;
    /**
     * 配送操作,取餐
     */

    public static int OPERATION_FETCH = 1;
    /**
     * 配送操作,分拣
     */
    public static int OPERATION_SORTING = 2;
    /**
     * 配送
     */
    public static int OPERATION_DELIVERYING = 3;
    /**
     * 标记异常
     */
    public static int OPERATION_MARKEXCEPTION = 4;
    /**
     * 标记正常
     * 
     */
    public static int OPERATION_MARKNORMAL = 5;
    /**
     * 电话通知
     */
    public static int OPERATION_CALL = 6;
    /**
     * 确认送达
     */
    public static int OPERATION_CONFIRM = 7;
    /**
     * 取消操作
     */
    public static int OPERATION_CANCEL = 8;

    /**
     * 只需要配送单信息
     */
    public static final String DETAIL_LEVER_ZERO = "0";
    /**
     * 需要配送单和饿单详情信息
     */
    public static final String DETAIL_LEVER_ONE = "1";
    /**
     * 确认送达成功
     */
    public static final int CONFIRM_DELIVEIED_SUCCESS = 1;

    /**
     * 确认送达失败
     */
    public static final int CONFIRM_DELIVEIED_FAIL = 0;
    /**
     * 在线支付
     */
    public static final int ONLINE_PAID = 1;
    /**
     *  
     */
    public static final int CASH_ON_DELIVERY = 0;
    /**
     * 饿单来源
     */
    public static final int SOURCE_FROM_ELEME = 0;

    public static final int SOURCE_FROM_MANAULLY = 1;
    /**
     * 判断是否是预订单
     */
    public final static String ISBOOKED = "\"booked\":true";
    /**
     * 预订单预定送达时间
     */
    public final static String BOOKEDTIME = "\"bookedTime\"";
    /**
     * 饿单创建时间
     */
    public final static String CREATETIME = "\"createdTime\"";
    /**
     * 系统标记异常msg
     */
    public static final String MARKEXCEPTIONMSG = "系统自动标记异常--订单超过4小时未送达";

    /**
     * 系统操作，标记系统为操作人
     */
    public static final int SYSTEM_OPERATOR = -10000;

    public static final String MARK_CONFIRM_MSG = "系统自动更新订单状态为已送达--订单配送时间超过3小时";

}
