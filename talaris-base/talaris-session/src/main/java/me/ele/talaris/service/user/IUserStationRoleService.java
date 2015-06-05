package me.ele.talaris.service.user;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.model.UserStationRole;

public interface IUserStationRoleService {

	public List<UserStationRole> listUserStationRoleByUserId(int userId);

	public Timestamp getCreateTimeByUserId(int id);

}