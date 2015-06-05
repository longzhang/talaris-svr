/**
 * 
 */
package me.ele.talaris.service.user.impl;

import java.util.ArrayList;
import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.context.utils.ContextUtil;
import me.ele.talaris.dao.RoleDao;
import me.ele.talaris.dao.UserDao;
import me.ele.talaris.dao.UserDeviceDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.user.IUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author chaoguodeng
 *
 */
@Service
public class UserService implements IUserService {

    @Autowired
    UserDao userDao;

    @Autowired
    UserDeviceDao userDeviceDao;

    @Autowired
    RoleDao roleDao;
    @Autowired
    IPermissionService permitionValidateService;
    @Autowired
    UserStationRoleDao userStationRoleDao;

    private final static Logger logger = LoggerFactory.getLogger(UserService.class);

    private int getRoleIdByRoleType(String roleType) {
        try {
            if (roleType.equals(Constant.COURIER)) {
                return roleDao.getRoleIdByRoleName(Constant.COURIER);
            } else if (roleType.equals(Constant.STATIONMANAGER)) {
                return roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
            } else {
                return 0;
            }
        } catch (Exception e) {
            throw new SystemException(Constant.SYSTEMEXCEPTIONCODE, "无法获取角色类型的roleId");
        }
    }

    /**
     * TODO: move to session service
     * 
     * 通过token获取user，会检查token有没有过期
     */
    @Override
    public User getUserByToken(String token) {
        int userId = 0;
        User user = null;
        try {
            userId = userDeviceDao.getUserIdByToke(token);
            user = userDao.getUserById(userId);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new SystemException(Constant.SYSTEMEXCEPTIONCODE, "无法根据用户id查到其对应的用户");
        }
        return user;
    }

    @Override
    public User getUserByMobile(String mobile) {
        return userDao.getUserByMobile(Long.valueOf(mobile));
    }
    
    @Override
    public User getUserByMobileNoMatterStatus(String mobile) {
    	return userDao.getUserByMobileNoMatterStatus(Long.valueOf(mobile));
    }
    
    @Override
    public int updateUser(User user) {
    	return userDao.update(user);
    }

    /**
     * 增加一个人员信息
     * 
     * @param newUser
     * @return
     */
    @Override
    public int addUser(User newUser) {
        return userDao.insert(newUser);
    }

    /**
     * 增加一个人user信息并通过手机号返回user
     * 
     * @param user
     * @return
     */
    @Override
    public User addNewUser(User user) {
        int result = userDao.insert(user);
        if (result > 0) {
            return userDao.getUserByMobile(user.getMobile());
        } else {
            return null;
        }

    }

    /**
     *
     *
     * 更新基本信息，如，姓名、身份证号码
     * 
     * @param user
     * @return
     */
    @Override
    public int updateUserInfo(User user) throws UserException, SystemException {
        int result;
        try {
            result = userDao.updateUserInfo(user);
        } catch (Exception e) {
            logger.error("补全个人信息失败,userId:{},mobile:{}", user.getId(), user.getMobile());
            throw ExceptionFactory.newUserException(ExceptionCode.USER_ERROR_540);
        }
        return result;
    }

    /**
     * 个人信息是否完整 身份证号，姓名
     * 
     * @param userId
     * @return
     */
    @Override
    public boolean isUserBaseInfoCompleted(int userId) {
        return userDao.isUserBaseInfoCompleted(userId);
    }

    /**
     * 同意协议
     * 
     * @param userId
     * @return
     */
    @Override
    public int hasAcceptedAgreement(int userId) {
        return userDao.hasAccepted(userId);
    }

    /**
     * 通过用户id查询用户信息
     * 
     * @param userId
     * @return
     */
    @Override
    public User getUserByUserId(int userId) {
        return userDao.getUserById(userId);
    }

    /**
     * 通过用户id查询用户信息NoMatterStatus
     * 
     * @param userId
     * @return
     */
    @Override
    public User getUserByUserIdNoMatterStatus(int userId) {
        return userDao.getUserByIdNoMatterStatus(userId);
    }

    @Override
    public User updateCourier(Context context, long mobile, String takerName, String certificateNumber)
            throws UserException {
        User user = validateCanUpdateCourier(context, mobile, takerName);
        if (user != null) {
            user.setCertificate_number(certificateNumber);
            user.setName(takerName);
            User userInDb = userDao.getUserById(user.getId());
            user.setStatus(userInDb.getStatus());
            userDao.update(user);
            return user;
        } else {
            throw new UserException("STATION_ERROR_366", "您无权限");
        }

    }

    /**
     * 实质上该方法只是被配送员改名称的时候调用
     * 
     * @param context
     * @param mobile
     * @param takerName
     * @return
     * @throws UserException
     */
    private User validateCanUpdateCourier(Context context, long mobile, String takerName) throws UserException {
        if (mobile != context.getUser().getMobile()) {
            throw new UserException("STATION_ERROR_360", "您无权限");
        }
        List<User> allUsers = new ArrayList<User>();
        for (UserStationRole userStationRole : context.getUserStationRoles()) {
            List<User> users = listUserByStaionIdAndRoleId(context, userStationRole.getStation_id(), 2);
            allUsers.addAll(users);
        }
        if (CollectionUtils.isEmpty(allUsers)) {
            return null;
        }
        User courier = null;
        for (User user : allUsers) {
            if (mobile == user.getMobile()) {
                courier = user;
                break;
            }
        }
        if (courier != null) {
            for (User user : allUsers) {
                if (takerName.equals(user.getName()) && courier.getMobile() != user.getMobile()) {
                    throw new UserException("STATION_ERROR_365", "该餐厅已存在姓名为" + takerName + "的配送员，请重新编辑");
                }
            }
        }
        return courier;
    }

    /**
     * 根据staionId和roleId查看对应的所有roleId人员
     * 
     * @param context
     * @param stationId
     * @param roleId
     * @return
     */
    @Override
    public List<User> listUserByStaionIdAndRoleId(Context context, int stationId, int roleId) {
        try {
            List<User> userList = new ArrayList<User>();
            List<UserStationRole> userStationRoleList = userStationRoleDao
                    .listUserStationRoleByRoldIdAndStationIdNoMatterStatus(roleId, stationId);
            for (UserStationRole userStationRole : userStationRoleList) {
                User user = userDao.getUserByIdNoMatterStatus(userStationRole.getUser_id());
                if (userStationRole.getStatus() == 0) { // 该角色已经被软删除
                    user.setStatus(0);
                }
                userList.add(user);
            }
            return userList;
        } catch (Exception e) {
            logger.error("ListUserByStationIdAndRoleId failed", e);
            throw new SystemException("STATION_ERROR_400", "系统异常");
        }
    }

}
