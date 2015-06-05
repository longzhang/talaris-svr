package me.ele.talaris.service.permission;

import me.ele.talaris.model.Context;

public interface IPermissionService {

	/**
	 * 
	 * @param context
	 * @param roleNames
	 *            role的name可以为多个。用逗号隔开
	 * @return
	 */
	public boolean hasRole(Context context, String roleNames);

	/*
	 * 判断context的user是否还有station_role
	 */
	public boolean hasStationRole(Context context, int stationId, int roleId);

	public boolean hasStationRole(Context context, int stationId,
			String roleName);

	/*
	 *  查询该用户是否是某一站点的某一角色
	 */
	public boolean isRoleTypeBelongToStation(int userId, int stationId,
			String roleName);

}