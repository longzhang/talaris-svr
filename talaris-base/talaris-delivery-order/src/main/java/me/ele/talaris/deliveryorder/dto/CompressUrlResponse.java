package me.ele.talaris.deliveryorder.dto;

/**
 * 调用百度的API生成短连接的返回
 * @author zhengwen
 *
 */
public class CompressUrlResponse {
    private String tinyurl;
    private int status;
    private String longurl;
    private String err_msg;

    public String getTinyurl() {
        return tinyurl;
    }

    public void setTinyurl(String tinyurl) {
        this.tinyurl = tinyurl;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLongurl() {
        return longurl;
    }

    public void setLongurl(String longurl) {
        this.longurl = longurl;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public CompressUrlResponse(String tinyurl, int status, String longurl, String err_msg) {
        super();
        this.tinyurl = tinyurl;
        this.status = status;
        this.longurl = longurl;
        this.err_msg = err_msg;
    }

    public CompressUrlResponse() {
        super();
    }

}
