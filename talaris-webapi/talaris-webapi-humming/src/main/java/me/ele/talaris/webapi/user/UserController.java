package me.ele.talaris.webapi.user;

import java.sql.Timestamp;
import java.util.List;

import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.bank.BankSerialMappingDto;
import me.ele.talaris.model.bank.UserBankInfoDto;
import me.ele.talaris.model.wallet.UserWalletTotal;
import me.ele.talaris.service.user.IUserBankService;
import me.ele.talaris.service.user.IUserWalletService;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController extends WebAPIBaseController{
	
	@Autowired
	IUserWalletService userWalletService;
	
	@Autowired
	IUserBankService userBankService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 获取补贴详情
     * 
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/webapi/wallet")
    @InterfaceMonitor(interfaceName = "获取补贴详情")
    public @ResponseBody ResponseEntity<UserWalletTotal> GetUserWalletDetail(Context context,
    		@RequestParam("page_now") int page_now, @RequestParam("page_size") int page_size) throws UserException{
        
    	logger.info("获取用户补贴详情");
    	
    	ResponseEntity<UserWalletTotal> responseEntity = null;
        
        UserWalletTotal userWalletDetail = userWalletService.getWalletDetailByUserId(context, page_now, page_size);
        
        responseEntity = new ResponseEntity<UserWalletTotal>("200", "", userWalletDetail);
        
        return responseEntity;
    }

    /**
     * 查询支持绑定的银行列表
     */
    @RequestMapping(value = "/webapi/banks", method = RequestMethod.GET)
    @InterfaceMonitor(interfaceName = "获取支持绑定的银行列表")
    public @ResponseBody ResponseEntity<List<BankSerialMappingDto>> GetBanks() throws UserException {
        
    	logger.info("获取支持绑定的银行卡列表");

    	ResponseEntity<List<BankSerialMappingDto>> responseEntity = null;
    	
        List<BankSerialMappingDto> bankList = userBankService.getSupportBankList();
        
        responseEntity = new ResponseEntity<List<BankSerialMappingDto>>("200", "", bankList);
        
    	return responseEntity;
    }
    
    /**
     * 用户新增绑定银行卡
     * 
     * @param user_name
     * @param bank_id
     * @param bank_account
     */
    @RequestMapping(value = "/webapi/bank_card/bind", method = RequestMethod.POST)
    @InterfaceMonitor(interfaceName = "绑定新银行卡")
    public @ResponseBody ResponseEntity<UserBankInfoDto> CreateBankCardBind(Context context, 
    		@RequestParam("user_name") String user_name, @RequestParam("bank_id") int bank_id, @RequestParam("bank_account") String bank_account) throws UserException {

    	logger.info("用户绑定新的银行卡");

    	ResponseEntity<UserBankInfoDto> responseEntity = null;
        int user_id = context.getUser().getId();
        
        if(user_id != 0) {
        	UserBankInfoDto bindCardInfo = userBankService.addBindBankCard(user_id, user_name, bank_id, bank_account);
        	responseEntity = new ResponseEntity<UserBankInfoDto>("200", "", bindCardInfo);
        } else {
        	responseEntity = new ResponseEntity<UserBankInfoDto>("BANK_ERROR_003", "绑定失败，请重试", null);
        }
    	return responseEntity;
    }
    
    /**
     * 用户查看绑定银行卡信息
     * 
     * @param user_id
     */
    @RequestMapping(value = "/webapi/bank_card", method = RequestMethod.GET)
    @InterfaceMonitor(interfaceName = "查看银行卡绑定信息")
    public @ResponseBody ResponseEntity<UserBankInfoDto> GetBankCardBindInfo(Context context) throws UserException {
        
    	logger.info("用户查看已绑定的银行卡");

    	ResponseEntity<UserBankInfoDto> responseEntity = null;
        int user_id = context.getUser().getId();
        
        if(user_id != 0) {
        	UserBankInfoDto bindCardInfo = userBankService.getBindBankCard(user_id);
        	responseEntity = new ResponseEntity<UserBankInfoDto>("200", "", bindCardInfo);
        } else {
        	responseEntity = new ResponseEntity<UserBankInfoDto>("BANK_ERROR_004", "获取失败，请重试", null);
        }
    	return responseEntity;
    }
    
    /**
     * 用户更新绑定银行卡信息
     * 
     * @param user_name
     * @param bank_id
     * @param bank_account
     * @param is_active
     */
    @RequestMapping(value = "/webapi/bank_card", method = RequestMethod.PUT)
    @InterfaceMonitor(interfaceName = "更新银行卡绑定信息")
    public @ResponseBody ResponseEntity<UserBankInfoDto> UpdateBankCardBindInfo(Context context,
    		@RequestParam("user_name") String user_name, @RequestParam("bank_id") int bank_id, @RequestParam("bank_account") String bank_account,
    		@RequestParam("is_active") int is_active) throws UserException {

    	logger.info("用户更新已绑定的银行卡");

    	ResponseEntity<UserBankInfoDto> responseEntity = null;
        int user_id = context.getUser().getId();
        
        if(user_id != 0) {
        	UserBankInfoDto bindCardInfo = userBankService.updateBindBankCard(user_id, user_name, bank_id, bank_account, is_active);
        	responseEntity = new ResponseEntity<UserBankInfoDto>("200", "", bindCardInfo);
        } else {
        	responseEntity = new ResponseEntity<UserBankInfoDto>("BANK_ERROR_005", "更新失败，请重试", null);
        }
    	return responseEntity;
    }

    /**
     * 用户获取系统时间
     *
     */
    @RequestMapping(value = "/webapi/sys/time", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<Timestamp> getSystemTime(Context context) throws UserException {

        logger.info("用户查询当前系统时间");

        ResponseEntity<Timestamp> responseEntity = null;

        responseEntity = new ResponseEntity<Timestamp>("200", "", new Timestamp(System.currentTimeMillis()));
        return responseEntity;
    }

}
