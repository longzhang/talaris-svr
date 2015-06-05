package me.ele.talaris.service.user.impl;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.service.user.IUserStationRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserStationRoleService implements IUserStationRoleService {
    @Autowired
    UserStationRoleDao userStationRoleDao;

    @Override
    public List<UserStationRole> listUserStationRoleByUserId(int userId) {
        List<UserStationRole> userStationRoles = userStationRoleDao.listUserStationRoleByUserId(userId);
        if (CollectionUtils.isEmpty(userStationRoles)) {
            return null;
        }
        Iterator<UserStationRole> iterator = userStationRoles.iterator();
        while (iterator.hasNext()) {
            UserStationRole userStationRole = iterator.next();
            if (userStationRole.getStatus() == 0 || userStationRole.getStatus() == -1) {
                iterator.remove();
            }

        }
        return userStationRoles;

    }

    @Override
    public Timestamp getCreateTimeByUserId(int id) {
        try {
            return userStationRoleDao.getCreateTimeByUserId(id);
        } catch (DataAccessException e) {
            throw new SystemException("用户不存在", e);
        }
    }
}
