package me.ele.talaris.dao;

import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.NtAnnouncementRead;

import org.springframework.jdbc.core.BeanPropertyRowMapper;


public class AnnouncementReadDao<E> extends BaseSpringDao<NtAnnouncementRead> {
	
	
	public AnnouncementReadDao() {
		super(new BeanPropertyRowMapper<NtAnnouncementRead>(NtAnnouncementRead.class));
	}
	
	/**
	 * 增加,用户月阅读记录表
	 * @param AnnouncementRead
	 * @return 1 成功
	 */
	public int addAnnouncementRead(NtAnnouncementRead ntAnnouncementRead){
		return this.insert(ntAnnouncementRead);
	}
	
	/**
	 * 更新阅读记录信息
	 * @param userId
	 * @param announcementId
	 * @return
	 */
	public int updateAnnouncementRead(NtAnnouncementRead ntAnnouncementRead){
		return this.update(ntAnnouncementRead);
	}
	
	/**
	 * 根据用户Id和announcementId查找NtAnnouncementRead 记录
	 * @param userId
	 * @return
	 */
	public NtAnnouncementRead getAnnouncementReadById(int userId,int announcementId){
		return this.selectOneOrNull("where user_id = ? and announcement_id = ?", userId, announcementId);
	}
	
}
