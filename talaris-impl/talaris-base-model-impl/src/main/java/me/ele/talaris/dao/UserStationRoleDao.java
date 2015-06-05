/**
 * 
 */
package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserStationRole;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

/**
 * @author chaoguodeng
 *
 */
public class UserStationRoleDao extends BaseSpringDao<UserStationRole> {
    public UserStationRoleDao() {
        super(new BeanPropertyRowMapper<UserStationRole>(UserStationRole.class));
    }

    public UserStationRole getUserStationRoleByPk(int pk) {
        return this.selectOneOrNull("where id = ?", pk);
    }

    public UserStationRole getUserStationRoleByUserIdAndStationIdAndRoleId(int userId, int stationId, int roleId) {
        return this.selectOneOrNull("where user_id = ? and station_id = ? and role_id = ?", userId, stationId, roleId);
    }

    public UserStationRole getUserStationRoleByUserIdAndStationIdAndRoleIdWithStatus(int userId, int stationId,
            int roleId) {
        return this.selectOneOrNull("where user_id = ? and station_id = ? and role_id = ? and status = 1", userId,
                stationId, roleId);
    }

    // 根据roleId和stationId返回List<UserStationRole>
    // 注意：即使status为0，也会返回
    public List<UserStationRole> listUserStationRoleByRoldIdAndStationIdNoMatterStatus(int roleId, int stationId) {
        return this.select("where role_id = ? and station_id = ? and status !=-1", roleId, stationId);
    }

    public List<UserStationRole> listUserStationRoleByRoldIdAndStationId(int roleId, int stationId) {
        return this.select("where role_id = ? and station_id = ?", roleId, stationId);
    }

    // 根据roleId和stationId返回userIdList
    public List<Integer> listUserIdByRoleIdAndStationId(int roleId, int stationId) {
        List<UserStationRole> userStationRoleList = this.listUserStationRoleByRoldIdAndStationIdNoMatterStatus(roleId,
                stationId);
        if (userStationRoleList == null) {
            return null;
        }
        List<Integer> userIdList = new ArrayList<Integer>();
        for (UserStationRole userStationRole : userStationRoleList) {
            userIdList.add(userStationRole.getUser_id());
        }
        return userIdList;
    }

    public List<UserStationRole> listUserStationRoleByRoldId(int roleId) {
        return this.select("where role_id = ?", roleId);
    }

    public List<UserStationRole> listUserStationRoleByUserIdAndRoleIdWithStatus(int userId, int roleId) {
        return this.select("where user_id = ? and role_id = ? and status = 1", userId, roleId);
    }

    public List<UserStationRole> listUserStationRoleByUserIdAndRoldIdNoMatterStatus(int userId, int roleId) {
        return this.select("where user_id = ? and role_id = ?", userId, roleId);
    }

    public List<Integer> listUserIdByRoldId(int roleId) {
        List<UserStationRole> userStationRoleList = this.listUserStationRoleByRoldId(roleId);
        if (userStationRoleList == null) {
            return null;
        }
        List<Integer> userIdList = new ArrayList<Integer>();
        for (UserStationRole userStationRole : userStationRoleList) {
            userIdList.add(userStationRole.getUser_id());
        }
        return userIdList;
    }

    public List<UserStationRole> listUserStationRoleByStationId(int stationId) {
        return this.select("where station_id = ?", stationId);
    }

    public List<Integer> listUserIdByStationId(int stationId) {
        List<UserStationRole> userStationRoleList = this.listUserStationRoleByStationId(stationId);
        if (userStationRoleList == null) {
            return null;
        }
        Set<Integer> userIdSet = new HashSet<Integer>();
        for (UserStationRole userStationRole : userStationRoleList) {
            userIdSet.add(userStationRole.getUser_id());
        }
        List<Integer> userIdList = new ArrayList<Integer>();
        userIdList.addAll(userIdSet);
        return userIdList;
    }

    public List<UserStationRole> listUserStationRoleByUserId(int userId) {
        return this.select("where user_id = ?", userId);
    }

    // 根据当前用户id获取其对应的stationIdList
    public List<Integer> listStationIdByUserId(Integer userId) {
        List<UserStationRole> userStationRoleList = this.listUserStationRoleByUserId(userId);
        if (userStationRoleList == null) {
            return null;
        }
        Set<Integer> stationIdSet = new HashSet<Integer>();
        for (UserStationRole userStationRole : userStationRoleList) {
            stationIdSet.add(userStationRole.getStation_id());
        }
        List<Integer> stationIdList = new ArrayList<Integer>();
        stationIdList.addAll(stationIdSet);
        return stationIdList;
    }

    public int addUserStationRole(UserStationRole userStationRole) {
        return this.insert(userStationRole);
    }

    public int updateUserStationRole(UserStationRole userStationRole) {
        return this.update(userStationRole);
    }

    // 软删除
    public int deleteUserStationRoleByPk(int pk) {
        return this.jdbcTemplate.update("update user_station_role set status = 0 where id = ?", pk);
    }

    public int deleteUserStatioinRoleByStationId(int stationId) {
        return this.jdbcTemplate.update("update user_station_role set status = 0 where station_id = ?", stationId);
    }

    public int deleteUserStatioinRoleByUserId(int userId) {
        return this.jdbcTemplate.update("update user_station_role set status = 0 where user_id = ?", userId);
    }

    public int deleteUserStatioinRoleByUserIdAndStationId(int userId, int stationId) {
        return this.jdbcTemplate.update("update user_station_role set status = 0 where user_id = ? and station_id = ?",
                userId, stationId);
    }

    public Timestamp getCreateTimeByUserId(int id) {
        List<UserStationRole> userStationRoles = this.select("where user_id= ? ", id);
        if (CollectionUtils.isEmpty(userStationRoles)) {
            return null;
        }
        for (UserStationRole userStationRole : userStationRoles) {
            if (userStationRole.getRole_id() == 2) {
                return userStationRole.getCreated_at();
            }
        }
        return null;
    }
}
