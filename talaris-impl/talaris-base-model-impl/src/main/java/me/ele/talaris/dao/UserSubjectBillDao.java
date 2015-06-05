package me.ele.talaris.dao;


import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserSubjectBill;
import me.ele.talaris.utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class UserSubjectBillDao extends BaseSpringDao<UserSubjectBill> {

    private final static Logger logger = LoggerFactory.getLogger(UserSubjectBillDao.class);
    
	public UserSubjectBillDao() {
		super(new BeanPropertyRowMapper<UserSubjectBill>(UserSubjectBill.class));
	}

	
	/**
     * 查询某个配送员某种类型的补贴单
     * 
     * @param courierId
     * @param startTime
     * @return
	 */
	public List<UserSubjectBill> selectOneCourierBill(int courierId, Timestamp startTime) {
		
		return (List<UserSubjectBill>) this.select("where user_id= ? and created_at >= ? ", courierId, startTime);
		
	}
	
	/**
     * 分页查询某个配送员所有类型的补贴单
     * 
     * @param courierId
     * @param pageNow
     * @param pageSize
     * @param startTime
     * @return
	 */
	public Pair<List<UserSubjectBill>, Long> selectPageOneCourierAllTypeBill(int courierId, int pageNow, int pageSize, Timestamp startTime) {
		
		return (Pair<List<UserSubjectBill>, Long>) this.selectPage("where user_id= ? and created_at >= ?",
                pageNow, pageSize, courierId, startTime);
		
	}
	
}
