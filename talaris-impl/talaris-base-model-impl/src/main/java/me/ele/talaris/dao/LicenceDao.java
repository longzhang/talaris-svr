/**
 * 
 */
package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.Licence;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class LicenceDao extends BaseSpringDao<Licence> {
	public LicenceDao() {
		super(new BeanPropertyRowMapper<Licence>(Licence.class));
	}

	/**
	 * 根据协议类型查询,默认类型type＝1，以后可能扩展;
	 * 
	 * status=2(0:无效;1:有效不可发布;2:有效可发布)
	 * 
	 * @param type
	 * @return
	 */
	public Licence getLicenceByType(int type, int status) {
		return this.selectOneOrNull("where type = ? and status = ?", type, status);
	};
}
