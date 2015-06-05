/**
 * 
 */
package me.ele.talaris.dao;

import java.util.List;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.User;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * @author
 * 
 *
 */
public class UserDao extends BaseSpringDao<User> {
	public UserDao() {
		super(new BeanPropertyRowMapper<User>(User.class));
	}

	/**
	 * 查看信息 根据手机号查询用户
	 * 
	 * @param mobile
	 * @return
	 */
	public User getUserByMobile(long mobile) {
		return this.selectOneOrNull("where mobile=? and status=1", mobile);
	}

	public User getUserByMobileNoMatterStatus(long mobile) {
		return this.selectOneOrNull("where mobile=?", mobile);
	}

	/**
	 * 根据用户id查询用户
	 * 
	 * @param id
	 * @return
	 */
	public User getUserById(int id) {
		return this.selectOneOrNull("where id = ? and status=1", id);
	}

	/**
	 * 查数据库表是否物理上存在这个人，不管软删除
	 * 
	 * @param id
	 * @return
	 */
	public User getUserByIdNoMatterStatus(int id) {
		return this.selectOneOrNull("where id = ? ", id);
	}

	/**
	 * 根据用户id列表获取用户列表
	 * 
	 * @param userIdList
	 * @return
	 */
	public List<User> listUserByIdList(List<Integer> userIdList) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("userIdList", userIdList);
		return namedParameterJdbcTemplate.query(this.sql_selectAllColumnsClause + "where id in (:userIdList)",
				parameters, this.rowMapper);
	}

	/**
	 * 判断配送员是否可以接单
	 * 
	 * @param courierId
	 * @return
	 */
	public boolean isCourierAvailable(int courierId) {
		User user = this.selectOneOrNull("where id=? and status=1", courierId);
		if (user == null) {
			return false;
		}
		return (user.getOnline() == 1);
	}

	/**
	 * 判断用户基本信息是否完整
	 * V2新版本只有user.getName的判断
	 * @param userId
	 * @return
	 */
	public boolean isUserBaseInfoCompleted(int userId) {
		User user = this.selectOneOrNull("where id = ? and status=1", userId);
		if (user == null) {
			return false;
		}
		if (StringUtils.isEmpty(user.getName())) {
			return false;
		}
		return true;
	}

	public int getUserStatusByUserId(int userId) {
		User user = this.selectOneOrNull("where id = ? and status=1", userId);
		if (user == null) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * 增加信息 增加配送员或者站点管理员信息，返回一个user对象的主键(id)
	 * 
	 * @param newUser
	 * @return
	 */
	public int addUser(User newUser) {
		return this.insert(newUser);
	}

	/**
	 * 更新信息
	 *
	 * 更新基本信息，如，姓名、身份证号码
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user) {
		return this.update(user);
	}

	/**
	 * 配送员可以标记自己不接单，或者站点管理员设置自己站点的某个配送员不接单 operationId用户把userId用户设置为不可接单
	 * 
	 * @param userId
	 * @return
	 */
	public int setOffline(int userId) {
		return this.jdbcTemplate.update("update user set online = ? where id = ?", 0, userId);
	}

	/**
	 * 删除信息，只是软删除 站点管理员删除配送员，需要权限检查，站点管理员只可以删除本站点配送员， 另外，只是软删除，把status设置为0
	 * 
	 * @param userId
	 * @return
	 */
	public int deleteCourier(int userId) {
		return this.jdbcTemplate.update("update user set status = ? where id = ?", 0, userId);
	}

	/**
	 * 
	 * @param userId
	 * @return
	 */
	public int hasAccepted(int userId) {
		return this.jdbcTemplate.update("update user set has_accepted = 1 where id = ?", userId);
	}

}
