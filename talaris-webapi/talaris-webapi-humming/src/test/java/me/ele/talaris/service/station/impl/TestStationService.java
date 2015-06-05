//package me.ele.talaris.service.station.impl;
//
//import me.ele.talaris.exception.SystemException;
//import me.ele.talaris.model.Context;
//import me.ele.talaris.model.Station;
//import me.ele.talaris.model.User;
//import me.ele.talaris.service.station.IStationService;
//import me.ele.talaris.service.station.dto.RetailerWithOrderInfo;
//import me.ele.talaris.service.user.IUserService;
//import me.ele.talaris.utils.SerializeUtil;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.sql.Timestamp;
//import java.util.List;
//
///**
// * Created by Daniel on 15/5/12.
// */
//
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:context-talaris-svr.xml", "classpath:coffee-hermes.xml", "classpath:talaris-napos.xml"})
//public class TestStationService {
//    private Logger logger = LoggerFactory.getLogger(TestStationService.class);
//    private Context context;
//
//    @Resource
//    private IStationService stationService;
//    @Resource
//    private IUserService userService;
//
//
//    @Before
//    public void prepare() {
//        if (context == null) {
//            context = new Context();
//        }
//    }
//
//    @Test
//    public void testGetStationByStationId() throws SystemException{
//        logger.debug("----------- Test Unit: StationService.getRetailerWithOrderInfoListByDelivererId(...) ----------");
//
//        List<RetailerWithOrderInfo> rsts = stationService.getRetailerWithOrderInfoListByDelivererId(context, 1, 1);
//        for(RetailerWithOrderInfo rst : rsts){
//            print(rst);
//        }
//    }
//
//    @Test
//    public void testAddDelivererToRetailer(){
//        logger.debug("----------- Test Unit: StationService.addDelivererToRetailer(...) ----------");
//        User user = userService.getUserByUserId(9);
//        Station retailer = stationService.createStationByUser(user);
//
//        Integer testUserId = 11;
//        Integer testRetailerId = 2;
//        stationService.addDelivererToRetailer(context, testUserId, testRetailerId);
//
//        List<RetailerWithOrderInfo> rsts = stationService.getRetailerWithOrderInfoListByDelivererId(context, testUserId, 1);
//        for(RetailerWithOrderInfo rst : rsts){
//            print(rst);
//        }
//    }
//
//    private void print(Object obj){
//        logger.debug(SerializeUtil.beanToJson(obj));
//    }
//
//
//    private User FakeUser(){
//        User testUser = new User();
//        testUser.setName("fake user");
//        testUser.setMobile(13811111111l);
//        testUser.setEmail("");
//        testUser.setCertificate_number("");
//        testUser.setLongitude(new BigDecimal(0));
//        testUser.setLatitude(new BigDecimal(0));
//        testUser.setCreated_at(new Timestamp(System.currentTimeMillis()));
//        testUser.setUpdated_at(new Timestamp(System.currentTimeMillis()));
//
//        return testUser;
//    }
//}
