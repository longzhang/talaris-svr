package me.ele.talaris.service.station.persistence.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.service.station.persistence.eb.EBRetailerDelivererMapping;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * Created by eleme on 15/5/12.
 */
public class RetailerDelivererMappingDao extends BaseSpringDao<EBRetailerDelivererMapping> {

    public RetailerDelivererMappingDao() {
        super(new BeanPropertyRowMapper<EBRetailerDelivererMapping>(EBRetailerDelivererMapping.class));
    }

    public int insert(EBRetailerDelivererMapping entity){
        return super.insert(entity);
    }
}
