/**
 * 
 */
package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.AdminModifyRecord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class AdminModifyRecordDao extends BaseSpringDao<AdminModifyRecord> {
	private final static Logger logger = LoggerFactory.getLogger(AdminModifyRecordDao.class);

	public AdminModifyRecordDao() {
		super(new BeanPropertyRowMapper<AdminModifyRecord>(AdminModifyRecord.class));
	}
	

	/**
	 * 增加一条修改wall绑定的旧老板信息
	 * @param adminModifyRecord
	 * @return
	 */
	public int addAdminModifyRecord(AdminModifyRecord adminModifyRecord) {
		return this.insert(adminModifyRecord);
	}

}
