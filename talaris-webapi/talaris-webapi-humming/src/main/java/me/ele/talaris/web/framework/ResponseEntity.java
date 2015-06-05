package me.ele.talaris.web.framework;

public class ResponseEntity<E> {
    public ResponseEntity() {
        super();
    }

    private String err_code;
    private String msg;
    private E data;

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

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }

    public ResponseEntity(String err_code, String msg, E data) {
        super();
        this.err_code = err_code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> ResponseEntity<E> success(E data) {
        return new ResponseEntity<E>("200", "", data);
    }

    public static <E> ResponseEntity<E> failed(String errMsg, E data) {
        return new ResponseEntity<E>("400", errMsg, data);
    }
}
