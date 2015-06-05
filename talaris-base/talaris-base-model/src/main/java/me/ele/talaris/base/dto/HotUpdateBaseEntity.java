package me.ele.talaris.base.dto;

/**
 * Created by Daniel on 15/5/28.
 */
public class HotUpdateBaseEntity {
    private String version;
    private String canonical_url;
    private String md5;

    public HotUpdateBaseEntity(){}
    public HotUpdateBaseEntity(String version, String canonical_url, String md5){
        this.setVersion(version);
        this.setCanonical_url(canonical_url);
        this.setMd5(md5);
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
}
