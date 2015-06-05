package me.ele.talaris.dao;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserBankInfo;

public class UserBankInfoDao extends BaseSpringDao<UserBankInfo>{

    private final static Logger logger = LoggerFactory.getLogger(UserBankInfoDao.class);
    
	public UserBankInfoDao() {
		super(new BeanPropertyRowMapper<UserBankInfo>(UserBankInfo.class));
	}
	
	public int addUserBankInfo(int user_id, String user_name, int bank_id, String bank_account) {
		UserBankInfo userBankInfo = new UserBankInfo();
		
		userBankInfo.setUser_id(user_id);
		userBankInfo.setUser_name(user_name);
		userBankInfo.setBank_id(bank_id);
		userBankInfo.setBank_account(bank_account);
		userBankInfo.setIs_bind(0);
		userBankInfo.setIs_active(1);
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		userBankInfo.setCreated_at(currentTime);
		userBankInfo.setUpdated_at(currentTime);
		
		int result = insert(userBankInfo);
		return result;
	}
	
	public UserBankInfo getUserBankInfo(int user_id) {
		return this.selectOneOrNull("where user_id = ?", user_id);
	}
	
	public int updateUserBankInfo(int id, String user_name, int bank_id, String bank_account, int is_active) {
		int result = this.jdbcTemplate.update("update user_bank_info set user_name = ?, bank_id = ? , bank_account = ?, is_active = ?, "
				+ "updated_at = ? where id = ? ",
				user_name, bank_id, bank_account, is_active, new Timestamp(System.currentTimeMillis()), id);
		return result;
	}
	
	public int updateUserBankBindInfo(int id, int is_bind) {
		int result = this.jdbcTemplate.update("update user_bank_info set updated_at = ?, is_bind = ? where id = ? ",
				new Timestamp(System.currentTimeMillis()), is_bind, id);
		return result;
	}

}
