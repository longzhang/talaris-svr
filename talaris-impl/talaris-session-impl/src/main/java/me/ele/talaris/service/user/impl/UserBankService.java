package me.ele.talaris.service.user.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import me.ele.talaris.dao.BankSerialMappingDao;
import me.ele.talaris.dao.UserBankInfoDao;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.BankSerialMapping;
import me.ele.talaris.model.UserBankInfo;
import me.ele.talaris.model.bank.BankSerialMappingDto;
import me.ele.talaris.model.bank.UserBankInfoDto;
import me.ele.talaris.service.user.IUserBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBankService implements IUserBankService{

	@Autowired
	BankSerialMappingDao bankSerialMappingDao;
	
	@Autowired
	UserBankInfoDao userBankInfoDao;
	
	@Autowired
	BankAuthService bankAuthService;

	private long bankAuthTimeGap = 12*60*60*1000;	//查询时间与银行卡绑定时间超过12小时，发送验证状态查看请求

	private final static Logger logger = LoggerFactory.getLogger(UserBankService.class);
	
	private static Timestamp getWeekStartTimestamp() {
		Calendar c1 = new GregorianCalendar();
		c1.setFirstDayOfWeek(Calendar.MONDAY);
		c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		return new Timestamp(c1.getTime().getTime());
	}

	@Override
	public List<BankSerialMappingDto> getSupportBankList() throws UserException, SystemException{
		
		try{
			List<BankSerialMapping> bankSerialMappingList = bankSerialMappingDao.listActiveBanks();

			List<BankSerialMappingDto> bankList = new ArrayList<BankSerialMappingDto>();
			
			if(bankSerialMappingList != null)
			{
				for(BankSerialMapping mappingItem : bankSerialMappingList) {
					BankSerialMappingDto bankItem = new BankSerialMappingDto();
					bankItem.setBank_name(mappingItem.getBank_name());
					bankItem.setBank_id(mappingItem.getId());
					bankList.add(bankItem);
				}
			}
			return bankList;
		} catch(Exception e) {
			logger.error("查找银行信息失败");
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_001);
		}
	}

	@Override
	public UserBankInfoDto addBindBankCard(int user_id, String user_name, int bank_id, String bank_account) throws UserException, SystemException{
		
		try{
			UserBankInfo originUserBankInfo = userBankInfoDao.getUserBankInfo(user_id);
			
			if(originUserBankInfo != null) {
				throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_002);
			}
			
			userBankInfoDao.addUserBankInfo(user_id, user_name, bank_id, bank_account);
		} catch(Exception e) {
			logger.error("添加银行账号绑定失败");
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_003);
		}

		UserBankInfoDto userBankInfoDto = getBindBankCard(user_id);
		if(userBankInfoDto == null) {
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_007);
		}

		//TODO - 后续版本恢复此功能
		//发送银行卡绑定验证信息
		//EDU-1.0.6：风控体系未建立，暂时不启用1分钱验证功能
//		try {
//			String tradeNo = new Integer(user_id).toString()+
//					new Long(userBankInfoDto.getUpdated_at().getTime()).toString();
//			String inAccountBankId = new Integer(bank_id).toString();
//			int result = bankAuthService.sendBankAuthRequest(tradeNo, user_name, bank_account, inAccountBankId);
//			if(result == 0) {
//				throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_009);
//			}
//		} catch(Exception e) {
//			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_009);
//		}
		return userBankInfoDto;
	}

	@Override
	public UserBankInfoDto updateBindBankCard(int user_id, String user_name,
			int bank_id, String bank_account, int is_active) throws UserException, SystemException {
		
		try {
			UserBankInfo originUserBankInfo = userBankInfoDao.getUserBankInfo(user_id);
			
			//未存在该用户绑定信息，无法更新
			if(originUserBankInfo == null) {
				throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_004);			
			}

			//一周只允许更新一次银行卡绑定信息
			Timestamp lastUpdateTimestamp = originUserBankInfo.getUpdated_at();
			Timestamp weekStartTimestamp = getWeekStartTimestamp();
			if(lastUpdateTimestamp.getTime() > weekStartTimestamp.getTime()) {
				throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_011);
			}

			int id = originUserBankInfo.getId();
			userBankInfoDao.updateUserBankInfo(id, user_name, bank_id, bank_account, is_active);

		} catch(Exception e) {
			logger.error("更新银行账号绑定失败");
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_005);
		}

		UserBankInfoDto userBankInfoDto = getBindBankCard(user_id);
		if(userBankInfoDto == null) {
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_008);
		}

		//TODO - 后续版本恢复此功能
		//发送银行卡绑定验证信息
		//EDU-1.0.6：风控体系未建立，暂时不启用1分钱验证功能
//		try {
//			String tradeNo = new Integer(user_id).toString()+
//					new Long(userBankInfoDto.getUpdated_at().getTime()).toString();
//			String inAccountBankId = new Integer(bank_id).toString();
//			int result = bankAuthService.sendBankAuthRequest(tradeNo, user_name, bank_account, inAccountBankId);
//			if(result == 0) {
//				throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_010);
//			}
//		} catch(Exception e) {
//			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_010);
//		}
		return userBankInfoDto;
	}

	@Override
	public UserBankInfoDto getBindBankCard(int user_id) throws UserException, SystemException{
		try {
			UserBankInfo userBankInfo = userBankInfoDao.getUserBankInfo(user_id);
			
			if(userBankInfo != null) {

				UserBankInfoDto userBankInfoDto = new UserBankInfoDto();
	
				BeanUtils.copyProperties(userBankInfo, userBankInfoDto);
				
				BankSerialMapping bankMapping = bankSerialMappingDao.getBankById(userBankInfo.getBank_id());
				
				userBankInfoDto.setBank_name(bankMapping.getBank_name());
				
				//TODO - 后续版本恢复此功能
				//EDU - 1.0.6：风控体系未建立，暂时不启用1分钱验证功能
//				if(userBankInfo.getIs_bind() == 0) {
//					Timestamp lastUpdateTime = userBankInfo.getUpdated_at();
//					Timestamp currentTime = new Timestamp(System.currentTimeMillis());
//					//银行卡绑定时间距现在已超过12小时，进行查询操作
//					if((currentTime.getTime() - lastUpdateTime.getTime()) > bankAuthTimeGap) {
//						String tradeNo = new Integer(user_id).toString()+new Long(userBankInfo.getUpdated_at().getTime()).toString();
//						int result = bankAuthService.getBankAuthResult(tradeNo);
//						userBankInfoDto.setIs_bind(result);
//						if(result != 0) {
//							int id = userBankInfo.getId();
//							userBankInfoDao.updateUserBankBindInfo(id, result);
//						}
//					}
//				}
				return userBankInfoDto;
			} else {
				return null;
			}
		} catch(Exception e) {
			logger.error("查询银行账号绑定失败");
			throw ExceptionFactory.newUserException(ExceptionCode.BANK_CARD_ERROR_006);
		}
	}
	
}
