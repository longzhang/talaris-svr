package me.ele.talaris.napos.service;

import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Restaurant;
import me.ele.talaris.napos.model.TEleOrder;

public interface INaposService {

	/**
	 * 通过餐厅老板手机号查询餐厅
	 * 
	 * @param mobile
	 * @return Restaurant
	 * @throws UserException
	 * @throws SystemException
	 */

	public Restaurant getRestaurantByMobile(String mobile) throws UserException;

	/**
	 * 通过餐厅id查询餐厅
	 * 
	 * @param restaurantId
	 * @param mobile
	 * @return Restaurant
	 * @throws UserException
	 * @throws SystemException
	 */

	public Restaurant getRestaurantByRestaurantId(int restaurantId,
			String mobile) throws UserException;

	/**
	 * 通过饿单Id获取饿单
	 * 
	 * @param eleOrderId
	 * @return TEleorder
	 * @throws UserException
	 * @throws SystemException
	 */

	public TEleOrder getEleOrderByEleOrderId(Long eleOrderId)
			throws UserException;

	/**
	 * 通过餐厅ID拉取饿单
	 * 
	 * @param restaurantId
	 * @return List<TEleorder>
	 * @throws UserException
	 * @throws SystemException
	 */

	public List<TEleOrder> getEleOrderListByRestaurantId(int restaurantId)
			throws UserException;

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

	public void updateEleOrderStatus(String eleOrderId, String deliveryOrderId,
			int status, String deliverymanName, String deliverymanMobile)
			throws UserException;

	/**
	 * 激活餐厅
	 * 
	 * @param tRestaurant
	 * @param mobile
	 * @throws UserException 
	 * @throws SystemException
	 */

	public void recordRestaurantBindInfo(int eleId, String managerMobile) throws UserException;

	/**
	 * 获取餐厅绑定信息
	 * @param eleId
	 * 
	 * @return mobile
	 */
	
	public String getRestaurantBindInfo(int eleId);
	/**
	 * 关闭餐厅建站功能
	 * 
	 * @param tRestaurant
	 * @param mobile
	 * @throws SystemException
	 */

	public void deactivateRestaurant(int restaurantId, String mobile);

}