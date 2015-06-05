package me.ele.talaris.service.user.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import me.ele.talaris.constant.Constant;
import me.ele.talaris.dao.UserBankInfoDao;
import me.ele.talaris.dao.UserSubjectBillDao;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.UserBankInfo;
import me.ele.talaris.model.UserStationRole;
import me.ele.talaris.model.UserSubjectBill;
import me.ele.talaris.model.wallet.UserWalletDaily;
import me.ele.talaris.model.wallet.UserWalletTotal;
import me.ele.talaris.service.user.IUserWalletService;
import me.ele.talaris.utils.Pair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserWalletService implements IUserWalletService{

	private final long forwardDays = 10*24*3600*1000;	//前推10天
	
	@Autowired
	UserSubjectBillDao userSubjectBillDao;

	@Autowired
	UserBankInfoDao userBankInfoDao;

	//获取当天的零点时间戳
	private static Timestamp getTodayStartTimestamp() {
		Calendar c1 = new GregorianCalendar();
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c1.getTime().getTime());
	}
	
	@Override
	public UserWalletTotal getWalletDetailByUserId(Context context,
			int pageNow, int pageSize) throws UserException {

		int userId = context.getUser().getId();
		if(userId == 0) {
			throw new UserException("WALLET_ERROR_001", "无此用户");
		}
		
		List<UserStationRole> userStationRoles = context.getUserStationRoles();
		if(CollectionUtils.isEmpty(userStationRoles)) {
			throw new UserException("WALLET_ERROR_002", "登陆用户信息出错");
		}
		
		//只有配送员可调用此接口
		boolean managerRoleOnly = true;
		for(UserStationRole uSRole : userStationRoles) {
			if(uSRole.getRole_id() == Constant.COURIER_CODE) {
				managerRoleOnly = false;
				break;
			}
		}

		if(managerRoleOnly) {
			throw new UserException("WALLET_ERROR_003", "登陆用户身份出错，餐厅管理员无法查看账户余额！");			
		}
		
		boolean displayDailyList = true;
		//EDU-1.0.6尚未启用银行卡绑定验证功能，只有未绑定银行卡的用户不展示补贴单明细情况
		//TODO - 后续版本恢复此功能
		UserBankInfo currentUserBankInfo = userBankInfoDao.getUserBankInfo(userId);
		if(currentUserBankInfo == null) {
			displayDailyList = false;
		}
//		if(currentUserBankInfo == null || currentUserBankInfo.getIs_bind() != 1) {
//			displayDailyList = false;
//		}

		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Timestamp forwardDate = new Timestamp(getTodayStartTimestamp().getTime() - forwardDays);

		//分页查询每日补贴详情和历史详情
		Pair<List<UserSubjectBill>, Long> userBillDataPagePair = userSubjectBillDao.selectPageOneCourierAllTypeBill(userId, pageNow, pageSize, forwardDate);
		
		List<UserWalletDaily> userWalletList = new ArrayList<UserWalletDaily>();

		UserWalletTotal userWalletTotal = new UserWalletTotal();	
		
		userWalletTotal.setUser_id(userId);
		userWalletTotal.setCreated_at(currentTime);
		userWalletTotal.setUpdated_at(currentTime);
		
		for(UserSubjectBill uSBill : userBillDataPagePair.first)
		{
			//只向前端传输有效记录
			if(uSBill.getIs_active() == 1)	
			{
				UserWalletDaily walletDaily = new UserWalletDaily();
				walletDaily.setAllowance(uSBill.getAmount());
				walletDaily.setType(uSBill.getActivity_name());
				walletDaily.setPredict_pay_date(uSBill.getClear_date());
				walletDaily.setDate(new Date(uSBill.getCreated_at().getTime()));
				walletDaily.setCreated_at(uSBill.getCreated_at());
				walletDaily.setUpdated_at(uSBill.getCreated_at());
				
				userWalletTotal.incTotalAllowance(uSBill.getAmount());
				if(currentTime.compareTo(uSBill.getClear_date()) > 0) {
					userWalletTotal.incPaidAllowance(uSBill.getAmount());
					walletDaily.setIs_paid(1);
				} else {
					userWalletTotal.incUnpaidAllowance(uSBill.getAmount());
					walletDaily.setIs_paid(0);
				}
				if(displayDailyList) {
					userWalletList.add(walletDaily);
				}
			}
		}
		
		Collections.sort(userWalletList);
		userWalletTotal.setWallet_detail_daily(userWalletList);

		if(displayDailyList) {
			//查询补贴每日详情
			List<UserSubjectBill> userBillDataAllPair = userSubjectBillDao.selectOneCourierBill(userId, forwardDate);
			userWalletTotal.setDaily_list_size(userBillDataAllPair.size());
		} else {
			userWalletTotal.setDaily_list_size(0);
		}
		
		return userWalletTotal;
	}

}
