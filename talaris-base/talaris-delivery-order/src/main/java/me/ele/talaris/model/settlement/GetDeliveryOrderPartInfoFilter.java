package me.ele.talaris.model.settlement;

public class GetDeliveryOrderPartInfoFilter {
    @Override
    public String toString() {
        return "GetDeliveryOrderPartInfoFilter [taker_id=" + taker_id + ", status=" + status + ", payment_type="
                + payment_type + ", page_now=" + page_now + ", page_size=" + page_size + ", detail_level="
                + detail_level + "]";
    }

    private Integer taker_id = 0;
    private Integer status;
    private Integer payment_type;
    private Integer page_now = 1;
    private Integer page_size = 10;
    private String detail_level = "0";
    private int rst_id;

    public int getRst_id() {
        return rst_id;
    }

    public void setRst_id(int rst_id) {
        this.rst_id = rst_id;
    }

    public Integer getTaker_id() {
        return taker_id;
    }

    public void setTaker_id(Integer taker_id) {
        this.taker_id = taker_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(Integer payment_type) {
        this.payment_type = payment_type;
    }

    public Integer getPage_now() {
        return page_now;
    }

    public void setPage_now(Integer page_now) {
        this.page_now = page_now;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public String getDetail_level() {
        return detail_level;
    }

    public void setDetail_level(String detail_level) {
        this.detail_level = detail_level;
    }

    public GetDeliveryOrderPartInfoFilter(Integer taker_id, Integer status, Integer payment_type, Integer page_now,
            Integer page_size, String detail_level, int rst_id) {
        super();
        this.taker_id = taker_id;
        this.status = status;
        this.payment_type = payment_type;
        this.page_now = page_now;
        this.page_size = page_size;
        this.detail_level = detail_level;
        this.rst_id = rst_id;
    }

    public GetDeliveryOrderPartInfoFilter() {
        super();
    }

}
