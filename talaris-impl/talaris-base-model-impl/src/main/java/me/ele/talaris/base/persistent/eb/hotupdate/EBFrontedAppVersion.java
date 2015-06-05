package me.ele.talaris.base.persistent.eb.hotupdate;

import me.ele.talaris.bean.Human;
import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

import java.sql.Timestamp;

/**
 * Created by Daniel on 15/5/28.
 */

@Table(name="fronted_app_version")
public class EBFrontedAppVersion {
    @Column(pk = true, auto_increase = true)
    private int id;

    @Column
    private String version;

    @Column
    private int version_code;

    @Column
    private String update_note;

    @Column
    private int force;

    @Column
    private int is_active;

    @Column
    private Timestamp created_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

    public String getUpdate_note() {
        return update_note;
    }

    public void setUpdate_note(String update_note) {
        this.update_note = update_note;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
