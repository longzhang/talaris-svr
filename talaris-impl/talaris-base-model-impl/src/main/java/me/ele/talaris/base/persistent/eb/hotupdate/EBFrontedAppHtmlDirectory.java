package me.ele.talaris.base.persistent.eb.hotupdate;

/**
 * Created by Daniel on 15/5/28.
 */

import me.ele.talaris.framework.dao.Column;
import me.ele.talaris.framework.dao.Table;

import java.sql.Timestamp;

@Table(name = "fronted_app_html_directory")
public class EBFrontedAppHtmlDirectory {
    @Column(pk = true, auto_increase = true)
    private int id;

    @Column
    private String version;

    @Column
    private int version_code;

    @Column
    private String canonical_url;

    @Column
    private String md5;

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

    public String getCanonical_url() {
        return canonical_url;
    }

    public void setCanonical_url(String canonical_url) {
        this.canonical_url = canonical_url;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
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
