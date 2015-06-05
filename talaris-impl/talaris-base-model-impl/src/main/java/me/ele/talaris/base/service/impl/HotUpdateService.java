package me.ele.talaris.base.service.impl;

import me.ele.talaris.base.dto.HotUpdateBaseEntity;
import me.ele.talaris.base.dto.HotUpdateInfo;
import me.ele.talaris.base.persistent.dao.hotupdate.FrontedAppCssDirectoryDao;
import me.ele.talaris.base.persistent.dao.hotupdate.FrontedAppHtmlDirectoryDao;
import me.ele.talaris.base.persistent.dao.hotupdate.FrontedAppJsDirectoryDao;
import me.ele.talaris.base.service.IHotUpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Daniel on 15/5/28.
 */
@Service
public class HotUpdateService implements IHotUpdateService{
    Logger logger = LoggerFactory.getLogger(HotUpdateService.class);
    @Autowired
    FrontedAppCssDirectoryDao cssDirectoryDao;
    @Autowired
    FrontedAppJsDirectoryDao jsDirectoryDao;
    @Autowired
    FrontedAppHtmlDirectoryDao htmlDirectoryDao;
    @Value("${LatestHotUpdateVersionCode}")
    int latestVersionCode;

    public HotUpdateInfo getLatestHotUpdateInfo(){
        //TODO: Hardcode, load it from properties
        logger.info("hot update latest version code: " + latestVersionCode);
        return getHotUpdateInfo(latestVersionCode);
    }

    public HotUpdateInfo getHotUpdateInfo(int versionCode){
        HotUpdateInfo hotUpdateInfo = new HotUpdateInfo();

        List<HotUpdateBaseEntity> css = cssDirectoryDao.getCssInfoByVersionCode(versionCode);
        List<HotUpdateBaseEntity> js = jsDirectoryDao.getJSInfoByVersionCode(versionCode);
        List<HotUpdateBaseEntity> html = htmlDirectoryDao.getHtmlInfoByVersionCode(versionCode);

        hotUpdateInfo.setCSS(css);
        hotUpdateInfo.setJavaScript(js);
        hotUpdateInfo.setHtml(html);

        return hotUpdateInfo;
    }
}
