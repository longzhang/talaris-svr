package me.ele.talaris.napos.wrapper;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import me.ele.talaris.dao.AdminModifyRecordDao;
import me.ele.talaris.dao.CityDao;
import me.ele.talaris.dao.EleOrderDetailDao;
import me.ele.talaris.dao.RestaurantBindRecordDao;
import me.ele.talaris.dao.RestaurantDao;
import me.ele.talaris.dao.StationDao;
import me.ele.talaris.dao.StationRestaurantDao;
import me.ele.talaris.errorlog.processor.ErrorLogProcessor;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.hermes.service.IHermesService;
import me.ele.talaris.model.AdminModifyRecord;
import me.ele.talaris.model.EleOrderDetail;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.model.RestaurantBindRecord;
import me.ele.talaris.model.Station;
import me.ele.talaris.model.StationRestaurant;
import me.ele.talaris.model.User;
import me.ele.talaris.napos.model.TEleOrder;
import me.ele.talaris.napos.model.TRestaurant;
import me.ele.talaris.redis.RedisLock;
import me.ele.talaris.service.auth.ILoginService;
import me.ele.talaris.service.user.IUserService;
import me.ele.talaris.utils.HttpRequest;
import me.ele.talaris.utils.HttpResponseEntity;
import me.ele.talaris.utils.SerializeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import redis.clients.jedis.JedisPool;

/**
 * @author kimizhang
 *
 */

public class NaposServiceWrapper {

	@Autowired
	private RestaurantDao restaurantDao;
	@Autowired
	private StationRestaurantDao stationRestaurantDao;
	@Autowired
	private AdminModifyRecordDao adminModifyRecordDao;

	private EleOrderDetailDao eleOrderDetailDao;
	private CityDao cityDao;
	private StationDao stationDao;
	private RestaurantBindRecordDao restaurantBindRecordDao;

	public static final Logger logger = LoggerFactory.getLogger(NaposServiceWrapper.class);

	@Resource(name = "coffeeHermesService")
	private IHermesService coffeeHermesService;
	@Autowired
	private IUserService userService;
	@Autowired
	private ILoginService loginService;
	@Autowired
	JedisPool jedisPool;
	@Autowired
	HttpRequest httpClient;
	@Autowired
	ErrorLogProcessor errorLogProcessor;

	public void setEleOrderDetailDao(EleOrderDetailDao eleOrderDetailDao) {
		this.eleOrderDetailDao = eleOrderDetailDao;
	}

	public void setCityDao(CityDao cityDao) {
		this.cityDao = cityDao;
	}

	public void setStationDao(StationDao stationDao) {
		this.stationDao = stationDao;
	}

	public void setRestaurantBindRecordDao(RestaurantBindRecordDao restaurantBindRecordDao) {
		this.restaurantBindRecordDao = restaurantBindRecordDao;
	}

	private String getrestaurantbymobileurl;
	private String getrestaurantbyidurl;
	private String geteleorderbyidurl;
	private String geteleordersbyridurl;
	private String posteleorderstatusurl;

	public String getGetrestaurantbymobileurl() {
		return getrestaurantbymobileurl;
	}

	public void setGetrestaurantbymobileurl(String getrestaurantbymobileurl) {
		this.getrestaurantbymobileurl = getrestaurantbymobileurl;
	}

	public String getGetrestaurantbyidurl() {
		return getrestaurantbyidurl;
	}

	public void setGetrestaurantbyidurl(String getrestaurantbyidurl) {
		this.getrestaurantbyidurl = getrestaurantbyidurl;
	}

	public String getGeteleorderbyidurl() {
		return geteleorderbyidurl;
	}

	public void setGeteleorderbyidurl(String geteleorderbyidurl) {
		this.geteleorderbyidurl = geteleorderbyidurl;
	}

	public String getGeteleordersbyridurl() {
		return geteleordersbyridurl;
	}

	public void setGeteleordersbyridurl(String geteleordersbyridurl) {
		this.geteleordersbyridurl = geteleordersbyridurl;
	}

	private String getRequestRestaurantByMobileUrl(String mobile) {
		return this.getrestaurantbymobileurl + mobile;
	}

	private String getRequestRestaurantByRestaurantIdUrl(int restaurantId) {
		return this.getrestaurantbyidurl + restaurantId;
	}

	private String getRequestTEleOrderByTEleOrderIdUrl(long eleOrderId) {
		return this.geteleorderbyidurl + eleOrderId;
	}

	private String getRequsetTEleOrderListByRestaurantId(int restaurantId) {
		return this.geteleordersbyridurl + restaurantId;
	}

	public String getPosteleorderstatusurl() {
		return posteleorderstatusurl;
	}

	public void setPosteleorderstatusurl(String posteleorderstatusurl) {
		this.posteleorderstatusurl = posteleorderstatusurl;
	}

	private Restaurant tRestaurantToRestaurant(TRestaurant tRestaurant) {
		Restaurant result = new Restaurant();
		result.setEle_id(tRestaurant.getId());
		result.setName(tRestaurant.getName());
		if (tRestaurant.getPhones() != null && tRestaurant.getPhones().size() > 0) {
			result.setPhone(tRestaurant.getPhones().get(0));
		}
		result.setAddress(tRestaurant.getAddress());
		if (tRestaurant.getCity() != null) {
			try {
				logger.info("select from city use area_code:{}", tRestaurant.getCity().getCode());
				int cityId = cityDao.getCityIdByAreaCode(tRestaurant.getCity().getCode());
				result.setCity_id(cityId);
			} catch (Exception e) {
				logger.error("select from city failed, tmply set city_id(0)", e);
				result.setCity_id(0);
			}
		}
		if (tRestaurant.getGeoLocation() != null) {
			result.setLongitude(tRestaurant.getGeoLocation().getLongitude());
			result.setLatitude(tRestaurant.getGeoLocation().getLatitude());
		}
		result.setStatus(1);
		return result;
	}

	private void insertEleOrderDetail(String jResult, long eleOrderId) {
		try {
			if (eleOrderDetailDao.getEleOrderByEleOrderId(eleOrderId) == null) {
				EleOrderDetail eleOrderDetail = new EleOrderDetail();
				eleOrderDetail.setEle_order_id(eleOrderId);
				eleOrderDetail.setEle_order_detail(jResult);
				eleOrderDetailDao.insert(eleOrderDetail);
			}
		} catch (Exception e) {
			logger.error("insert EleOrderDetail failed, eleOrderId:{}, eleOrderDetail:{}", eleOrderId, jResult, e);
			throw new SystemException("DATABASE_ERROR", "data base operation failed");
		}
	}

	public Restaurant requestGetRestaurantByMobile(String mobile) throws UserException {
		HttpResponseEntity hResult = null;
		try {
			String requestUrl = getRequestRestaurantByMobileUrl(mobile);
			hResult = httpClient.doRequest(requestUrl, null, null, "UTF-8");
		} catch (Exception e) {
			logger.error("napos connection failed, mobile:{}", mobile, e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
		int resPonseCode = hResult.getResponseCode();
		if (resPonseCode != 200) {
			logger.error("responseCode:{}, responseContent:{}", hResult.getResponseCode(), hResult.getResponseContent());
			if (resPonseCode == 404) {
				throw new UserException("AUTH_ERROR_500", "您的手机号码未绑定到餐厅");
			} else {
				if (resPonseCode >= 500) {
					coffeeHermesService.safeSendAlarm(String.format(
							"napos connection failed with code:%s in requestGetRestaurantByMobile(mobile:%s)",
							resPonseCode, mobile), -1);
				}
				throw new SystemException("NAPOS_ERROR_404", "系统异常");
			}
		}
		List<TRestaurant> tResult;
		try {
			String jResult = hResult.getResponseContent();
			tResult = SerializeUtil.getObjectsFromJson(jResult, TRestaurant.class);
		} catch (Exception e) {
			logger.error("serialize napos restaurant json failed, json:{}", hResult.getResponseContent(), e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
		if (tResult.size() > 1) {
			throw new UserException("AUTH_ERROR_503", "您的手机号目前绑定了多个餐厅，本系统暂时不支持");
		}
		if (CollectionUtils.isEmpty(tResult)) {
			throw new UserException("AUTH_ERROR_500", "您的手机号码未绑定到餐厅");
		}
		Restaurant result = tRestaurantToRestaurant(tResult.get(0));
		result.setManager_mobile(Long.parseLong(mobile));
		return result;
	}

	public Restaurant requestGetRestaurantByRestaurantId(int restaurantId, String mobile) throws UserException {
		HttpResponseEntity hResult = null;
		try {
			String requestUrl = getRequestRestaurantByRestaurantIdUrl(restaurantId);
			hResult = httpClient.doRequest(requestUrl, null, null, "UTF-8");
		} catch (Exception e) {
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
		if (hResult.getResponseCode() != 200) {
			if (hResult.getResponseCode() == 404) {
				throw new UserException("RESTAURANT_NOT_FOUND", "未找到对应餐厅");
			} else {
				throw new SystemException("NAPOS_ERROR_404", "系统异常");
			}
		}
		try {
			String jResult = hResult.getResponseContent();
			TRestaurant tResult = SerializeUtil.jsonToBean(jResult, TRestaurant.class);
			Restaurant result = tRestaurantToRestaurant(tResult);
			result.setManager_mobile(Long.parseLong(mobile));
			return result;
		} catch (Exception e) {
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	public TEleOrder requestGetEleOrderByEleOrderId(long eleOrderId) throws UserException {
		HttpResponseEntity hResult = null;
		try {
			String requestUrl = getRequestTEleOrderByTEleOrderIdUrl(eleOrderId);
			hResult = httpClient.doRequest(requestUrl, null, null, "UTF-8");
		} catch (Exception e) {
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
		if (hResult.getResponseCode() != 200) {
			logger.error("responseCode:{}, responseContent:{}", hResult.getResponseCode(), hResult.getResponseContent());
			if (hResult.getResponseCode() >= 500) {
				coffeeHermesService.safeSendAlarm(String.format(
						"napos connection failed with code:%s in requestGetEleOrderByEleOrderId(eleOrderId:%s)",
						hResult.getResponseCode(), eleOrderId), -1);
			}
			throw new UserException("DELIVERY_ORDER_ERROR_560", "获取配送单失败");
		}
		try {
			String jResult = hResult.getResponseContent();
			if (StringUtils.isEmpty(jResult)) {
				return null;
			}
			RedisLock redisLock = new RedisLock("ele_order_id", String.valueOf(eleOrderId), jedisPool);
			redisLock.lock(2000, 3);
			TEleOrder result = null;
			try {
				result = SerializeUtil.jsonToBean(jResult, TEleOrder.class);
				insertEleOrderDetail(SerializeUtil.beanToJson(result), eleOrderId);
			} catch (Throwable e) {
				logger.error("扫码拉取饿单失败", e);
			} finally {
				redisLock.unlock();
			}
			return result;
		} catch (Exception e) {
			logger.error("get eleOrder from Napos by eleOrderId failed, eleOrderId:{}", eleOrderId, e);
			throw new UserException("DELIVERY_ORDER_ERROR_560", "获取配送单失败");
		}
	}

	public List<TEleOrder> requestGetEleOrderListByRestaurantId(int restaurantId) throws UserException {
		HttpResponseEntity hResult = null;
		try {
			String requestUrl = getRequsetTEleOrderListByRestaurantId(restaurantId);
			hResult = httpClient.doRequest(requestUrl, null, null, "UTF-8");
		} catch (Exception e) {
			logger.info("开始写错误日志");
			errorLogProcessor.writeErrorInfo(String.format(
					"napos connection failed in requestGetEleOrderByRestaurantId(restaurantId: {}, errorMessage:{})",
					restaurantId, e.getMessage()));
			throw new UserException("DELIVERY_ORDER_ERROR_560", "获取配送单失败");

		}
		if (hResult.getResponseCode() != 200) {
			logger.error("responseCode:{}, responseContent:{}", hResult.getResponseCode(), hResult.getResponseContent());
			if (hResult.getResponseCode() >= 500) {
				coffeeHermesService.safeSendAlarm(String.format(
						"napos connection failed with code:%s in requestGetEleOrderByRestaurantId(restaurantId: %s)",
						hResult.getResponseCode(), restaurantId), -1);
			}
			throw new UserException("DELIVERY_ORDER_ERROR_560", "获取配送单失败");
		}
		try {
			String jResultList = hResult.getResponseContent();
			List<TEleOrder> tResultList = SerializeUtil.getObjectsFromJson(jResultList, TEleOrder.class);
			RedisLock redisLock = new RedisLock("restaurant_id", String.valueOf(restaurantId), jedisPool);
			redisLock.lock(3000, 4);
            logger.info("{}餐厅获取了{}单",restaurantId,tResultList.size());
			try {
				for (TEleOrder tResult : tResultList) {
					String jResult = SerializeUtil.beanToJson(tResult);
					insertEleOrderDetail(jResult, tResult.getId());
				}
			} catch (Throwable e) {
				logger.error("根据餐厅拉取饿单，部分成功，部分失败", e);
			} finally {
				redisLock.unlock();
			}
			return tResultList;
		} catch (Exception e) {
			logger.error("get eleOrder from Napos by restaurantId failed, restaurantId:"+restaurantId, e);
			throw new UserException("DELIVERY_ORDER_ERROR_560", "获取配送单失败");
		}
	}

	public void postEleOrderStatus(String eleOrderId, String deliveryOrderId, int status, String deliverymanName,
			String deliverymanMobile) throws UserException {
		try {
			Map<String, Object> postData = new HashMap<String, Object>();
			postData.put("orderId", eleOrderId);
			postData.put("deliveryOrderId", deliveryOrderId);
			postData.put("status", statusTotStatus(status));
			postData.put("substatus", "");
			postData.put("deliverymanName", deliverymanName);
			postData.put("deliverymanMobile", deliverymanMobile);
			postData.put("chargingType", "");
			postData.put("chargingAmount", "");
			String Url = posteleorderstatusurl.replaceAll("ELEORDERID", eleOrderId);
			HttpResponseEntity hResult = httpClient.doRequest(Url, postData, null, "UTF-8");
			if (hResult.getResponseCode() != 200) {
				logger.error("responseCode:{}, responseContent:{}", hResult.getResponseCode(),
						hResult.getResponseContent());
				if (hResult.getResponseCode() >= 500) {
					coffeeHermesService
							.safeSendAlarm(
									String.format(
											"napos connection failed with code:%s in postEleOrderStatus(eleOrderId: %s, deliveryOrderId:%s)",
											eleOrderId, deliveryOrderId), -1);
				}
				throw new SystemException("NAPOS_ERROR", "post delivery order status failed");
			}
		} catch (Exception e) {
			logger.error("post eleOrder status failed, eleOrderId:{}", eleOrderId, e);
			throw new UserException("DELIVERY_ORDER_ERROR_610", "系统异常，请重试");
		}
	}
	
    @Transactional
	public void insertRestaurant(int eleId, String managerMobile) throws UserException {
		// caution: assume one mobile bind one station, tmply use mobile to get
		// station
    	logger.debug("bind restaurant, eleId:{}, managerMobile:{}", eleId, managerMobile);
		try {
			RestaurantBindRecord record = new RestaurantBindRecord();
			record.setMobile(Long.parseLong(managerMobile));
			record.setStatus(1);
			record.setRestaurant_id(eleId);
			record.setCreated_at(new Timestamp(System.currentTimeMillis()));
			restaurantBindRecordDao.insert(record);
		} catch (Exception e) {
			logger.error("insert restaurantBindRecord failed in insertRestaurant()", e);
		}

		Restaurant restaurant = null;
		try {
			restaurant = requestGetRestaurantByRestaurantId(eleId, managerMobile);
		} catch (UserException e) {
			logger.error("get restaurnat by rst_id from napos failed", e);
			throw e;
		} catch (Exception e) {
			logger.error("get restaurnat by rst_id from napos failed", e);
			throw new SystemException("WALLE_POST_ERROR_001", "获取餐厅数据失败，请重试");
		}

		Restaurant localRestaurant = null;
		try {
			localRestaurant = restaurantDao.getRestaurantByMobile(Long.parseLong(managerMobile));
		} catch (Exception e) {
			logger.error("get local restaurant by ele_id failed", e);
			throw new SystemException("WALLE_POST_ERROR_002", "绑定失败，请重试");
		}

		if (localRestaurant != null) {
			if (localRestaurant.getEle_id() == restaurant.getEle_id()) {
				return;
			}
			else {
				throw new UserException("MOBILE_ALREADY_BINDED_ANOTHER", "手机号已经绑定其他餐厅");
			}
		}

		try {
			localRestaurant = restaurantDao.getRestaurantByEleId(eleId);
		} catch (Exception e) {
			logger.error("get local restaurant by ele_id failed", e);
			throw new SystemException("WALLE_POST_ERROR_002", "绑定失败，请重试");
		}

		User user = null;
		try {
			user = userService.getUserByMobileNoMatterStatus(managerMobile);
		} catch (Exception e) {
			logger.error("get user by mobile failed", e);
			throw new SystemException("WALLE_POST_ERROR_003", "绑定失败， 请重试");
		}
		if (user != null) {
			throw new UserException("MOBILE_ALREADY_IN_USE", "手机号已经绑定其他餐厅");
		}

		if (localRestaurant == null) {
			try {
				restaurantDao.insert(restaurant);
			} catch (Exception e) {
				logger.error("insert local restaurant failed", e);
				throw new SystemException("WALLE_POST_ERROR_004", "绑定失败， 请重试");
			}
		} else {
			try {
				StationRestaurant stationRestaurant = stationRestaurantDao.getStationRestaurantByRstId(localRestaurant
						.getId());
				if (stationRestaurant == null) {
					logger.debug("modifing restaurant(not binded), restaurantId:{}", localRestaurant.getId());
					localRestaurant.setManager_mobile(Long.parseLong(managerMobile));
					restaurantDao.update(localRestaurant);
				}
				else {
					Station station = stationDao.getStationByPk(stationRestaurant.getStation_id());
					logger.debug("modifing station, stationId:{}", station.getId());
					station.setManager_mobile(Long.parseLong(managerMobile));
					stationDao.update(station);
					User preAdmin = userService.getUserByMobileNoMatterStatus(String.valueOf(localRestaurant.getManager_mobile()));					
					logger.debug("record user mobile modify record, userId:{}, oldMobile:{}, newMobile:{}",
							preAdmin.getId(), preAdmin.getMobile(), managerMobile);
					AdminModifyRecord adminModifyRecord = new AdminModifyRecord();
					adminModifyRecord.setUser_id(preAdmin.getId());
					adminModifyRecord.setOld_mobile(preAdmin.getMobile());
					adminModifyRecord.setNew_mobile(Long.parseLong(managerMobile));
					adminModifyRecord.setCreated_at(new Timestamp(System.currentTimeMillis()));
					adminModifyRecordDao.insert(adminModifyRecord);	
					logger.debug("modifing user, userId{}", preAdmin.getId());
					preAdmin.setMobile(Long.parseLong(managerMobile));
					userService.updateUser(preAdmin);
					logger.debug("modifing restaurant(already binded), restaurantId:{}", localRestaurant.getId());
					localRestaurant.setManager_mobile(Long.parseLong(managerMobile));
					restaurantDao.update(localRestaurant);
					
				}
			} catch (Exception e) {
				logger.error("modify managerMobile failed", e);
				throw new SystemException("WALLE_POST_ERROR_005", "绑定失败， 请重试");
			}
		}

		try {
			String resName = restaurant.getName();
			coffeeHermesService.sendRestaurantBindNotification(managerMobile, managerMobile, resName);
		} catch (Exception e) {
			logger.error("send restaurant bind sms notification failed", e);
		}

		// try {
		// Station station =
		// stationDao.getStationByManagerMobile(Long.parseLong(mobile));
		// if (station != null) {
		// if (station.getStatus() == 1) {
		// logger.error("station exist, station status is already 1");
		// return;
		// }
		// logger.debug("station exist, setting station status to 1, stationId:{}",
		// station.getId());
		// station.setStatus(1);
		// stationDao.update(station);
		// logger.debug("setting station status to 1 successed, stationId:{}",
		// station.getId());
		// }
		// } catch (Exception e) {
		// logger.error("station exist, and setting station status to 1 failed",
		// e);
		// throw new SystemException("WALLE_POST_ERROR_001", "数据库异常");
		// }

	}
    
    public String getRestaurantBindInfo(int eleId) {
    	Restaurant restaurant = null;
    	try {
    		restaurant = restaurantDao.getRestaurantByEleId(eleId);
    		if (restaurant == null) {
    			return null;
    		}
    		else {
    			return String.valueOf(restaurant.getManager_mobile());
    		}
    	} catch (Exception e) {
    		logger.error("get restaurant bind info failed, eleId:{}", eleId);
    		return null;
    	}
    }

	/**
	 * Walle开通蜂鸟配送，写Restaurant信息和user表信息
	 * 
	 * @param mobile
	 * @return
	 * @throws UserException
	 */
	@Deprecated
	private boolean openBossAdminFromWalle(String mobile) throws UserException {
		Restaurant restaurant = null;
		Restaurant restaurantLocalByEleId = null;
		try {
			User user = userService.getUserByMobile(mobile);
			// 已经是配送员或者管理员，就不让再开餐厅
			if (user != null) {
				logger.error("您已经是配送员或者管理员，无法再开通蜂鸟配送,mobile:{}", mobile);
				throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_521.getCode(),
						"您已经是配送员或者管理员，无法再开通蜂鸟配送");
			}
			restaurant = this.requestGetRestaurantByMobile(mobile);
			if (restaurant != null) {
				restaurantLocalByEleId = restaurantDao.getRestaurantByEleId(restaurant.getEle_id());
			} else {
				return false;
			}
		} catch (UserException e) {
			logger.error("Walle开通蜂鸟配送失败,mobile:{}", mobile);
			throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_519.getCode(), "Walle开通蜂鸟配送失败");
		}
		// 1.不同手机号绑定同一个餐厅walle开通蜂鸟配送，点击登录返回异常
		// 2.同一个手机号点击walle开通蜂鸟配送多次. 两种情况都是为了防止本地有重复restaurant信息
		if (null != restaurantLocalByEleId) {
			logger.error("Walle开通蜂鸟配送,本地已存在餐厅信息,mobile:{}", mobile);
			throw ExceptionFactory.newUserException(ExceptionCode.AUTH_ERROR_520.getCode(), "Walle开通蜂鸟配送失败,此餐厅已开通过,");
		}
		int result = restaurantDao.insertRestaurantInfo(restaurant);
		if (result > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void closeRestaurant(int restaurantId, String mobile) {
		try {
			RestaurantBindRecord record = new RestaurantBindRecord();
			record.setMobile(Long.parseLong(mobile));
			record.setStatus(0);
			record.setRestaurant_id(restaurantId);
			record.setCreated_at(new Timestamp(System.currentTimeMillis()));
			restaurantBindRecordDao.insert(record);
		} catch (Exception e) {
			logger.error("insert restaurantBindRecord failed in closeRestaurant()", e);
		}
		// tmply return
		/*
		 * try { Station station =
		 * stationDao.getStationByManagerMobile(Long.parseLong(mobile)); if
		 * (station == null) { logger.error(
		 * "walle post unbind restaurant dose not exist, restaurantId:{}, mobile:{}"
		 * , restaurantId, mobile); throw new
		 * SystemException("WALLE_POST_ERROR_002", "餐厅不存在"); }
		 * station.setStatus(0); stationDao.update(station); } catch (Exception
		 * e) { logger.error("unbind restaurant failed ", e); throw new
		 * SystemException("WALLE_POST_ERROR_001", "数据库异常"); }
		 */
	}

	public String statusTotStatus(int status) {
		String tStatus = "";
		switch (status) {
		case 2:
			tStatus = "delivering";
			break;
		case 3:
			tStatus = "success";
			break;
		case 4:
			tStatus = "failed";
			break;
		}
		return tStatus;
	}
}
