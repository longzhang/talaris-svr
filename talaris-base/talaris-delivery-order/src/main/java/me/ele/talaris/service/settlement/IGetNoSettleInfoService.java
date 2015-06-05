package me.ele.talaris.service.settlement;

import java.util.List;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.settlement.TakerNoSettleInfo;

public interface IGetNoSettleInfoService {

	/**
	 * 查询站点下所有配送员未结款信息
	 * 
	 * @param stationId
	 * @return
	 * @throws UserException
	 */
	public List<TakerNoSettleInfo> getNoSettleInfoByStationId(Context context,
			int stationId, int roleId) throws UserException;

}