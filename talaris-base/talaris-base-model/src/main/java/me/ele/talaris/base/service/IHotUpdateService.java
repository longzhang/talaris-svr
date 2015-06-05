package me.ele.talaris.base.service;

import me.ele.talaris.base.dto.HotUpdateInfo;

/**
 * Created by Daniel on 15/5/28.
 */
public interface IHotUpdateService {
    public HotUpdateInfo getHotUpdateInfo(int versionCode);
    public HotUpdateInfo getLatestHotUpdateInfo();
}
