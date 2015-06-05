package me.ele.talaris.context.utils;

import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.UserStationRole;

public class ContextUtil {
    public static int getMyStationIdByContext(Context context) throws UserException {
        List<UserStationRole> userStationRoles = context.getUserStationRoles();
        for (UserStationRole userStationRole : userStationRoles) {
            if (userStationRole.getRole_id() == Constant.STATIONMANAGER_CODE) {
                return userStationRole.getStation_id();
            }
        }
        throw new UserException("NOT_MANAGER_ERROR_100", "您不是管理员");
    }

}
