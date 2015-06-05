package me.ele.talaris.service.user;

import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.bank.BankSerialMappingDto;
import me.ele.talaris.model.bank.UserBankInfoDto;

public interface IUserBankService {
	
	public List<BankSerialMappingDto> getSupportBankList() throws UserException, SystemException;
	
	public UserBankInfoDto addBindBankCard(int user_id, String user_name, int bank_id, String bank_account) throws UserException, SystemException;
	
	public UserBankInfoDto updateBindBankCard(int user_id, String user_name, int bank_id, String bank_account, int is_active) throws UserException, SystemException;
	
	public UserBankInfoDto getBindBankCard(int user_id) throws UserException, SystemException;
}
