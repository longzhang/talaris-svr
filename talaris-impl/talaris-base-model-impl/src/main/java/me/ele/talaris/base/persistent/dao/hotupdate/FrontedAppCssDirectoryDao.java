package me.ele.talaris.base.persistent.dao.hotupdate;

import me.ele.talaris.base.dto.HotUpdateBaseEntity;
import me.ele.talaris.base.persistent.eb.hotupdate.EBFrontedAppCssDirectory;
import me.ele.talaris.framework.dao.BaseSpringDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 15/5/28.
 */
public class FrontedAppCssDirectoryDao extends BaseSpringDao<EBFrontedAppCssDirectory>{
    private static final String GET_CSS_BY_VERSION_CODE = "where version_code = ?";
    public FrontedAppCssDirectoryDao() {
        super(new BeanPropertyRowMapper<EBFrontedAppCssDirectory>(EBFrontedAppCssDirectory.class));
        String[] columns = this.sql_allColumnString.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            String column = columns[i];
            if (i == 0)
                sb.append(this.tableDescriptor.getTableName() + "." + column);
            else
                sb.append(", " + this.tableDescriptor.getTableName() + "." + column);
        }
        this.sql_selectAllColumnsClause = String.format("select %s from %s ", new Object[] { sb.toString(),
                this.tableDescriptor.getTableName() });
        this.sql_allColumnString = sb.toString();
    }

    public List<HotUpdateBaseEntity> getCssInfoByVersionCode(int versionCode){
        List<HotUpdateBaseEntity> rs = new ArrayList<>();

        List<EBFrontedAppCssDirectory> css = this.select(GET_CSS_BY_VERSION_CODE, versionCode);
        if(css != null){
            for(EBFrontedAppCssDirectory eb : css){
                HotUpdateBaseEntity entity = new HotUpdateBaseEntity();
                entity.setMd5(eb.getMd5());
                entity.setVersion(eb.getVersion());
                entity.setCanonical_url(eb.getCanonical_url());

                rs.add(entity);
            }
        }

        return rs;
    }


}
