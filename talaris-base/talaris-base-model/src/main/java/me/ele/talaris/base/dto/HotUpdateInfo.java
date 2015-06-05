package me.ele.talaris.base.dto;

import java.util.List;

/**
 * Created by Daniel on 15/5/28.
 */
public class HotUpdateInfo {
    private List<HotUpdateBaseEntity> CSS;
    private List<HotUpdateBaseEntity> Html;
    private List<HotUpdateBaseEntity> JavaScript;


    public List<HotUpdateBaseEntity> getCSS() {
        return CSS;
    }

    public void setCSS(List<HotUpdateBaseEntity> CSS) {
        this.CSS = CSS;
    }

    public List<HotUpdateBaseEntity> getHtml() {
        return Html;
    }

    public void setHtml(List<HotUpdateBaseEntity> html) {
        Html = html;
    }

    public List<HotUpdateBaseEntity> getJavaScript() {
        return JavaScript;
    }

    public void setJavaScript(List<HotUpdateBaseEntity> javaScript) {
        JavaScript = javaScript;
    }
}
