package me.ele.talaris.model;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

@Table(name = "user_delete_log")
public class UserDeleteLog {
    @Column(pk = true, auto_increase = true)
    private int id;
    @Column
    private long origin_mobile;
    @Column
    private int origin_user_id;
    @Column
    private int prefix_number;
    @Column
    private Timestamp created_at;

    public int getId() {
        return id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getOrigin_mobile() {
        return origin_mobile;
    }

    public void setOrigin_mobile(long origin_mobile) {
        this.origin_mobile = origin_mobile;
    }

    public int getOrigin_user_id() {
        return origin_user_id;
    }

    public void setOrigin_user_id(int origin_user_id) {
        this.origin_user_id = origin_user_id;
    }

    public int getPrefix_number() {
        return prefix_number;
    }

    public void setPrefix_number(int prefix_number) {
        this.prefix_number = prefix_number;
    }

    public UserDeleteLog(int id, long origin_mobile, int origin_user_id, int prefix_number, Timestamp created_at) {
        super();
        this.id = id;
        this.origin_mobile = origin_mobile;
        this.origin_user_id = origin_user_id;
        this.prefix_number = prefix_number;
        this.created_at = created_at;
    }

    public UserDeleteLog() {
        super();
    }

}
