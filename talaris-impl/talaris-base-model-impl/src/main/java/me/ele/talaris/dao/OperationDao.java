/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;
import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class OperationDao extends BaseSpringDao<Operation> {
	private final static Logger logger = LoggerFactory.getLogger(OperationDao.class);

	public OperationDao() {
		super(new BeanPropertyRowMapper<Operation>(Operation.class));
	}

	/**
	 * 查询所有操作记录
	 * 
	 * @return
	 */
	public List<Operation> queryAll() {
		return this.select("order by id");
	}

	/**
	 * 插入操作记录
	 * 
	 * @param operation
	 * @return
	 */
	public int insertAndRecordLog(Operation operation) {
		int result = insert(operation);
		logger.debug("增加操作记录:" + result);
		return result;
	}

	/**
	 * 修改操作记录
	 * 
	 * @param operation
	 * @return
	 */
	public int updateAndRecordLog(Operation operation) {
		int result = update(operation);
		logger.debug("更新操作记录:result" + result);
		return result;
	}

}
