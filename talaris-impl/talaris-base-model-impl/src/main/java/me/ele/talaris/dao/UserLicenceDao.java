/**
 * 
 */
package me.ele.talaris.dao;
import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserLicence;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class UserLicenceDao extends BaseSpringDao<UserLicence> {
	public UserLicenceDao() {
		super(new BeanPropertyRowMapper<UserLicence>(UserLicence.class));
	}

	/**
	 * 写入userLicence关联信息
	 * 
	 * @param userLicence
	 * @return
	 */
	public int addUserLicence(UserLicence userLicence) {
		return this.insert(userLicence);
	}

	/**
	 * 查询用户协议关联表，查询是否有记录表示用户是否同意过协议(有代表同意过，没有代表没有同意过)
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isAgreedUserLicence(int userId, int licenceId) {
		UserLicence userLicence = this.selectOneOrNull("where user_id  = ? and license_id = ?", userId, licenceId);
		return (userLicence != null) ? true : false;
	}
}
