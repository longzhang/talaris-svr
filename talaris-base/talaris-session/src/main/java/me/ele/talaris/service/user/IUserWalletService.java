package me.ele.talaris.service.user;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.wallet.UserWalletTotal;

public interface IUserWalletService {
	/**
	 * 根据userId和roleId查看对应的账户余额
	 * 
	 * @param context
	 * @param userId
	 * @param roleId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public UserWalletTotal getWalletDetailByUserId(Context context, int pageNum, int pageSize) throws UserException;

}
