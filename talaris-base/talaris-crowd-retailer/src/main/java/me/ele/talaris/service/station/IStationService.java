package me.ele.talaris.service.station;

import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.User;
import me.ele.talaris.service.station.dto.RetailerWithOrderInfo;

public interface IStationService {

	/**
	 * 第一版：餐厅老板就是站点管理员 创建站点: 现在的应用场景是，餐厅老板（即站点管理员）第一次登录时候，自动创建站点
	 * 到了创建站点这一步，正常情况下，站点所对应的餐厅应该已经存在本地数据库
	 * 
	 * @param user
	 * @return
	 */
	public Station createStationByUser(User user);

	/**
	 * 第一版：一个餐厅老板只对应一个站点 查看currentUser的站点: 如果currentUser是直接返回currentUser的station
	 * 
	 * @param context
	 * @return
	 */
	public Station getStationByContext(Context context);

	/**
	 * 查看stationId的站点：只有该站点的管理员才可以查看该站点
	 * 
	 * @param context
	 * @param stationId
	 * @return
	 */
	public Station getStationByStationId(Context context, int stationId);

	/**
	 * 根据staionId和roleId查看对应的所有roleId人员
	 * 
	 * @param context
	 * @param stationId
	 * @param roleId
	 * @return
	 */
	public List<User> listUserByStaionIdAndRoleId(Context context,
			int stationId, int roleId);

	/**
	 * 根据用户类型(如，配送员，站点管理员)获取用户
	 * 
	 * @param context
	 * @param userType
	 * @return
	 */
	public List<User> listUserByUserType(Context context, String userType);

	/**
	 * 站点管理员获取该站的所有配送员
	 * 
	 * @param context
	 * @return
	 */
	public List<User> listCourierByStation(Context context);

	/**
	 * 站点管理员获取该站的所有管理员
	 * 
	 * @param context
	 * @return
	 */
	public List<User> getStationManagerByStation(Context context);

	/**
	 * 站点管理员增加配送员
	 * 
	 * @param context
	 * @param stationId
	 * @param mobile
	 * @return
	 * @throws UserException
	 */
	public User addCourierByStationIdAndCourierMobile(Context context,
			int stationId, long mobile, String certificateNumber, String name)
			throws UserException;

	/**
	 * 站点管理员移除配送员
	 * 
	 * @param context
	 * @param stationId
	 * @param courierId
	 * @return
	 */
	public User removeCourierByStationIdAndCourierId(Context context,
			int stationId, int courierId);

	public User updateCourier(Context context, long mobile, String takerName,
			String certificateNumber) throws UserException;

	public User deleteCourierByStationIdAndCourierId(Context context,
			int stationId, int courierId) throws UserException;

	public List<RetailerWithOrderInfo> getRetailerWithOrderInfoListByDelivererId(Context context, int delivererId, Integer status);

	public void addDelivererToRetailer(Context context, int deliverId, int retailerId) throws SystemException;

}