package me.ele.talaris.napos.model;

import java.sql.Timestamp;

public class General {

    private int daySn;

    private Timestamp createdTime;

    private String remark;

    private boolean isOnlinePaid;

    private boolean isBooked;

    private boolean isInvoiced;

    private Timestamp bookedTime;
    private String invoiceTitle;

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public Timestamp getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(Timestamp bookedTime) {
        this.bookedTime = bookedTime;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getDaySn() {
        return daySn;
    }

    public void setDaySn(int daySn) {
        this.daySn = daySn;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isOnlinePaid() {
        return isOnlinePaid;
    }

    public void setIsOnlinePaid(boolean isOnlinePaid) {
        this.isOnlinePaid = isOnlinePaid;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setIsBooked(boolean isBooked) {
        this.isBooked = isBooked;
    }

    public boolean isInvoiced() {
        return isInvoiced;
    }

    public void setIsInvoiced(boolean isInvoiced) {
        this.isInvoiced = isInvoiced;
    }

}
