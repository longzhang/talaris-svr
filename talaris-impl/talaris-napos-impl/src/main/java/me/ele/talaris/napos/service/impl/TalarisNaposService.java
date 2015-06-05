package me.ele.talaris.napos.service.impl;

import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.napos.model.TEleOrder;
import me.ele.talaris.napos.service.INaposService;
import me.ele.talaris.napos.wrapper.NaposServiceWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author kimizhang
 *
 */
public class TalarisNaposService implements INaposService {
	private final static Logger logger = LoggerFactory.getLogger(TalarisNaposService.class);

	private NaposServiceWrapper naposServiceWrapper;

	/**
	 * 通过餐厅老板手机号查询餐厅
	 * 
	 * @param mobile
	 * @return Restaurant
	 * @throws UserException
	 * @throws SystemException
	 */

	@Override
	public Restaurant getRestaurantByMobile(String mobile) throws UserException {

		try {
			logger.info("正在通过manager_mobile:{}, 查询Restaurant", mobile);
			Restaurant result = naposServiceWrapper.requestGetRestaurantByMobile(mobile);
			logger.info("通过manager_mobile:{}, 查询Restaurant成功", mobile);
			return result;
		} catch (UserException e) {
			logger.error("通过餐厅老板手机号查询Restaurant失败 manager_mobile:{}, code:{} , message:{}", mobile, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.error("通过餐厅老板手机号查询Restaurant失败 manager_mobile:{}, code:{} , message:{}", mobile, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("napos unknown exception", e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	/**
	 * 通过餐厅id查询餐厅
	 * 
	 * @param restaurantId
	 * @param mobile
	 * @return Restaurant
	 * @throws UserException
	 * @throws SystemException
	 */

	@Override
	public Restaurant getRestaurantByRestaurantId(int restaurantId, String mobile) throws UserException {
		try {
			logger.info("正在通过manager_mobile:{}, 查询Restaurant", restaurantId);
			Restaurant result = naposServiceWrapper.requestGetRestaurantByRestaurantId(restaurantId, mobile);
			logger.info("通过manager_mobile:{}, 查询Restaurant成功", restaurantId);
			return result;
		} catch (UserException e) {
			logger.error("通过restaurantId查询餐厅失败 restaurantId:{}, code:{} , message:{}", restaurantId, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.error("查询restaurantId查询餐厅失败 restaurantId:{}, code:{} , message:{}", restaurantId, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("napos unknown exception", e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	/**
	 * 通过饿单Id获取饿单
	 * 
	 * @param eleOrderId
	 * @return TEleorder
	 * @throws UserException
	 * @throws SystemException
	 */

	@Override
	public TEleOrder getEleOrderByEleOrderId(Long eleOrderId) throws UserException {
		try {
			logger.info("正在通过饿单Id:{}, 查询饿单", eleOrderId);
			TEleOrder result = naposServiceWrapper.requestGetEleOrderByEleOrderId(eleOrderId);
			logger.info("通过饿单成功 Id:{}, 查询饿单", eleOrderId);
			return result;
		} catch (UserException e) {
			logger.error("通过饿单Id获取饿单 eleOrderId:{}, code:{} , message:{}", eleOrderId, e.getErrorCode(), e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.error("通过饿单Id获取饿单 eleOrderId:{}, code:{} , message:{}", eleOrderId, e.getErrorCode(), e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("napos unknown exception", e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	/**
	 * 通过餐厅ID拉取饿单
	 * 
	 * @param restaurantId
	 * @return List<TEleorder>
	 * @throws UserException
	 * @throws SystemException
	 */

	@Override
	public List<TEleOrder> getEleOrderListByRestaurantId(int restaurantId) throws UserException {
		try {
			logger.info("通过餐厅Id:{}, 拉取饿单", restaurantId);
			List<TEleOrder> result = naposServiceWrapper.requestGetEleOrderListByRestaurantId(restaurantId);
			logger.info("通过餐厅Id:{}, 拉取饿单成功", restaurantId);
			return result;
		} catch (UserException e) {
			logger.error("通过餐厅Id拉取饿单失败 eleOrderId:{}, code : {} , message : {}", restaurantId, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (SystemException e) {
			logger.error("通过餐厅Id拉取饿单失败 eleOrderId:{}, code : {} , message : {}", restaurantId, e.getErrorCode(),
					e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("napos unknown exception", e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	/**
	 * 更新饿单信息
	 * 
	 * @param eleOrderId
	 * @param deliveryOrderId
	 * @param status
	 *            （2：配送中，3:配送完成，4:配送失败）
	 * @return
	 * @throws UserException
	 */

	@Override
	public void updateEleOrderStatus(String eleOrderId, String deliveryOrderId, int status, String deliverymanName,
			String deliverymanMobile) throws UserException {
		try {
			logger.info(
					"更新饿单信息 eleOrderId:{}, deliveryOrderId:{}, status:{}, deliverymanName:{}, deliverymanMobile:{}",
					eleOrderId, deliveryOrderId, status, deliverymanName, deliverymanMobile);
			naposServiceWrapper.postEleOrderStatus(eleOrderId, deliveryOrderId, status, deliverymanName,
					deliverymanMobile);
			logger.info(
					"更新饿单信息成功 eleOrderId:{}, deliveryOrderId:{}, status:{}, deliverymanName:{}, deliverymanMobile:{}",
					eleOrderId, deliveryOrderId, status, deliverymanName, deliverymanMobile);
		} catch (UserException e) {
			logger.error(
					"更新饿单信息失败 eleOrderId:{}, deliveryOrderId:{}, status:{}, deliverymanName:{}, deliverymanMobile:{}",
					eleOrderId, deliveryOrderId, status, deliverymanName, deliverymanMobile, e);
			throw e;
		} catch (Exception e) {
			logger.error("napos unknown exception", e);
			throw new SystemException("NAPOS_ERROR_404", "系统异常");
		}
	}

	/**
	 * 激活餐厅
	 * 
	 * @param tRestaurant
	 * @param mobile
	 * @throws UserException 
	 * @throws SystemException
	 */

	@Override
	public void recordRestaurantBindInfo(int eleId, String managerMobile) throws UserException {
		logger.debug("激活餐厅, restaurantId:{}, mobile:{}", eleId, managerMobile);
		naposServiceWrapper.insertRestaurant(eleId, managerMobile);
		logger.debug("激活餐厅成功, restaurantId:{}, mobile:{}", eleId, managerMobile);
	}
	
	/**
	 * 获取餐厅绑定信息
	 * @param eleId
	 * 
	 * @return mobile
	 */
	@Override
	public String getRestaurantBindInfo(int eleId) {
		logger.debug("获取餐厅绑定信息， eleId:{}", eleId);
		String mobile = naposServiceWrapper.getRestaurantBindInfo(eleId);
		logger.debug("获取餐厅绑定信息成功, eleId:{}, mobile:{}", eleId, mobile);
		return mobile;
	}

	/**
	 * 关闭餐厅建站功能
	 * 
	 * @param tRestaurant
	 * @param mobile
	 * @throws SystemException
	 */

	@Override
	public void deactivateRestaurant(int restaurantId, String mobile) {
		logger.debug("关闭餐厅建站功能, restaurantId:{}, mobile:{}", restaurantId, mobile);
		naposServiceWrapper.closeRestaurant(restaurantId, mobile);
		logger.debug("关闭餐厅建站功能成功, restaurantId:{}, mobile:{}", restaurantId, mobile);
	}

	public NaposServiceWrapper getNaposServiceWrapper() {
		return naposServiceWrapper;
	}

	public void setNaposServiceWrapper(NaposServiceWrapper naposServiceWrapper) {
		this.naposServiceWrapper = naposServiceWrapper;
	}
}
