package me.ele.talaris.webapi.notice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ele.talaris.dto.AnnoucementImportant;
import me.ele.talaris.dto.AnnoucementSummery;
import me.ele.talaris.dto.AnnouncementVo;
import me.ele.talaris.dto.AnnouncementVoBasic;
import me.ele.talaris.dto.AnnouncementVoContent;
import me.ele.talaris.exception.NoticeException;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.model.Context;
import me.ele.talaris.service.IAnnouncementService;
import me.ele.talaris.service.auth.IUserDeviceService;
import me.ele.talaris.service.station.IStationService;
import me.ele.talaris.service.user.impl.UserStationRoleService;
import me.ele.talaris.web.framework.ResponseEntity;
import me.ele.talaris.web.framework.WebAPIBaseController;
import me.ele.talaris.web.log.report.InterfaceMonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/webapi/")
@Transactional
public class NoticeController extends WebAPIBaseController {
	
    @Autowired
    IAnnouncementService announcementService;
    
    @Autowired
    IUserDeviceService userDeviceService;
    
    @Autowired
    UserStationRoleService userStationRoleService;
    
    @Autowired
    IStationService stationService;
    
    private final static Logger logger = LoggerFactory.getLogger(NoticeController.class);
    

    /**
     * 获取公告
     * 
     * @param context
     * @param stationId
     * @param roleID
     * @return  
     * @throws UserException
     */
    @RequestMapping(value = "announcement")
    @InterfaceMonitor(interfaceName = "获取公告", contextIndex = 0)
    public @ResponseBody ResponseEntity<AnnoucementSummery> getAnnouncements(Context context
    		 ) throws NoticeException {
        ResponseEntity<AnnoucementSummery> responseEntity = null;
        int userId = context.getUser().getId() ;
        int cityId = announcementService.getCityByUserId(userId);
        boolean isNewUser = false;
        int allCount = announcementService.getAllCountById(userId);
        if(allCount==0){
        	isNewUser = true;
        }
        
        logger.info("获取公告:"+userId +" cityId:"+cityId);
        List<AnnouncementVo> announcementList = announcementService.getAnnouncementByUserId(userId,cityId,isNewUser);
        AnnoucementSummery annoucementSummery = new AnnoucementSummery();
        annoucementSummery.setSys_time(new Date().getTime());
        List<AnnouncementVoBasic> basicList = new ArrayList<AnnouncementVoBasic>();
        if(announcementList!=null && announcementList.size()>0){
        	for(AnnouncementVo anncementVo: announcementList){
             AnnouncementVoBasic aBasic = new AnnouncementVoBasic();
             aBasic.setId(anncementVo.getId());
             aBasic.setTitle(anncementVo.getTitle());
             aBasic.setSummary(anncementVo.getSummary());
             aBasic.setStatus(anncementVo.getStatus());
             aBasic.setCreated_at(anncementVo.getCreated_at().getTime());
             aBasic.setCreated_at(anncementVo.getCreated_at().getTime());
             basicList.add(aBasic);
        	}
        }
        annoucementSummery.setList(basicList);
        annoucementSummery.setSys_time(new Date().getTime());
        responseEntity = new ResponseEntity<AnnoucementSummery>("200", "", annoucementSummery);
        return responseEntity;
    }

    /**
     * 获取公告详情
     * 
     * @param context
     * @param id
     * @return
     * @throws SystemException
     * @throws UserException
     */
    
    @RequestMapping(value = "announcement/{id}")
    @InterfaceMonitor(interfaceName = "获取公告详情信息", contextIndex = 0)
    public @ResponseBody ResponseEntity<AnnouncementVoContent> getAnnouncementById(Context context,
            @PathVariable("id") int id)
            throws SystemException, NoticeException {
        int userId = context.getUser().getId() ;
        logger.info("获取公告详情:"+userId +" id:"+id);
        AnnouncementVoContent aContent = announcementService.getAnnouncementById(userId,id);
        return ResponseEntity.success(aContent);
    }

    /**
     * 未读通知/重要公告
     * 
     * @return
     * @throws UserException
     */
    @RequestMapping(value = "announcement/unread")
    @InterfaceMonitor(interfaceName = "", contextIndex = 0)
    public @ResponseBody ResponseEntity<AnnoucementImportant> getImporttantAnnouncementList(Context context) throws NoticeException {
        ResponseEntity<AnnoucementImportant> responseEntity = null;
        int userId = context.getUser().getId() ;
        int cityId = announcementService.getCityByUserId(userId);
        logger.info("未读通知/重要公告:"+userId +" cityId:"+cityId);
        
        boolean isNewUser = false;
        int allCount = announcementService.getAllCountById(userId);
        if(allCount==0){
        	isNewUser = true;
        }
        
        //新用户初始化数据,以后会单独出来
        List<AnnouncementVo>  annList  = null;
        if(isNewUser){
          annList =  announcementService.getAnnouncementByUserId(userId,cityId,isNewUser);
        }
        
        //1.取得数据来源
    	List<AnnouncementVo>  announcementList  =  announcementService.getImporttantAnnouncementById(cityId,userId,isNewUser);
    	
    	AnnoucementImportant aImportant = new AnnoucementImportant();
    	
    	//2.转换成前端要显示的数据类型
        List<AnnouncementVoContent> contentList = new ArrayList<AnnouncementVoContent>();
        if(announcementList!=null && announcementList.size()>0){
        	for(AnnouncementVo anncementVo: announcementList){
        		AnnouncementVoContent aCotent = new AnnouncementVoContent();
        		aCotent.setId(anncementVo.getId());
        		aCotent.setTitle(anncementVo.getTitle());
        		aCotent.setContent(anncementVo.getPopup_content()==null ?"":anncementVo.getPopup_content());
        		aCotent.setStatus(anncementVo.getStatus());
        		aCotent.setCreated_at(anncementVo.getCreated_at().getTime());
                contentList.add(aCotent);
                //目前只需取第一条
                break;
        	}
        }
        
        //3.搜索未读的结果数
        int num = announcementService.getUnReadCount(userId);
        logger.info("未读通知的数量:"+num);
        if(isNewUser){
        	if(annList!=null && annList.size()>0){
        			num = annList.size();
        	}
        }
        aImportant.setCount(num);
        aImportant.setPopups(contentList);
        responseEntity = new ResponseEntity<AnnoucementImportant>("200", "", aImportant);
        return responseEntity;
    }

    
}