package me.ele.talaris.web.log.report;

import java.sql.Timestamp;

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

/**
 * 接口日志访问实体
 * 
 * @author zhengwen
 *
 */

@Table(name = "statistic_user_access_log")
public class InterfaceLog {
    @Column(pk = true, auto_increase = true)
    private long id;
    @Column
    private int user_id;
    @Column
    private String username;
    @Column
    private String interface_name;
    @Column
    private Timestamp visit_time;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getInterface_name() {
        return interface_name;
    }

    public void setInterface_name(String interface_name) {
        this.interface_name = interface_name;
    }

    public Timestamp getVisit_time() {
        return visit_time;
    }

    public void setVisit_time(Timestamp visit_time) {
        this.visit_time = visit_time;
    }

    public InterfaceLog(int user_id, String username, String interface_name, Timestamp visit_time) {
        super();
        this.user_id = user_id;
        this.username = username;
        this.interface_name = interface_name;
        this.visit_time = visit_time;
    }

    public InterfaceLog() {
        super();
    }
    

}
