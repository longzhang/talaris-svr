package me.ele.talaris.deliveryorder.dto;

/**
 * 调用map返回是否有配送员的地址信息
 * 
 * @author zhengwen
 *
 */
public class AddressResponse {
    private String err_code;
    private String msg;
    private int data;

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public AddressResponse(String err_code, String msg, int data) {
        super();
        this.err_code = err_code;
        this.msg = msg;
        this.data = data;
    }

    public AddressResponse() {
        super();
    }

}
