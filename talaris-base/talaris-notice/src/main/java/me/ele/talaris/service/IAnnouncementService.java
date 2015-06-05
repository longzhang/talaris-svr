package me.ele.talaris.service;

import java.util.List;

import me.ele.talaris.dto.AnnouncementVo;
import me.ele.talaris.dto.AnnouncementVoContent;


public interface IAnnouncementService {
	
	/**
	 * 获取用户所有的用户公告，30天内记录
	 * @param userId
	 * @return
	 */
	public List<AnnouncementVo> getAnnouncementByUserId(int userId,int cityId,boolean isNewUser) ;
	
	/**
	 * 获取公告详情
	 * @param userId
	 * @return
	 */
	public AnnouncementVoContent getAnnouncementById(int userId, int announcementId) ;
	
	
	/**
	 * 获取重要的公告
	 * @param userId
	 * @return
	 */
	public List<AnnouncementVo> getImporttantAnnouncementById(int cityId,int userId,boolean isNewUser)  ;
	
	/**
	 * 获取用户所在的城市
	 * @param userId
	 * @return
	 */
	public int getCityByUserId(int userId) ;

	
	/**
	 * 获取所有用户未读的消息
	 * @param userId
	 * @return
	 */
	public int getUnReadCount(int userId);
	
	/**
	 * 返回用户的记录数
	 * @param userId
	 * @return
	 */
	public int getAllCountById(int userId);

}
