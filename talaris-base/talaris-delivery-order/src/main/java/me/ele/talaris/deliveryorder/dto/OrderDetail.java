package me.ele.talaris.deliveryorder.dto;

import me.ele.talaris.model.EleOrderDetail;

public class OrderDetail {
    private EleOrderDetail eleOrderDetail;
    private DeliveryOrder deliveryOrder;

    public EleOrderDetail getEleOrderDetail() {
        return eleOrderDetail;
    }

    public void setEleOrderDetail(EleOrderDetail eleOrderDetail) {
        this.eleOrderDetail = eleOrderDetail;
    }

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

}
