package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.BankSerialMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class BankSerialMappingDao extends BaseSpringDao<BankSerialMapping>{

    private final static Logger logger = LoggerFactory.getLogger(BankSerialMappingDao.class);
    
	public BankSerialMappingDao() {
		super(new BeanPropertyRowMapper<BankSerialMapping>(BankSerialMapping.class));
	}
	
	public List<BankSerialMapping> listActiveBanks() {
		return this.select("where is_active = 1");
	}
	
	public BankSerialMapping getBankById(int id) {
		return this.selectOneOrNull("where id = ?", id);
	}
	
}
