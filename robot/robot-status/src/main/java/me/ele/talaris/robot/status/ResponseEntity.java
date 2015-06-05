package me.ele.talaris.robot.status;

/**
 * Created by Daniel on 15/5/21.
 */
public class ResponseEntity{
    String err_code;
    String msg;
    String data;

    public ResponseEntity(){}

    public ResponseEntity(String err_code, String msg, String data) {
        this.err_code = err_code;
        this.msg = msg;
        this.data = data;
    }

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
