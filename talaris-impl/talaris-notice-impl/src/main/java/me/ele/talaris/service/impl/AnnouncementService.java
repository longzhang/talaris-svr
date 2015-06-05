package me.ele.talaris.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ele.talaris.dao.AnnouncementDao;
import me.ele.talaris.dao.AnnouncementReadDao;
import me.ele.talaris.dto.AnnouncementVo;
import me.ele.talaris.dto.AnnouncementVoContent;
import me.ele.talaris.model.NtAnnouncement;
import me.ele.talaris.model.NtAnnouncementRead;
import me.ele.talaris.service.IAnnouncementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnnouncementService implements IAnnouncementService{
	
    @SuppressWarnings("rawtypes")
	@Autowired
    AnnouncementDao announcementDao;
    
	@SuppressWarnings("rawtypes")
	@Autowired
	AnnouncementReadDao announcementReadDao;
	
	/**
	 * 获取用户所有的用户公告，30天内记录
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnnouncementVo> getAnnouncementByUserId(int  userId,int cityId,boolean isNewUser) {
		List<AnnouncementVo>  announcementList =  new ArrayList<AnnouncementVo>(0);
		announcementList = announcementDao.getAnnouncementByUserId(userId, cityId,isNewUser);
		    
		   if(isNewUser){
			if( (announcementList!=null) && (announcementList.size()>0) ){
				for( AnnouncementVo  annVo: announcementList){
					NtAnnouncementRead ntRead  = new NtAnnouncementRead();
					ntRead.setAnnouncement_id(annVo.getId());
					ntRead.setUser_id(userId);
					ntRead.setStatus(0);
					java.sql.Timestamp nowTime = new java.sql.Timestamp(new Date().getTime());
					ntRead.setUpdated_at(nowTime);
					ntRead.setCreated_at(nowTime);
					announcementReadDao.insert(ntRead);
				}
		     } 
		   }
		return announcementList;
	}
	
	/**
	 * 获取公告详情
	 * @param userId
	 * @return
	 */
	public AnnouncementVoContent getAnnouncementById(int userId,int announcementId) {
		   NtAnnouncement announcement = announcementDao.getAnnouncementById(announcementId);
		   //返回指定格式
		   AnnouncementVoContent  returnAnnounce = new AnnouncementVoContent();
		   if(announcement!=null){
			   returnAnnounce.setId(announcement.getId());
			   returnAnnounce.setTitle(announcement.getTitle());
			   returnAnnounce.setContent(announcement.getContent());
			   returnAnnounce.setCreated_at(announcement.getCreated_at().getTime());
		   }
		   //更新公告的内容为已读
		   NtAnnouncementRead  announcementRead = announcementReadDao.getAnnouncementReadById(userId,announcementId);
		   if(announcementRead!=null){
			   //如果已经存在记录,则不更新
               if(announcementRead.getStatus() == 0||announcementRead.getStatus() == 2){
			   announcementRead.setStatus(1);
			   announcementReadDao.updateAnnouncementRead(announcementRead);
			   returnAnnounce.setStatus(0);
               }else{
                 returnAnnounce.setStatus(1);      
               }
		   }else{
			   NtAnnouncementRead annRead = new NtAnnouncementRead();
			   annRead.setUser_id(userId);
			   annRead.setStatus(1);
			   annRead.setAnnouncement_id(announcementId);
			   java.sql.Timestamp nowTime = new java.sql.Timestamp(new Date().getTime());
			   annRead.setCreated_at(nowTime);
			   annRead.setUpdated_at(nowTime);
			   announcementReadDao.addAnnouncementRead(annRead);
			   returnAnnounce.setStatus(1);
		   }
		   return returnAnnounce;
	}
	
	/**
	 * 获取重要的公告
	 * @param userId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AnnouncementVo> getImporttantAnnouncementById(int cityId,int userId,boolean isNewUser) {
		  List<AnnouncementVo> basicList = announcementDao.getImporttantAnnouncementById(cityId, userId, isNewUser);
		  List<AnnouncementVo> returnList =  new ArrayList<AnnouncementVo>();
		  if(basicList!=null){
			   for( AnnouncementVo aVo:basicList){
					if(aVo.getStatus()==0){
					 NtAnnouncementRead  announcementRead = announcementReadDao.getAnnouncementReadById(userId,aVo.getId());
					 announcementRead.setStatus(2);
				     announcementReadDao.updateAnnouncementRead(announcementRead);
					}
			   returnList.add(aVo);
		  }
		  }
		 return returnList;
	}
	
	/**
	 * 获取用户所在的城市
	 * @param userId
	 * @return
	 */
	public int getCityByUserId(int userId) {
		return announcementDao.getCityByUserId( userId);
	}
	
	
	/**
	 * 获取所有用户未读的消息
	 * @param userId
	 * @return
	 */
	public int getUnReadCount(int userId) {
		return announcementDao.getUnReadCount(userId);
	}
	
	/**
	 * 返回用户的记录数
	 * @param userId
	 * @return
	 */
	public int getAllCountById(int userId){
		return announcementDao.getAllCountById(userId);
	}

}
