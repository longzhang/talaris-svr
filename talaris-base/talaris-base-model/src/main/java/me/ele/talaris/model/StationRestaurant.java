package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "station_restaurant")
@Human(label = "配送站点和餐厅关联表")
public class StationRestaurant {
	@Column(pk = true, auto_increase = true)
	@Human(label = "关联id")
    private int id;
	
	@Column
	@Human(label = "配送站点id")
    private int station_id;
	
	@Column
	@Human(label = "餐厅id")
    private int rst_id;
	
	@Column
	@Human(label = "关联状态")
    private int status;
	
	@Column
	@Human(label = "关联创建时间")
    private Timestamp created_at;
	
	@Column
	@Human(label = "关联更新时间")
    private Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStation_id() {
        return station_id;
    }

    public void setStation_id(int station_id) {
        this.station_id = station_id;
    }

    public int getRst_id() {
        return rst_id;
    }

    public void setRst_id(int rst_id) {
        this.rst_id = rst_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public StationRestaurant(int id, int station_id, int rst_id, int status, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.station_id = station_id;
        this.rst_id = rst_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public StationRestaurant() {
        super();
    }

}