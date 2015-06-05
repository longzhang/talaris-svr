package me.ele.talaris.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * @author chaoguodeng
 *
 */
@Table(name = "station")
@Human(label = "配送站点")
public class Station {
	@Column(pk = true, auto_increase = true)
	@Human(label = "配送站点id")
    private int id;
	
	@Column
	@Human(label = "配送站点名称")
    private String name;
	
	@Column
	@Human(label = "配送站点电话")
    private String phone;
	
	@Column
	@Human(label = "配送站点地址")
    private String address;
	
	@Column
	@Human(label = "配送站点经度")
    private BigDecimal longitude;
	
	@Column
	@Human(label = "配送站点纬度")
    private BigDecimal latitude;
	
	@Column
	@Human(label = "配送站点管理员id")
    private int manager_id;
	
	@Column
	@Human(label = "配送站点管理员手机号码")
    private long manager_mobile;
	
	@Column
	@Human(label = "配送站点城市id")
    private int city_id;
	
	@Column
	@Human(label = "配送站点状态")
    private int status;
	
	@Column
	@Human(label = "配送站点创建时间")
    private Timestamp created_at;
	
	@Column
	@Human(label = "配送站点更新时间")
    private Timestamp updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public int getManager_id() {
        return manager_id;
    }

    public void setManager_id(int manager_id) {
        this.manager_id = manager_id;
    }

    public long getManager_mobile() {
        return manager_mobile;
    }

    public void setManager_mobile(long manager_mobile) {
        this.manager_mobile = manager_mobile;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
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

    public Station(int id, String name, String phone, String address, BigDecimal longitude, BigDecimal latitude,
            int manager_id, long manager_mobile, int city_id, int status, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.manager_id = manager_id;
        this.manager_mobile = manager_mobile;
        this.city_id = city_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Station(String name, String phone, String address, BigDecimal longitude, BigDecimal latitude,
            int manager_id, long manager_mobile, int city_id, int status, Timestamp created_at, Timestamp updated_at) {
        super();
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.manager_id = manager_id;
        this.manager_mobile = manager_mobile;
        this.city_id = city_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public Station() {
        super();
    }

}
