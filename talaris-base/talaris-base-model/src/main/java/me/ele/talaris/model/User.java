/**
 * 
 */
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
@Table(name = "user")
@Human(label = "用户")
public class User {
    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", mobile=" + mobile + ", email=" + email
                + ", certificate_number=" + certificate_number + ", status=" + status + ", online=" + online
                + ", longitude=" + longitude + ", latitude=" + latitude + ", created_at=" + created_at
                + ", updated_at=" + updated_at + "]";
    }

    @Column(pk = true, auto_increase = true)
    @Human(label = "用户ID")
    private int id;

    @Column
    @Human(label = "用户姓名")
    private String name;

    @Column
    @Human(label = "用户手机号码")
    private long mobile;

    @Column
    @Human(label = "用户邮箱")
    private String email;

    @Column
    @Human(label = "用户身份证号码")
    private String certificate_number;

    @Column
    @Human(label = "用户状态")
    private int status;

    @Column
    @Human(label = "是否在线")
    // 配送员只有在线才可以接单
    private int online;

    @Column
    @Human(label = "用户当前所在经度")
    private BigDecimal longitude;

    @Column
    @Human(label = "用户当前所在纬度")
    private BigDecimal latitude;

    @Column
    @Human(label = "用户创建时间")
    private Timestamp created_at;

    @Column
    @Human(label = "用户更新时间")
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

    public long getMobile() {
        return mobile;
    }

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCertificate_number() {
        return certificate_number;
    }

    public void setCertificate_number(String certificate_number) {
        this.certificate_number = certificate_number;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
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

    public User(int id, String name, long mobile, String email, String certificate_number, int status, int online,
            BigDecimal longitude, BigDecimal latitude, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.certificate_number = certificate_number;
        this.status = status;
        this.online = online;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(String name, long mobile, String email, String certificate_number, int status, int online,
            BigDecimal longitude, BigDecimal latitude, Timestamp created_at, Timestamp updated_at) {
        super();
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.certificate_number = certificate_number;
        this.status = status;
        this.online = online;
        this.longitude = longitude;
        this.latitude = latitude;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User() {
        super();
    }

}
