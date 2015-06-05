package me.ele.talaris.service.deliveryorder;

import java.util.List;

import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.Station;

public interface ICreateDeliveryOrderService {

	/**
	 * 创建配送单，分为扫码创建，或者是批量选择然后确认创建
	 * 
	 * @param context
	 * @param eleOrderIds
	 * @param actionType 如果为扫码拉取订单，则直接更新其状态为配送中
	 * @return
	 * @throws UserException
	 */
	public List<DeliveryOrder> fetchDeliveryOrders(Context context,
			String[] eleOrderIds, String actionType) throws UserException;

	/**
	 * 更新配送单为配送中：注意，该方法会修改配送单的创建时间，挂在一个配送员身上，然后可以算配送时长
	 * 
	 * @param status
	 * @param deliveryId
	 * @param userId
	 * @param mobile
	 * @return
	 */
	public Integer updateDeliveryOrderStatus(int stationId,int status, long deliveryId,
			int userId, long mobile);

	public void addDeliveryOrder(Context context, List<DeliveryOrder> deliveryOrders) ;

    public DeliveryOrder createDeliveryOrder(Context context, DeliveryOrder inOrder, Long mobile)  throws UserException;

    public Station getStationByRestaurantId(int rstId);

}