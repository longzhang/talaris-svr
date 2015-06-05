package me.ele.talaris.service.permission.impl;

import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.dao.RoleDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.service.permission.IPermissionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService implements IPermissionService {
	@Autowired
	RoleDao roleDao;

	@Autowired
	UserStationRoleDao userStationRoleDao;

	/**
	 * 
	 * @param context
	 * @param roleNames
	 *            role的name可以为多个。用逗号隔开
	 * @return
	 */
	@Override
	public boolean hasRole(Context context, String roleNames) {
		String[] roles = roleNames.split(",");
		List<Integer> ids = new ArrayList<Integer>();
		for (String role : roles) {
			int id = roleDao.getRoleIdByRoleName(role);
			ids.add(id);
		}

		List<UserStationRole> userStationRoles = context.getUserStationRoles();
		for (UserStationRole userStationRole : userStationRoles) {
			for (int id : ids) {
				if (userStationRole.getRole_id() == id) {
					return true;
				}
			}

		}
		return false;
	}

	/*
	 * 判断context的user是否还有station_role
	 */
	@Override
	public boolean hasStationRole(Context context, int stationId, int roleId) {
		
		if (roleId == 0) {
			return false;
		}
		User currentUser = context.getUser();
		List<Integer> userIdList = userStationRoleDao
				.listUserIdByRoleIdAndStationId(roleId, stationId);
		if (userIdList.contains(currentUser.getId())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean hasStationRole(Context context, int stationId, String roleName) {
		int roleId = roleDao.getRoleIdByRoleName(roleName);
		return this.hasStationRole(context, stationId, roleId);
	}
	
	/*
	 *  查询该用户是否是某一站点的某一角色
	 */
	@Override
	public boolean isRoleTypeBelongToStation(int userId, int stationId, String roleName) {
		int roleId = roleDao.getRoleIdByRoleName(roleName);
		if(roleId == 0) {
			return false;
		}
		return (userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(userId, stationId, roleId) != null);
		
	}
}
