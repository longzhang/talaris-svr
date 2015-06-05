//package me.ele.talaris.service.station.impl;
//
//import me.ele.talaris.exception.SystemException;
//import me.ele.talaris.model.*;
//import me.ele.talaris.deliveryorder.dto.DeliveryOrderExInfo;
//import me.ele.talaris.service.deliveryorder.ICreateDeliveryOrderService;
//import me.ele.talaris.service.deliveryorder.IGetDeliveryOrderService;
//import me.ele.talaris.service.station.IStationService;
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
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by eleme on 15/5/12.
// */
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:context-talaris-svr.xml", "classpath:coffee-hermes.xml", "classpath:talaris-napos.xml"})
//public class TestOrderService {
//    private Logger logger = LoggerFactory.getLogger(TestOrderService.class);
//    private Context context;
//
//    @Resource
//    private ICreateDeliveryOrderService createDeliveryOrderService;
//    @Resource
//    private IGetDeliveryOrderService getDeliveryOrderService;
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
//    public void testCreateTestOrderForAutoUpdate() throws SystemException {
//        Calendar calendar = Calendar.getInstance();
//        int hour = calendar.get(Calendar.HOUR);
//        int min = calendar.get(Calendar.MINUTE);
//
//        List<DeliveryOrder> orderList = new ArrayList<>();
//
//        for(int i = 0; i < 60; i++){
//            Calendar orderUpdateTime = Calendar.getInstance();
//            orderUpdateTime.set(Calendar.HOUR, hour - 3);
//            orderUpdateTime.set(Calendar.MINUTE, min - i);
//            orderList.add(getOrder(new Timestamp(orderUpdateTime.getTime().getTime())));
//        }
//
//        createDeliveryOrderService.addDeliveryOrder(context, orderList);
//    }
//
//    @Test
//    public void testAddRedisTestOrder(){
//        getDeliveryOrderService.testAddRedisTestOrder();
//    }
//
//    @Test
//    public void testGetRedisOrder(){
//        Map<String, String> rs = getDeliveryOrderService.testGetRedisOrder();
//        int i = 0;
//        for(Map.Entry<String, String> entry : rs.entrySet()){
//            DeliveryOrderExInfo exInfo = SerializeUtil.jsonToBean(entry.getValue(), DeliveryOrderExInfo.class);
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String str = df.format(exInfo.getStatus_change_time());
//
//            logger.debug( i++ + "-order: " + entry.getKey() + " " + str);
//        }
//    }
//
//    public DeliveryOrder getOrder(Timestamp updatedAt){
//        DeliveryOrder order = new DeliveryOrder();
//        order.setReceiver_name("tst receiver name");
//        order.setReceiver_mobile(13800000000l);
//        order.setReceiver_address("地球");
//        order.setStation_id(0);
//        order.setStatus(2);
//        order.setTotal_amount(new BigDecimal(10));
//        order.setPaied_amount(new BigDecimal(10));
//        order.setPayment_type(0);
//        order.setCreated_at(new Timestamp(System.currentTimeMillis()));
//        order.setUpdated_at(updatedAt);
//
//        return order;
//    }
//}
