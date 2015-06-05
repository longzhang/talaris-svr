/**
 * 
 */
package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.Date;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.UserDevice;
import me.ele.talaris.utils.Times;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author shaorongfei
 *
 */
public class UserDeviceDao extends BaseSpringDao<UserDevice> {
    private final static Logger logger = LoggerFactory.getLogger(UserDeviceDao.class);

    public UserDeviceDao() {
        super(new BeanPropertyRowMapper<UserDevice>(UserDevice.class));
    }

    public UserDevice getUserDeviceByPk(int pk) {
        return this.selectOneOrNull("where id = ?", pk);
    }

    /**
     * 通过user_id和device_type查询userDevice
     * 
     * @param userId
     * @param device_type
     * @return
     */
    public UserDevice getUserDeviceByUserIdAndDeviceType(int userId, int device_type) {
        return this.selectOneOrNull("where user_id = ? and device_type = ? ", userId, device_type);
    }

    public UserDevice getUserDeviceByDeviceId(int deviceId) {
        return this.selectOneOrNull("where device_id = ?", deviceId);
    }

    public int addUserDevice(UserDevice userDevice) {
        return this.insert(userDevice);
    }

    public int updateUserDevice(UserDevice userDevice) {
        return this.update(userDevice);
    }

    public UserDevice getUserDeviceByToken(String token) {
        return this.selectOneOrNull("where access_token = ? and is_valid = 1", token);
    }

    public int getUserIdByToken(String token) {
        UserDevice userDevice = this.getUserDeviceByToken(token);
        if (userDevice == null) {
            return 0;
        }
        return userDevice.getUser_id();
    }

    public int getUserIdByToke(String token) {
        UserDevice userDevice = this.selectOneOrNull("where access_token = ? and is_valid = 1", token);
        if (userDevice == null) {
            return 0;
        }

        Date date = new Date();
        Timestamp currentTimeStamp = new Timestamp(date.getTime());
        Timestamp expire_at = userDevice.getExpire_at();
        if (currentTimeStamp.before(expire_at)) {
            return userDevice.getUser_id();
        } else {
            return 0;
        }
    }

    public String getDeviceIdByToke(String token) {
        UserDevice userDevice = this.selectOneOrNull("where access_token = ?", token);
        if (userDevice == null) {
            return null;
        }
        return userDevice.getDevice_id();
    }

    /**
     * 同一类型设备登录顶替上次登录
     * 
     * user_id、device_type、is_valid能唯一确定登陆情况
     * 
     * @param newToken
     * @param userId
     * @param deviceType
     * @param time
     * @return
     */
    public int updateTokenAndIsValid(String newToken, int userId, int deviceType, Timestamp time, Timestamp expireTime) {
        return this.jdbcTemplate
                .update("update user_device set is_valid = 1, access_token = ?,updated_at = ?,expire_at = ? where user_id = ? and device_type = ? ",
                        newToken, time, expireTime, userId, deviceType);
    }

    /**
     * 顶登陆情况下更新其它deviceType为非登陆状态
     * 
     * @param deviceType
     * @return
     */
    public int updateOtherDeviceTypeIsValid(int deviceType, int userId, Timestamp time, Timestamp expireTime) {
        int result = 0;
        if (deviceType == 0 || deviceType == 1 || deviceType == 2) {
            result = this.jdbcTemplate
                    .update("update user_device set is_valid = 0,updated_at = ?,expire_at = ? where user_id = ? and device_type != 3 and device_type != ? ",
                            time, expireTime, userId, deviceType);
        }
        return result;
    }

    /**
     * 更新isValid登录 并更新过期时间
     * 
     * @param token
     * @return
     */
    // public int updateIsValidByToken(String token) {
    // Date date = new Date();
    // Timestamp timeStamp = new Timestamp(date.getTime());
    // return this.jdbcTemplate.update(
    // "update user_device set is_valid = 1,updated_at = ?,expire_at = ? where access_token = ?",
    // timeStamp,
    // Times.timestampPlusMiniuts(timeStamp, 1000), token);
    // }

    // 软删除
    public int deleteUserDeviceByPk(int pk) {
        return this.jdbcTemplate.update("update user_device set is_valid = 0 where id = ? ", pk);
    }

    /**
     * 更新用户登录状态
     * 
     * @param userId
     * @return
     */
    public int updateUserDeviceIsValidByUserId(int userId) {
        return this.jdbcTemplate.update("update user_device set is_valid = 0  where user_id = ? ", userId);
    }

    public int deleteUserDeviceByDeviceId(int deviceId) {
        return this.jdbcTemplate.update("update user_device set is_valid = 0  where device_id = ? ", deviceId);
    }

    /**
     * 登出接口调用
     * 
     * @param token
     * @return
     */
    public int logoutUpdateByToken(String token) {
        return this.jdbcTemplate.update("update user_device set is_valid = 0  where access_token = ?", token);
    }

    public void setUserAllTokenExpired(int userId) {
        this.jdbcTemplate.update("update user_device set updated_at =? , expire_at = ? where user_id = ?",
                new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), userId);
    }

}
