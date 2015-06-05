package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserBankBindAbnormal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class UserBankBindAbnormalDao extends BaseSpringDao<UserBankBindAbnormal> {
 
	private final static Logger logger = LoggerFactory.getLogger(UserBankBindAbnormalDao.class);
    
	public UserBankBindAbnormalDao() {
		super(new BeanPropertyRowMapper<UserBankBindAbnormal>(UserBankBindAbnormal.class));
	}

	public int addAbnormalLog(String tradeNo, String partnerId, int isProcessed) {
		
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		
		UserBankBindAbnormal abnormalBankBindLog = new UserBankBindAbnormal(tradeNo, 
				partnerId, isProcessed, currentTimestamp, currentTimestamp);
		
		int result = insert(abnormalBankBindLog);
		
		return result;
	}
	
	//TODO
	public List<UserBankBindAbnormal> selectAbnormalLogWithProcessStatus() {
		return null;
	}
	
	//TODO
	public List<UserBankBindAbnormal> selectAbnormalLogWithouProcessStatus() {
		return null;
	}
}
