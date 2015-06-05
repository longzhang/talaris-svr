
package me.ele.talaris.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import me.ele.talaris.dto.AnnouncementVo;
import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.model.NtAnnouncement;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * @author dawson
 * @param <E>
 *
 */
public class AnnouncementDao<E> extends BaseSpringDao<NtAnnouncement> {

	public AnnouncementDao() {
		super(new BeanPropertyRowMapper<NtAnnouncement>(NtAnnouncement.class));
	}
	
	/**
	 * 获取用户所有的用户公告，30天内记录
	 * @param userId
	 * @return
	 */
	public List<AnnouncementVo> getAnnouncementByUserId(int userId,int cityId,boolean isNewUser) {
		
		Calendar calNow = Calendar.getInstance();
		calNow.setTimeInMillis(new Date().getTime());
		
		calNow.add(Calendar.DATE, -30);
		Date selectStart =new Date();
		selectStart.setTime(calNow.getTimeInMillis());

		StringBuilder  sqlSb= new StringBuilder();
		if(!isNewUser){
			sqlSb.append(" select a.id,a.title,a.summary,a.level, ");
			sqlSb.append(" case b.status when 1 then 1 else 0 end status , ");
			sqlSb.append(" a.created_at ,b.user_id  from nt_announcement  a , ");
			sqlSb.append(" nt_announcement_read  b ");
			sqlSb.append(" where  a.id = b.announcement_id ");
			sqlSb.append(" and a.role_type in  (select distinct(role_id) from user_station_role where user_id = ?) ");
			sqlSb.append(" and a.city_id = ? and a.created_at>? ");
			sqlSb.append(" and b.user_id = ?  order by a.created_at desc ");
		}else{
			sqlSb.append(" select a.id,a.title,a.summary,a.level, ");
			sqlSb.append(" 0 status , ");
			sqlSb.append(" a.created_at, 0 user_id  from nt_announcement a ");
			sqlSb.append(" where  a.role_type in  (select distinct(role_id) from user_station_role where user_id = ?)");
			sqlSb.append(" and a.city_id = ? ");
			sqlSb.append(" order by  a.created_at>? ");
		}
		 
		final Object[] params = new Object[]  {userId,cityId, selectStart, userId };
		final Object[] params2 = new Object[] {userId,cityId, selectStart };
		
		//如果已有阅读记录
		List rows = null ;
		List<AnnouncementVo> list = new ArrayList<AnnouncementVo>();
		
		if(!isNewUser){
				rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params);
		}else{
		        rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params2);
		}
		
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map announceMentMap = (Map) rows.get(i);
				AnnouncementVo annouceVo = new AnnouncementVo();
				annouceVo.setId(Integer.parseInt(announceMentMap.get("id").toString()));
				annouceVo.setTitle((announceMentMap.get("title").toString()));
				annouceVo.setSummary((announceMentMap.get("summary").toString()));
				annouceVo.setStatus(Integer.parseInt(announceMentMap.get("status").toString())); 
				Timestamp createTime = announceMentMap.get("created_at") ==null ? null:   (Timestamp)announceMentMap.get("created_at")  ; 
				annouceVo.setCreated_at (createTime ); 
				annouceVo.setUser_id(Integer.parseInt(announceMentMap.get("user_id").toString()));
				
				list.add(annouceVo);
			}
		}
			
		return list;
	}
	
	/**
	 * 获取公告详情
	 * @param userId
	 * @return
	 */
	public NtAnnouncement getAnnouncementById(int announcementId) {
		return this.selectOneOrNull(" where id = ?", announcementId);
	}
	
	
	/**
	 * 获取重要的公告
	 * @param userId
	 * @return
	 */
	public List<AnnouncementVo> getImporttantAnnouncementById(int cityId,int userId,boolean isNewUser) {
		Calendar calNow = Calendar.getInstance();
		calNow.setTimeInMillis(new Date().getTime());
		
		calNow.add(Calendar.DATE, -30);
		Date selectStart =new Date();
		selectStart.setTime(calNow.getTimeInMillis());
		
		StringBuilder  sqlSb= new StringBuilder();
		if(!isNewUser){
			sqlSb.append(" select a.id,a.title,a.content,a.popup_content ,a.level, ");
			sqlSb.append(" b.status   status , ");
			sqlSb.append(" a.created_at,b.user_id  from nt_announcement  a ,  ");
			sqlSb.append(" nt_announcement_read  b ");
			sqlSb.append(" where a.id = b.announcement_id  ");
			sqlSb.append(" and a.role_type  in  (select distinct(role_id) from user_station_role where user_id = ?) ");
			sqlSb.append(" and a.city_id = ? and a.created_at>? ");
			sqlSb.append(" and a.level = 1 and b.status = 0");
			sqlSb.append(" and b.user_id = ? order by a.created_at desc ");
		}else{
			sqlSb.append(" select a.id,a.title,a.content,a.popup_content ,a.level, ");
			sqlSb.append(" 0 status , ");
			sqlSb.append(" a.created_at, 0 user_id  from nt_announcement a");
			sqlSb.append(" where a.role_type in  (select distinct(role_id) from user_station_role where user_id = ?)  ");
			sqlSb.append(" and  a.city_id = ? and a.created_at>? ");
			sqlSb.append(" and a.level = 1 ");
			sqlSb.append(" order by a.created_at desc ");
		}
		
		final Object[] params  =  new Object[] { userId ,cityId, selectStart, userId };
		final Object[] params2 = new Object[] { userId ,cityId, selectStart };
		
		List rows = null;
		if(!isNewUser){
			rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params);
		}else{
			rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params2);
		}
		
		List<AnnouncementVo> list = new ArrayList<AnnouncementVo>();
		
		if (rows != null && rows.size() > 0) {
			for (int i = 0; i < rows.size(); i++) {
				Map announceMentMap = (Map) rows.get(i);
				AnnouncementVo annouceVo = new AnnouncementVo();
				annouceVo.setId(Integer.parseInt(announceMentMap.get("id").toString()));
				annouceVo.setTitle((announceMentMap.get("title").toString()));
				annouceVo.setContent(announceMentMap.get("content").toString());
				annouceVo.setPopup_content(announceMentMap.get("popup_content").toString()==null?"":announceMentMap.get("popup_content").toString());
				annouceVo.setStatus(Integer.parseInt(announceMentMap.get("status").toString())); 
				Timestamp createTime = announceMentMap.get("created_at") ==null ? null:   (Timestamp)announceMentMap.get("created_at")  ; 
				annouceVo.setCreated_at (createTime); 
				annouceVo.setUser_id(Integer.parseInt(announceMentMap.get("user_id").toString()));
				
				list.add(annouceVo);
			}
		}
		return list;
	}
	
	
	/**
	 * 获取用户所在的城市
	 * @param userId
	 * @return
	 */
	public int getCityByUserId(int userId) {

		StringBuilder  sqlSb= new StringBuilder();
		sqlSb.append(" select min(city_id) city_id from station where id in  ( ");
		sqlSb.append(" select station_id from user_station_role where user_id = ? ");
		sqlSb.append(" ) ");
		
		final Object[] params = new Object[] {userId };
		//如果已有阅读记录
		List rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params);
		int cityId = -1;
		
		if (rows != null && rows.size() > 0) {
				Map cityMap = (Map) rows.get(0);
				cityId = Integer.parseInt(cityMap.get("city_id").toString());
			}
		return cityId;
	}
	
	
	/**
	 * 获取所有用户未读的消息
	 * @param userId
	 * @return
	 */
	public int getUnReadCount(int userId) {
	    return   getReadRecord(userId,"unread");
	}
	
	
	private int getReadRecord(int userId,String type){
		
		StringBuilder  sqlSb= new StringBuilder();
		sqlSb.append(" select count(1) num from nt_announcement_read where  user_id= ? ");
		
		//0 是未读 2 是已发送 未读
		if("unread".equals(type)){
		sqlSb.append(" and status in (0,2) ");
		}
		
		final Object[] params = new Object[] {userId };
		//如果已有阅读记录
		List rows = this.jdbcTemplate.queryForList(sqlSb.toString(), params);
		int num = 0;
		
		if (rows != null && rows.size() > 0) {
				Map numMap = (Map) rows.get(0);
				num = Integer.parseInt(numMap.get("num").toString());
			}
		return num;
		
	}
	
	/**
	 * 返回用户的记录数
	 * @param userId
	 * @return
	 */
	public int getAllCountById(int userId){
			return   getReadRecord(userId,"all");
	}

	
}