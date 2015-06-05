/**
 * 
 */
package me.ele.talaris.webapi.station;

import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.dao.RoleDao;
import me.ele.talaris.dao.StationDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.User;
import me.ele.talaris.service.permission.IPermissionService;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.user.IUserService;
import me.ele.talaris.utils.Utils;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author chaoguodeng
 *
 */
@Controller
public class StationController extends WebAPIBaseController {
    @Autowired
    IPermissionService permitionValidateService;
    @Autowired
    IStationService stationService;
    @Autowired
    IUserService userService;
    @Autowired
    RoleDao roleDao;
    @Autowired
    StationDao stationDao;
    @Autowired
    IHermesService coffeeHermesService;

    private final static Logger logger = LoggerFactory.getLogger(StationController.class);

    /**
     * 创建站点
     * 
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/webapi/station/")
    @InterfaceMonitor(interfaceName = "创建站点")
    public @ResponseBody ResponseEntity<Station> createStation(@RequestParam("mobile") String mobile) {
        ResponseEntity<Station> responseEntity = null;
        logger.info("创建站点");
        User user = userService.getUserByMobile(mobile);
        Station station = stationService.createStationByUser(user);
        if (station != null) {
            responseEntity = new ResponseEntity<Station>("200", "", station);
        } else {
            responseEntity = new ResponseEntity<Station>("STATION_ERROR_320", "创建站点失败，请重试", null);
        }
        return responseEntity;
    }

    /**
     * 查看站点
     * 
     * @param context
     * @param station_id
     * @return
     */
    @RequestMapping(value = "/webapi/station/{station_id}")
    @InterfaceMonitor(interfaceName = "查看站点", contextIndex = 0)
    public @ResponseBody ResponseEntity<Station>
            viewStation(Context context, @PathVariable("station_id") int station_id) {
        ResponseEntity<Station> responseEntity = null;
        logger.info("查看站点");
        // 权限检查，context的用户必须是station的管理员才可以查看该station
        if (!permitionValidateService.hasStationRole(context, station_id, Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<Station>("STATION_ERROR_330", "您无权限查看对应站点", null);
            return responseEntity;
        }
        Station station = stationService.getStationByStationId(context, station_id);
        responseEntity = new ResponseEntity<Station>("200", "", station);
        return responseEntity;
    }

    /**
     * 查看用户
     * 
     * @param context
     * @param station_id
     * @param role_id
     * @return
     */
    @RequestMapping(value = "/webapi/station/{station_id}/user/")
    @InterfaceMonitor(interfaceName = "查看站点人员", contextIndex = 0)
    public @ResponseBody ResponseEntity<List<User>> viewUsers(Context context, 
            @PathVariable(value = "station_id") int station_id, @RequestParam("role_id") int role_id) {
        ResponseEntity<List<User>> responseEntity = null;
        logger.info("查看用户, userId:{}, station_id:{}", context.getUser().getId(), station_id);
        // 权限检查，context的用户必须是station的管理员才可以查看该station的属于roleId的所有人员
        if (!permitionValidateService.hasStationRole(context, station_id,
                roleDao.getRoleIdByRoleName(Constant.STATIONMANAGER))) {
            responseEntity = new ResponseEntity<List<User>>("STATION_ERROR_340", "找不到对应的人员信息", null);
            return responseEntity;
        }
        List<User> userList = stationService.listUserByStaionIdAndRoleId(context, station_id, role_id);
        responseEntity = new ResponseEntity<List<User>>("200", "", userList);
        return responseEntity;
    }

    /**
     * 增加配送员 To-Do: 年后增加10个人限制的需求
     * 
     * @param context
     * @param station_id
     * @param mobile
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "/webapi/station/add_courier/")
    @InterfaceMonitor(interfaceName = "增加配送员", contextIndex = 0)
    public @ResponseBody ResponseEntity<User> addCourier(Context context, @RequestParam("station_id") int station_id,
            @RequestParam("mobile") long mobile, @RequestParam(value = "certificate_number", required = false,
                    defaultValue = "") String certificateNumber, @RequestParam(value = "taker_name", required = false,
                    defaultValue = "") String name) throws UserException {
        ResponseEntity<User> responseEntity = null;
        logger.info("增加配送员");
        // 权限检查，只有context的用户是station的管理员才可以给station增加配送员
        if (!permitionValidateService.hasStationRole(context, station_id, Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<User>("STATION_ERROR_351", "添加失败，系统异常", null);
            return responseEntity;
        }
        User courier = stationService.addCourierByStationIdAndCourierMobile(context, station_id, mobile,
                certificateNumber, name);
        responseEntity = new ResponseEntity<User>("200", "", courier);
        Station station = stationDao.getStationByPk(station_id);
        if (station != null) {
            String stationName = station.getName();
            long result = coffeeHermesService.sendAppDownloadUrlMessage(String.valueOf(mobile), stationName,
                    Constant.APPDOWNLOADURL);
        }
        return responseEntity;
    }

    /**
     * 移除配送员
     * 
     * @param context
     * @param station_id
     * @param user_id
     * @return
     */
    @RequestMapping(value = "/webapi/station/remove_courier/")
    @InterfaceMonitor(interfaceName = "移除配送人员", contextIndex = 0)
    public @ResponseBody ResponseEntity removeCourier(Context context, @RequestParam("station_id") int station_id,
            @RequestParam("user_id") int user_id) {
        ResponseEntity<User> responseEntity = null;
        logger.info("移除配送员");
        // 权限检查，只有context的用户是station的管理员才可以给station移除配送员
        if (!permitionValidateService.hasStationRole(context, station_id, Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<User>("STATION_ERROR_360", "系统异常，移除人员失败", null);
            return responseEntity;
        }

        User removedCourier = stationService.removeCourierByStationIdAndCourierId(context, station_id, user_id);
        responseEntity = new ResponseEntity<User>("200", "", removedCourier);
        return responseEntity;
    }

    /**
     * 彻底删除配送员
     * 
     * @param context
     * @param station_id
     * @param user_id
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "/webapi/station/delete_courier/")
    @InterfaceMonitor(interfaceName = "彻底删除配送人员", contextIndex = 0)
    public @ResponseBody ResponseEntity deleteCourier(Context context, @RequestParam("station_id") int station_id,
            @RequestParam("user_id") int user_id) throws UserException {
        ResponseEntity<User> responseEntity = null;
        logger.info("彻底删除配送员");
        // 权限检查，只有context的用户是station的管理员才可以给station移除配送员
        if (!permitionValidateService.hasStationRole(context, station_id, Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<User>("STATION_ERROR_360", "系统异常，删除人员失败", null);
            return responseEntity;
        }

        User removedCourier = stationService.deleteCourierByStationIdAndCourierId(context, station_id, user_id);
        responseEntity = new ResponseEntity<User>("200", "", removedCourier);
        return responseEntity;
    }

    /**
     * 
     * @param context
     * @param station_id
     * @param user_id
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "/webapi/station/update_courier")
    @InterfaceMonitor(interfaceName = "更新配送人员信息", contextIndex = 0)
    public @ResponseBody ResponseEntity<User> updateCourier(Context context, @RequestParam("mobile") long mobile,
            @RequestParam("taker_name") String taker_name, @RequestParam(value = "certificate_number",
                    required = false, defaultValue = "") String certificateNumber) throws UserException {
        ResponseEntity<User> responseEntity = null;
        if (!Utils.isChineseCharacter(taker_name)) {
            return new ResponseEntity<User>("STATION_ERROR_361", "系统异常", null);
        }
        logger.info("更新配送员");
        // 权限检查，只有context的用户是station的管理员才可以给station移除配送员
        if (!permitionValidateService.hasRole(context, Constant.STATIONMANAGER)) {
            responseEntity = new ResponseEntity<User>("STATION_ERROR_366", "您没有权限", null);
            return responseEntity;
        }
        User updatedCourier = stationService.updateCourier(context, mobile, taker_name, certificateNumber);
        responseEntity = new ResponseEntity<User>("200", "", updatedCourier);
        return responseEntity;
    }
}
