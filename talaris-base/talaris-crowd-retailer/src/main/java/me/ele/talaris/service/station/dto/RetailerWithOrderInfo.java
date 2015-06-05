package me.ele.talaris.service.station.dto;


import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Daniel on 15/5/13.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class RetailerWithOrderInfo implements Comparable<RetailerWithOrderInfo> {

    private Integer rst_id;
    private String rst_name;
    private Integer order_counts;

    private Timestamp binded_time;

    public Timestamp getBinded_time() {
        return binded_time;
    }

    public void setBinded_time(Timestamp binded_time) {
        this.binded_time = binded_time;
    }

    private List<DeliveryOrder> orders;

    public Integer getRst_id() {
        return rst_id;
    }

    public void setRst_id(Integer rst_id) {
        this.rst_id = rst_id;
    }

    public String getRst_name() {
        return rst_name;
    }

    public RetailerWithOrderInfo(Integer rst_id, String rst_name, Integer order_counts, Timestamp binded_time,
            List<DeliveryOrder> orders) {
        super();
        this.rst_id = rst_id;
        this.rst_name = rst_name;
        this.order_counts = order_counts;
        this.binded_time = binded_time;
        this.orders = orders;
    }

    public void setRst_name(String rst_name) {
        this.rst_name = rst_name;
    }

    public Integer getOrder_counts() {
        return order_counts;
    }

    public void setOrder_counts(Integer order_counts) {
        this.order_counts = order_counts;
    }

    public List<DeliveryOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<DeliveryOrder> orders) {
        this.orders = orders;
    }

    public RetailerWithOrderInfo() {
        super();
    }

    @Override
    public int compareTo(RetailerWithOrderInfo o) {

        return o.getOrder_counts() - this.getOrder_counts();
    }

}
