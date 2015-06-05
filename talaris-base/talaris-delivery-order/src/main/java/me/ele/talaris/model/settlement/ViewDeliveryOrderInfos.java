package me.ele.talaris.model.settlement;

import java.util.List;

public class ViewDeliveryOrderInfos {
    private long total_count;
    private long total_online_count;
    private long total_offline_count;
    private List<DeliveryOrderPartInfo> deliveryOrderList;

    public ViewDeliveryOrderInfos(long total_count, long total_online_count, long total_offline_count,
            List<DeliveryOrderPartInfo> deliveryOrderList) {
        super();
        this.total_count = total_count;
        this.total_online_count = total_online_count;
        this.total_offline_count = total_offline_count;
        this.deliveryOrderList = deliveryOrderList;
    }

    public long getTotal_count() {
        return total_count;
    }

    public void setTotal_count(long total_count) {
        this.total_count = total_count;
    }

    public long getTotal_online_count() {
        return total_online_count;
    }

    public void setTotal_online_count(long total_online_count) {
        this.total_online_count = total_online_count;
    }

    public long getTotal_offline_count() {
        return total_offline_count;
    }

    public void setTotal_offline_count(long total_offline_count) {
        this.total_offline_count = total_offline_count;
    }

    public List<DeliveryOrderPartInfo> getDeliveryOrderList() {
        return deliveryOrderList;
    }

    public void setDeliveryOrderList(List<DeliveryOrderPartInfo> deliveryOrderList) {
        this.deliveryOrderList = deliveryOrderList;
    }

    public ViewDeliveryOrderInfos() {
        super();
    }

}
