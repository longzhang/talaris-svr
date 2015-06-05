/**
 * 
 */
package me.ele.talaris.service.station.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.context.utils.ContextUtil;
import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.RoleDao;
import me.ele.talaris.dao.SettlementDao;
import me.ele.talaris.dao.SettlementDeliveryOrderDao;
import me.ele.talaris.dao.StationDao;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.dao.UserDao;
import me.ele.talaris.dao.UserDeleteLogDao;
import me.ele.talaris.dao.UserDeviceDao;
import me.ele.talaris.dao.UserStationRoleDao;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.dao.DeliveryOrderDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.Settlement;
import me.ele.talaris.model.SettlementDeliveryOrder;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.StationRestaurant;
import me.ele.talaris.model.User;
import me.ele.talaris.model.UserDeleteLog;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.station.dto.RetailerWithOrderInfo;
import me.ele.talaris.service.station.persistence.dao.RetailerDelivererMappingDao;
import me.ele.talaris.service.station.persistence.eb.EBRetailerDelivererMapping;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * STATION will be refactory to RETAILER
 * 
 * @author chaoguodeng
 *
 */
@Service
public class StationService implements IStationService {
    private static String PREFIX_NUMBER = "100000";

    public static Logger logger = LoggerFactory.getLogger(StationService.class);
    @Autowired
    StationDao stationDao;

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    RetailerDelivererMappingDao retailerDelivererMappingDao;

    @Autowired
    StationRestaurantDao stationRestaurantDao;

    @Autowired
    IPermissionService permitionValidateService;

    @Autowired
    RoleDao roleDao;

    @Autowired
    UserStationRoleDao userStationRoleDao;

    @Autowired
    UserDao userDao;
    @Autowired
    UserDeleteLogDao userDeleteLogDao;

    @Autowired
    DeliveryOrderDao deliveryOrderDao;

    @Autowired
    EleOrderDetailDao eleOrderDetailDao;

    @Autowired
    SettlementDao settlementDao;

    @Autowired
    SettlementDeliveryOrderDao settlementDeliveryOrderDao;

    @Autowired
    UserDeviceDao userDeviceDao;

    /**
     * 第一版：餐厅老板就是站点管理员 创建站点: 现在的应用场景是，餐厅老板（即站点管理员）第一次登录时候，自动创建站点
     * 到了创建站点这一步，正常情况下，站点所对应的餐厅应该已经存在本地数据库
     * 
     * @param user
     * @return
     */
    @Override
    public Station createStationByUser(User user) {
        try {
            int userId = user.getId();

            Restaurant restaurant = restaurantDao.getRestaurantByMobile(user.getMobile());
            // 如果该餐厅不存在，不创建站点
            if (restaurant == null) {
                return null;
            }

            int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);

            UserStationRole userStationRole = userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(
                    userId, 0, stationManagerRoleId);
            // 权限检查，如果该用户不是站点管理员，返回
            if (userStationRole == null) {
                return null;
            }

            Date date = new Date();
            Timestamp timeStamp = new Timestamp(date.getTime());

            Station station = new Station();
            station.setName(restaurant.getName());
            station.setPhone(restaurant.getPhone());
            station.setAddress(restaurant.getAddress());
            station.setLongitude(restaurant.getLongitude());
            station.setLatitude(restaurant.getLatitude());
            station.setManager_id(userId);
            station.setManager_mobile(user.getMobile());
            station.setCity_id(restaurant.getCity_id());
            station.setStatus(1);
            /*
             * to do : 以下语句： station.setCreat ed_at(timeStamp); 需要删除
             */
            station.setCreated_at(timeStamp);
            station.setUpdated_at(timeStamp);
            stationDao.addStation(station);

            // 获取插入的station的id
            int stationPk = stationDao.getStationByMobile(user.getMobile()).getId();
            // 插入StationRestaurant关联数据
            StationRestaurant stationRestaurant = new StationRestaurant();
            stationRestaurant.setRst_id(restaurant.getId());
            stationRestaurant.setStation_id(stationPk);
            stationRestaurant.setStatus(1);
            stationRestaurant.setCreated_at(timeStamp);
            stationRestaurant.setUpdated_at(timeStamp);
            stationRestaurantDao.insert(stationRestaurant);

            // 更新user_station_role表
            userStationRole.setStation_id(stationPk);
            userStationRole.setStatus(1);
            userStationRole.setCreated_at(timeStamp);
            userStationRole.setUpdated_at(timeStamp);
            userStationRoleDao.update(userStationRole);

            // 默认情况下，餐厅老板也是配送员
            userStationRole.setRole_id(roleDao.getRoleIdByRoleName(Constant.COURIER));
            userStationRole.setId(0);
            userStationRoleDao.insert(userStationRole);

            return station;

        } catch (Exception e) {
            logger.error("创建站点失败", e);
            throw new SystemException("STATION_ERROR_001", "创建站点失败");

        }
    }

    /**
     * 第一版：一个餐厅老板只对应一个站点 查看currentUser的站点: 如果currentUser是直接返回currentUser的station
     * 
     * @param context
     * @return
     */
    @Override
    public Station getStationByContext(Context context) {
        try {
            User currentUser = context.getUser();
            // 权限检查，如果该用户不是站点管理员，返回
            if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
                return null;
            }
            return stationDao.getStationByMobile(currentUser.getMobile());
        } catch (Exception e) {
            logger.error("getStationByContext failed", e);
            throw new SystemException("STATION_ERROR_400", "系统异常");
        }
    }

    /**
     * 查看stationId的站点：只有该站点的管理员才可以查看该站点
     * 
     * @param context
     * @param stationId
     * @return
     */
    @Override
    public Station getStationByStationId(Context context, int stationId) {
        try {
            User currentUser = context.getUser();
            // 权限检查，如果该用户不是站点管理员，返回
            if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
                return null;
            }
            int roleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
            List<Integer> userIdList = userStationRoleDao.listUserIdByRoleIdAndStationId(roleId, stationId);
            // 权限检查：只有该站点的管理员才可以查看该站点
            if (userIdList == null || !userIdList.contains(currentUser.getId())) {
                return null;
            }
            return stationDao.getStationByPk(stationId);

        } catch (Exception e) {
            logger.error("getStationByStaionId failed", e);
            throw new SystemException("STATION_ERROR_400", "系统异常");
        }
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
            int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
            // 权限检查：只有该站点的管理员才可以查看该站点的所有roleId人员
            if (!permitionValidateService.hasStationRole(context, stationId, stationManagerRoleId)) {
                return null;
            }
            List<User> userList = new ArrayList<User>();
            List<UserStationRole> userStationRoleList = userStationRoleDao
                    .listUserStationRoleByRoldIdAndStationIdNoMatterStatus(roleId, stationId);
            for (UserStationRole userStationRole : userStationRoleList) {
                User user = userDao.getUserByIdNoMatterStatus(userStationRole.getUser_id());
                // 一些人已经被永久删除了
                if (userStationRole.getStatus() == -1) {
                    continue;
                }
                // 该角色在user表中已经彻底删除
                if (user.getStatus() == 0) {
                    continue;
                }
                // 该角色已经被停用
                if (userStationRole.getStatus() == 0) {
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

    /**
     * 根据用户类型(如，配送员，站点管理员)获取用户
     * 
     * @param context
     * @param userType
     * @return
     */
    @Override
    public List<User> listUserByUserType(Context context, String userType) {
        try {
            Station station = getStationByContext(context); // 该方法里面会做权限检查
            int roleId = roleDao.getRoleIdByRoleName(userType);
            List<Integer> userIdList = userStationRoleDao.listUserIdByRoleIdAndStationId(roleId, station.getId());
            List<User> userList = userDao.listUserByIdList(userIdList);
            return userList;
        } catch (Exception e) {
            logger.error("listUserByUserType failed", e);
            throw new SystemException("STATION_ERROR_400", "系统异常");
        }
    }

    /**
     * 站点管理员获取该站的所有配送员
     * 
     * @param context
     * @return
     */
    @Override
    public List<User> listCourierByStation(Context context) {
        try {
            // 权限检查，如果该用户不是站点管理员，返回
            if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
                return null;
            }
            return listUserByUserType(context, Constant.COURIER);
        } catch (Exception e) {
            logger.error("listCourierByStation failed", e);
            throw new SystemException("STATION_ERROR_002", "获取配送员失败");
        }
    }

    /**
     * 站点管理员获取该站的所有管理员
     * 
     * @param context
     * @return
     */
    @Override
    public List<User> getStationManagerByStation(Context context) {
        try {
            // 权限检查，如果该用户不是站点管理员，返回
            if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
                return null;
            }
            return listUserByUserType(context, Constant.STATIONMANAGER);
        } catch (Exception e) {
            logger.error("getStationManagerByStation failed", e);
            throw new SystemException("STATION_ERROR_403", "获取管理员失败");
        }
    }

    /**
     * 站点管理员增加配送员
     * 
     * @param context
     * @param stationId
     * @param mobile
     * @return
     * @throws UserException
     */
    @Override
    public User addCourierByStationIdAndCourierMobile(Context context, int stationId, long mobile,
            String certificateNumber, String name) throws UserException {

        int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);

        // 权限检查：只有该站点的管理员才可以增加配送人员
        if (!permitionValidateService.hasStationRole(context, stationId, stationManagerRoleId)) {
            throw new UserException("STATION_ERROR_003", "没有增加配送员的权限");
        }
        int courierRoleId = roleDao.getRoleIdByRoleName(Constant.COURIER);

        User user = userDao.getUserByMobileNoMatterStatus(mobile);

        // 允许配送员绑定至多个餐厅 - EDU1.0.6
        // if (user != null) { // 已经存在该用户
        // List<UserStationRole> courierRoleList = null;
        // UserStationRole courierUserStationRole = null;
        // try {
        // courierRoleList = userStationRoleDao.listUserStationRoleByUserIdAndRoleIdNoMatterStatus(user.getId(),
        // courierRoleId);
        // courierUserStationRole = userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(
        // user.getId(), stationId, courierRoleId);
        // } catch (Exception e) {
        // throw new SystemException("STATION_ERROR_007", "数据访问出错");
        // }
        //
        // if (courierRoleList != null && courierUserStationRole == null) { // 该用户已经是其他餐厅的配送员,不管该用户是否已经被软删除
        // throw new UserException("STATION_ERROR_004", "系统已经存在手机号为" + mobile + "的记录，请重新填写");
        // }
        // }
        try {
            user = userDao.getUserByMobileNoMatterStatus(mobile);

            if (user != null) { // 数据库user表已经物理存在这个人
                UserStationRole userStationRole = userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(
                        user.getId(), stationId, courierRoleId);
                if (user.getStatus() == 0 && userStationRole != null && userStationRole.getStatus() != 0) {
                    logger.error("用户status为0，但是userStationRole的status却不为0"); // 当前是错误状态，做bug检测，后面代码会修复
                }

                // 已经存该配送员
                if (userStationRole != null && userStationRole.getStatus() == 1) {
                    throw new UserException("STATION_ERROR_004", "系统已经存在手机号为" + mobile + "的记录，请重新填写");
                }

                // 若人员为任一餐厅的管理员，不可将其添加为其他餐厅的配送员
                List<UserStationRole> allUserStationRole = userStationRoleDao
                        .listUserStationRoleByUserIdAndRoleIdWithStatus(user.getId(), stationManagerRoleId);
                if (allUserStationRole != null) {
                    for (UserStationRole oneStationRole : allUserStationRole) {
                        if (oneStationRole.getStation_id() != stationId) {
                            throw new UserException("STATION_ERROR_362", "添加失败，该配送员为另一餐厅经理！");
                        }
                    }
                }

                // 配送员最多绑定至3家餐厅（包括已停用该配送员的餐厅），给出提示 - EDU1.0.6
                List<UserStationRole> courierAllRestaurant = userStationRoleDao
                        .listUserStationRoleByUserIdAndRoldIdNoMatterStatus(user.getId(), courierRoleId);
                int bindSize = 0;
                for (UserStationRole oneTaker : courierAllRestaurant) {
                    if (oneTaker.getStatus() == 0 || oneTaker.getStatus() == 1) {
                        // 并非试图恢复该配送员在某站点的身份而是新增配送员，将记数加1
                        if (oneTaker.getStation_id() != stationId) {
                            bindSize++;
                        }
                    }
                }
                if (bindSize >= 3) {
                    throw new UserException("STATION_ERROR_363", "添加失败，该配送员已经绑定3个餐厅！");
                }

                // 若在该餐厅有重名配送员（包括已停用该配送员的餐厅），给出提示 - EDU1.0.6
                String newCourierName = name;
                List<UserStationRole> courierInStation = userStationRoleDao
                        .listUserStationRoleByRoldIdAndStationIdNoMatterStatus(courierRoleId, stationId);
                List<Integer> courierUserIdList = new ArrayList<>();

                for (UserStationRole courierItem : courierInStation) {
                    int courierUserId = courierItem.getUser_id();
                    // 包括活跃配送员和停用配送员
                    if ((courierUserId != 0 && courierItem.getStatus() == 1)
                            || (courierUserId != 0 && courierItem.getStatus() == 0)) {
                        courierUserIdList.add(courierUserId);
                    }
                }

                if (courierUserIdList.size() != 0) {
                    List<User> courierUserList = userDao.listUserByIdList(courierUserIdList);
                    for (User courierUserItem : courierUserList) {
                        // 停用时参数name传入为""，在该餐厅可能有其它的配送员名字也为""，防止此种情况出现
                        if (newCourierName.equals(courierUserItem.getName())
                                && !StringUtils.isEmpty(courierUserItem.getName().trim())) {
                            throw new UserException("STATION_ERROR_365", "该餐厅已存在姓名为" + newCourierName + "的配送员，请重新填写");
                        }
                    }
                }
                // 寻找配送员所有已经绑定的餐厅，不允许绑定跨城市的餐厅（包括已停用该配送员的餐厅），给出提示 - EDU1.0.6
                List<UserStationRole> courierAllStation = userStationRoleDao
                        .listUserStationRoleByUserIdAndRoldIdNoMatterStatus(user.getId(), courierRoleId);

                Station currentStation = stationDao.getStationByPk(stationId);
                int currentCityId = currentStation.getCity_id();

                for (UserStationRole courierStationItem : courierAllStation) {
                    int stationIdItem = courierStationItem.getStation_id();
                    if ((stationIdItem != 0 && courierStationItem.getStatus() == 0)
                            || (stationIdItem != 0 && courierStationItem.getStatus() == 1)) {
                        Station stationItem = stationDao.getStationByPk(stationIdItem);
                        if (stationItem.getCity_id() != currentCityId) {
                            throw new UserException("STATION_ERROR_364", "该配送员已绑定其他城市的餐厅");
                        }
                    }
                }

                // 这个人是该站点的配送员
                if (user.getStatus() == 0 && userStationRole != null) {
                    user.setStatus(1);
                    userDao.update(user); // 从软删除中恢复
                }

                // 曾经被该餐厅暂时移除
                if (userStationRole != null && userStationRole.getStatus() == 0) {
                    userStationRole.setStatus(1);
                    userStationRoleDao.update(userStationRole);
                }

                // 曾经被该餐厅彻底删除
                if (userStationRole != null && userStationRole.getStatus() == -1) {
                    userStationRole.setStatus(1);
                    userStationRoleDao.update(userStationRole);
                }

                if (userStationRole == null) { // 这个人还不是该站点的配送员
                    Date date = new Date();
                    Timestamp timeStamp = new Timestamp(date.getTime());
                    // 创建UserStationRole
                    userStationRole = new UserStationRole();
                    userStationRole.setUser_id(user.getId());
                    userStationRole.setRole_id(courierRoleId);
                    userStationRole.setStation_id(stationId);
                    userStationRole.setStatus(1);
                    userStationRole.setCreated_at(timeStamp);
                    userStationRole.setUpdated_at(timeStamp);
                    userStationRoleDao.insert(userStationRole);
                }

                // 最后只在非恢复（即新增）配送员时更新user表中的名字
                if (!StringUtils.isEmpty(newCourierName.trim())) {
                    user.setName(newCourierName);
                    userDao.update(user);
                }

                return user;
            } else {
                // 数据库user表物理上不存在这个人
                Date date = new Date();
                Timestamp timeStamp = new Timestamp(date.getTime());
                User courier = new User();
                courier.setName("");
                courier.setEmail("");
                courier.setOnline(1);
                courier.setStatus(1);
                courier.setMobile(mobile);
                courier.setCertificate_number("");
                courier.setLatitude(new BigDecimal(0));
                courier.setLongitude(new BigDecimal(0));
                courier.setCertificate_number(certificateNumber);
                // 可能会重名
                List<User> users = listUserByStaionIdAndRoleId(context, stationId, courierRoleId);
                for (User exitUser : users) {
                    if (exitUser.getName().equals(name.trim()) && !StringUtils.isEmpty(name.trim())) {
                        throw new UserException("STATION_ERROR_005", "该餐厅已存在姓名为" + name.trim() + "的人员，请重新填写");
                    }
                }
                courier.setName(name);
                courier.setCreated_at(timeStamp);
                courier.setUpdated_at(timeStamp);
                userDao.addUser(courier);
                // 插入数据库以后，要重新查询，以获取userId
                courier = userDao.getUserByMobile(mobile);
                UserStationRole userStationRole = new UserStationRole();

                userStationRole.setUser_id(courier.getId());
                userStationRole.setRole_id(courierRoleId);
                userStationRole.setStation_id(stationId);
                userStationRole.setStatus(1);
                userStationRole.setCreated_at(timeStamp);
                userStationRole.setUpdated_at(timeStamp);
                userStationRoleDao.insert(userStationRole);
                return courier;
            }
        } catch (Exception e) {
            if (e instanceof UserException) {
                throw (UserException) e;
            }
            logger.error("addCourierByStationIdAndCourierMobile failed", e);
            throw new SystemException("STATION_ERROR_005", "添加配送员失败");
        }
    }

    /**
     * 站点管理员移除配送员
     * 
     * @param context
     * @param stationId
     * @param courierId
     * @return
     */
    @Override
    public User removeCourierByStationIdAndCourierId(Context context, int stationId, int courierId) {

        try {
            int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
            // 权限检查：只有该站点的管理员才可以移除配送人员
            if (!permitionValidateService.hasStationRole(context, stationId, stationManagerRoleId)) {
                return null;
            }
            if (context.getUser().getId() == courierId) {
                throw new UserException("STATION_ERROR_006", "管理员不能禁用自己");
            }
            int stationCourierRoleId = roleDao.getRoleIdByRoleName(Constant.COURIER);
            UserStationRole userStationRole = userStationRoleDao
                    .getUserStationRoleByUserIdAndStationIdAndRoleIdWithStatus(courierId, stationId,
                            stationCourierRoleId);
            // 权限检查：只有courierId是是stationId站点的配送员，站点stationId的管理员才可以移除他
            if (userStationRole == null) {
                // UserStationRole表没有这条记录，说明数据库的数据是异常的
                return null;
            }
            if (userStationRole.getStatus() == -1) {
                throw new UserException("STATION_ERROR_006", "该配送员已经被永久删除了");
            }
            userStationRole.setStatus(0); // 软删除
            userStationRoleDao.update(userStationRole);
            List<UserStationRole> userStationRoleList = userStationRoleDao.listUserStationRoleByUserId(courierId);
            boolean deleteUser = true;
            for (UserStationRole uSR : userStationRoleList) {
                if (uSR.getStatus() != 0) {
                    deleteUser = false; // 如果该配送员的其他角色还存在，不需要从把该配送员从User表进行软删除
                    break;
                }
            }
            // 如果该配送员在UserStationRole表，所有的角色状态都是0，那么需要在User表把该配送员的status设置为0
            // EDU 1.0.6 - 影响更新后的状态显示
            // if (deleteUser) {
            // User courierUser = userDao.getUserById(courierId);
            // courierUser.setStatus(0);
            // userDao.update(courierUser);
            // }
            return userDao.getUserByIdNoMatterStatus(userStationRole.getUser_id());
        } catch (Exception e) {
            logger.error("removeCourierByStationIdANdCourierId failed", e);
            throw new SystemException("STATION_ERROR_006", "移除配送员失败");
        }
    }

    @Override
    public User updateCourier(Context context, long mobile, String takerName, String certificateNumber)
            throws UserException {
        User user = validateCanUpdateCourier(context, mobile, takerName);

        if (user != null) {
            user.setCertificate_number(certificateNumber);
            user.setName(takerName);
            User userInDb = userDao.getUserByIdNoMatterStatus(user.getId());
            user.setStatus(userInDb.getStatus());
            userDao.update(user);
            // 该配送员在该站点已被停用，前端希望看到status为0
            UserStationRole userStationRole = userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(
                    user.getId(), ContextUtil.getMyStationIdByContext(context), 2);
            if (userStationRole.getStatus() == 0) {
                user.setStatus(0);
            }
            return user;
        } else {
            throw new UserException("STATION_ERROR_366", "您无权限");
        }

    }

    private User validateCanUpdateCourier(Context context, long mobile, String takerName) throws UserException {

        if (context.getUser().getMobile() == mobile) {
            return context.getUser();
        }

        List<User> users = listUserByStaionIdAndRoleId(context, ContextUtil.getMyStationIdByContext(context), 2);

        if (CollectionUtils.isEmpty(users)) {
            return null;
        }
        User courier = null;
        for (User user : users) {
            if (mobile == user.getMobile()) {
                courier = user;
                break;
            }
        }
        if (courier != null) {
            for (User user : users) {
                if (takerName.equals(user.getName()) && courier.getMobile() != user.getMobile()
                        && !StringUtils.isEmpty(takerName.trim())) {
                    throw new UserException("STATION_ERROR_365", "该餐厅已存在姓名为" + takerName + "的配送员，请重新编辑");
                }
            }
        }
        return courier;
    }

    @Override
    public User deleteCourierByStationIdAndCourierId(Context context, int stationId, int courierId)
            throws UserException {
        int stationManagerRoleId = roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER);
        // 权限检查：只有该站点的管理员才可以移除配送人员
        if (!permitionValidateService.hasStationRole(context, stationId, stationManagerRoleId)) {
            return null;
        }
        UserStationRole userStationRole = validateCanDeleteCourierThoroughly(courierId, stationId);
        User courierUser = userDao.getUserByIdNoMatterStatus(courierId);
        // 管理员想彻底移除自己，这个时候只用动user_station_role表，不用动user表
        if (context.getUser().getId() == courierId) {
            throw new UserException("STATION_ERROR_006", "站点管理员不能彻底删除自己");
            // userStationRole.setStatus(-1);
            // userStationRoleDao.update(userStationRole);
            // return courierUser;
        }
        // 删除的是自己手下的配送员，则需要操作三张表，一张user_station_role、user、user_delete_log表
        else {
            try {
                boolean result = deleteCourierThoroughly(courierUser, userStationRole);
            } catch (Exception e) {
                logger.error("removeCourierByStationIdAndCourierId failed", e);
                throw new SystemException("STATION_ERROR_006", "彻底配送员失败");
            }
        }
        return courierUser;

    }

    @Transactional
    protected boolean deleteCourierThoroughly(User user, UserStationRole userStationRole) throws UserException {
        userStationRole.setStatus(-1);
        userStationRoleDao.update(userStationRole);
        List<UserDeleteLog> userDeleteLogs = userDeleteLogDao.getUserDeleteLogByMobile(user.getMobile());
        UserDeleteLog userDeleteLog = new UserDeleteLog();
        userDeleteLog.setOrigin_user_id(user.getId());
        userDeleteLog.setOrigin_mobile(user.getMobile());
        int prefixNumber = 0;

        int inUserId = user.getId();
        String inUserName = user.getName();
        int inStationId = userStationRole.getStation_id();

        // EDU1.0.6开始允许一个配送员绑定在多个餐厅上
        // 在删除时需要进行判断，若该配送员未被绑定至其他餐厅，从user表中将其软删除，否则不更新user表
        // 配送员在其它餐厅处于停用状态，也不用在user表中对其软删除
        int courierRoleId = roleDao.getRoleIdByRoleName(Constant.COURIER);
        List<UserStationRole> userStationRoleList = userStationRoleDao
                .listUserStationRoleByUserIdAndRoldIdNoMatterStatus(user.getId(), courierRoleId);

        boolean bindToSingleStation = true;
        int currentStationId = userStationRole.getStation_id();
        for (UserStationRole uSRole : userStationRoleList) {
            if ((uSRole.getStation_id() != currentStationId && uSRole.getStatus() == 0) || // 在其他餐厅处于停用状态
                    (uSRole.getStation_id() != currentStationId && uSRole.getStatus() == 1)) {// 在其它餐厅处于活跃状态
                bindToSingleStation = false;
                break;
            }
        }

        if (bindToSingleStation) {
            // 该配送员从来没被删除过
            if (CollectionUtils.isEmpty(userDeleteLogs)) {
                prefixNumber = Integer.valueOf(PREFIX_NUMBER);
                String stringMobile = PREFIX_NUMBER + user.getMobile();
                user.setMobile(Long.valueOf(stringMobile));
                String userName = PREFIX_NUMBER + user.getName();
                user.setName(userName);
                user.setStatus(0);
            }
            // 已经被删除过了
            else {
                prefixNumber = Integer.valueOf(userDeleteLogs.get(0).getPrefix_number()) + 1;
                user.setName(prefixNumber + user.getName());
                String stringMobile = String.valueOf(prefixNumber) + user.getMobile();
                user.setMobile(Long.valueOf(stringMobile));
                user.setStatus(0);
            }
            userDeleteLog.setPrefix_number(prefixNumber);
            userDeleteLog.setCreated_at(new Timestamp(System.currentTimeMillis()));
            userDeleteLogDao.insert(userDeleteLog);
            userDao.update(user);
        } else {
            writeToSettlementForDeletedMan(inUserId, inUserName, inStationId);
            userDeviceDao.setUserAllTokenExpired(inUserId);
        }
        return true;
    }

    private UserStationRole validateCanDeleteCourierThoroughly(int userId, int stationId) throws UserException {
        UserStationRole userStationRole = userStationRoleDao.getUserStationRoleByUserIdAndStationIdAndRoleId(userId,
                stationId, Constant.COURIER_CODE);
        if (userStationRole == null || userStationRole.getStatus() == 1 || userStationRole.getStatus() == -1) {
            throw new UserException("STATION_ERROR_006", "该人已被彻底删除，或者不在您的站点下，请清除缓存后再试");
        }
        return userStationRole;
    }

    @Override
    public List<RetailerWithOrderInfo> getRetailerWithOrderInfoListByDelivererId(Context context, int delivererId,
            Integer status) {
        // List<Restaurant> restaurants = restaurantDao.getRetailerListByDelivererId(delivererId);
        List<Restaurant> restaurants = new ArrayList<>();
        Set<Integer> stationIds = context.getStationID();
        Map<Integer, Integer> pkRstIdAndStationId = new HashMap<Integer, Integer>();
        for (Integer stationId : stationIds) {
            int restaurantId = stationRestaurantDao.getRestaurantIdByStaionId(stationId);
            restaurants.add(restaurantDao.getRestaurantByPk(restaurantId));
            pkRstIdAndStationId.put(restaurantId, stationId);
        }
        List<RetailerWithOrderInfo> rs = new ArrayList<>();
        if (restaurants != null) {
            for (Restaurant rst : restaurants) {
                RetailerWithOrderInfo rwoi = new RetailerWithOrderInfo();
                rwoi.setRst_id(rst.getEle_id());
                rwoi.setRst_name(rst.getName());
                int stationID = pkRstIdAndStationId.get(rst.getId());
                for (UserStationRole userStationRole : context.getUserStationRoles()) {
                    if (userStationRole.getStation_id() == stationID) {
                        rwoi.setBinded_time(userStationRole.getCreated_at());
                    }
                }
                List<DeliveryOrder> orders = deliveryOrderDao.getDeliveryOrdersByRstId(rst.getEle_id(), status);
                filterDeliveryOrder(orders);
                rwoi.setOrder_counts(orders.size());
                rs.add(rwoi);
            }
        }
        Collections.sort(rs);
        return rs;
    }

    @Override
    public void addDelivererToRetailer(Context context, int deliverId, int retailerId) throws SystemException {
        User deliverer = userDao.getUserById(deliverId);
        Restaurant retailer = restaurantDao.getRestaurantByPk(retailerId);

        if (deliverer == null) {
            throw new SystemException("STATION_ERROR_007", "数据访问错误, 找不到配送员，配送员ID:" + deliverId);
        }

        if (retailer == null) {
            throw new SystemException("STATION_ERROR_007", "数据访问错误, 找不到商家，商家ID:" + retailerId);
        }

        EBRetailerDelivererMapping rdm = new EBRetailerDelivererMapping();
        rdm.setDeliverer_id(deliverer.getId());
        rdm.setDeliverer_name(deliverer.getName());
        rdm.setDeliverer_mobile(deliverer.getMobile());
        rdm.setRetailer_id(retailer.getId());
        rdm.setRetailer_name(retailer.getName());
        rdm.setCreated_at(new Timestamp(System.currentTimeMillis()));
        rdm.setUpdated_at(new Timestamp(System.currentTimeMillis()));
        rdm.setIs_active(1);

        // retailerDelivererMappingDao.insert(rdm);
    }

    /**
     * 做六个小时时间限制
     * 
     * @param allDeliveryOrders
     */
    public void filterDeliveryOrder(List<DeliveryOrder> allDeliveryOrders) {
        int expiredHours = 3;
        if (CollectionUtils.isEmpty(allDeliveryOrders)) {
            return;
        }
        Iterator<DeliveryOrder> iterator = allDeliveryOrders.iterator();
        while (iterator.hasNext()) {
            DeliveryOrder deliveryOrder = iterator.next();
            // 普通订单
            if (deliveryOrder.getBooked_time() == null) {
                Timestamp eleCreatedTime = deliveryOrder.getEle_created_time();
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 下单超过六个小时,删除该单
                if (now.getTime() - eleCreatedTime.getTime() > expiredHours * 60 * 60 * 1000l) {
                    iterator.remove();
                }
            }
            // 预订单
            else {
                Timestamp bookedTime = deliveryOrder.getBooked_time();
                Timestamp now = new Timestamp(System.currentTimeMillis());
                // 下单超过六个小时,删除该单
                if ((now.getTime() - bookedTime.getTime() > expiredHours * 60 * 60 * 1000l && now.getTime()
                        - bookedTime.getTime() > 0)
                        || (bookedTime.getTime() - now.getTime() > 0 && bookedTime.getTime() - now.getTime() > 1.5 * 60 * 60 * 1000l)) {
                    iterator.remove();
                }
            }
        }

    }

    /**
     * 彻底删除结算初始化记录 以前的结算纪录status全部致为－1
     * 
     * @param userId
     * @param userName
     * @param stationId
     * @throws UserException
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void writeToSettlementForDeletedMan(int userId, String userName, int stationId) throws UserException {
        try {
            Date date = new Date();
            Timestamp timeStamp = new Timestamp(date.getTime());
            Settlement settlement = new Settlement(userId, userName, userId, userName, stationId, 0, BigDecimal.ZERO,
                    0, BigDecimal.ZERO, 0, BigDecimal.ZERO, timeStamp, timeStamp, -1);
            settlementDao.addSettlement(settlement);
            SettlementDeliveryOrder settlementDeliveryOrder = new SettlementDeliveryOrder(settlement.getId(), userId,
                    "", -1, timeStamp, timeStamp);
            settlementDeliveryOrderDao.addSettlementDelivery(settlementDeliveryOrder);
            settlementDao.updateSettlementForDeletedMan(stationId, userId, timeStamp);
        } catch (Exception e) {
            throw ExceptionFactory.newUserException(ExceptionCode.SETTLEMENT_ERROR_133.getCode(), "删除配送员结算初始化异常");
        }
    }
}
