package me.ele.talaris.service.user;

import java.util.List;

import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.model.User;

public interface IUserService {

	/**
	 * TODO: move to session service
	 * 
	 * 通过token获取user，会检查token有没有过期
	 */
	public User getUserByToken(String token);

	public User getUserByMobile(String mobile);
	
	public User getUserByMobileNoMatterStatus(String mobile);
	
	public int updateUser(User user);
	
	

	/**
	 * 增加一个人员信息
	 * 
	 * @param newUser
	 * @return
	 */
	public int addUser(User newUser);

	/**
	 * 增加一个人user信息并通过手机号返回user
	 * 
	 * @param user
	 * @return
	 */
	public User addNewUser(User user);

	/**
	 *
	 *
	 * 更新基本信息，如，姓名、身份证号码
	 * 
	 * @param user
	 * @return
	 */
	public int updateUserInfo(User user) throws UserException, SystemException;

	/**
	 * 个人信息是否完整 身份证号，姓名
	 * 
	 * @param userId
	 * @return
	 */
	public boolean isUserBaseInfoCompleted(int userId);

	/**
	 * 同意协议
	 * 
	 * @param userId
	 * @return
	 */
	public int hasAcceptedAgreement(int userId);

	/**
	 * 通过用户id查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserByUserId(int userId);

	/**
	 * 通过用户id查询用户信息NoMatterStatus
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserByUserIdNoMatterStatus(int userId);

	public User updateCourier(Context context, long mobile, String takerName,
			String certificateNumber) throws UserException;

	/**
	 * 根据staionId和roleId查看对应的所有roleId人员
	 * 
	 * @param context
	 * @param stationId
	 * @param roleId
	 * @return
	 */
	public List<User> listUserByStaionIdAndRoleId(Context context,
			int stationId, int roleId);


}