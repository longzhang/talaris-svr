package me.ele.talaris.service.station.persistence.eb;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

import java.sql.Timestamp;


/**
 * Created by Daniel on 15/5/12.
 */
@Table(name = "retailer_deliverer_mapping")
public class EBRetailerDelivererMapping {
    @Column(pk = true, auto_increase = true)
    private Integer id;
    @Column
    private Integer retailer_id;
    @Column
    private String  retailer_name;
    @Column
    private Integer deliverer_id;
    @Column
    private String  deliverer_name;
    @Column
    private Long    deliverer_mobile;
    @Column
    private Timestamp created_at;
    @Column
    private Timestamp updated_at;
    @Column
    private Integer is_active;

    public EBRetailerDelivererMapping (){
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRetailer_id() {
        return retailer_id;
    }

    public void setRetailer_id(Integer retailer_id) {
        this.retailer_id = retailer_id;
    }

    public String getRetailer_name() {
        return retailer_name;
    }

    public void setRetailer_name(String retailer_name) {
        this.retailer_name = retailer_name;
    }

    public Integer getDeliverer_id() {
        return deliverer_id;
    }

    public void setDeliverer_id(Integer deliverer_id) {
        this.deliverer_id = deliverer_id;
    }

    public String getDeliverer_name() {
        return deliverer_name;
    }

    public void setDeliverer_name(String deliverer_name) {
        this.deliverer_name = deliverer_name;
    }

    public Long getDeliverer_mobile() {
        return deliverer_mobile;
    }

    public void setDeliverer_mobile(Long deliverer_mobile) {
        this.deliverer_mobile = deliverer_mobile;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public Timestamp getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Timestamp updated_at) {
        this.updated_at = updated_at;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
    }
}
