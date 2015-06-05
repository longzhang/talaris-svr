/**
 * 
 */
package me.ele.talaris.deliveryorder.persistent.dao;

import java.sql.Timestamp;
import java.util.*;

import me.ele.talaris.constant.DeliveryOrderContant;
import me.ele.talaris.deliveryorder.dto.DeliveryOrder;
import me.ele.talaris.deliveryorder.persistent.eb.EBDeliveryOrder;
import me.ele.talaris.exception.ExceptionCode;
import me.ele.talaris.exception.ExceptionFactory;
import me.ele.talaris.exception.SystemException;
import me.ele.talaris.exception.UserException;
import me.ele.talaris.framework.dao.BaseSpringDao;
import me.ele.talaris.framework.dao.Criteria;
import me.ele.talaris.model.User;
import me.ele.talaris.utils.BeanCopyUtil;
import me.ele.talaris.utils.Pair;
import me.ele.talaris.utils.Times;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.CollectionUtils;

/**
 * @author
 *
 */
public class DeliveryOrderDao extends BaseSpringDao<EBDeliveryOrder> {
    private final static Logger logger = LoggerFactory.getLogger(DeliveryOrderDao.class);

    public DeliveryOrderDao() {
        super(new BeanPropertyRowMapper<EBDeliveryOrder>(EBDeliveryOrder.class));
    }

    public int insert(DeliveryOrder deliveryOrder){
        if(deliveryOrder == null)
            return 0;
        return  this.insert(convertDtoToEb(deliveryOrder, "insert"));
    }
    public int update(DeliveryOrder deliveryOrder){
        if(deliveryOrder == null)
            return 0;
        return  this.update(convertDtoToEb(deliveryOrder, "update"));
    }

    public List select(String where, Object... args){
        List<EBDeliveryOrder> ebs = super.select(where, args);
        if(ebs != null){
            return convertEbToDto(ebs, "select(...)");
        }
        return null;
    }
    public int addDeliveryOrder(DeliveryOrder newDeliveryOrder)  {
        try {
            EBDeliveryOrder eb = BeanCopyUtil.transform(newDeliveryOrder, EBDeliveryOrder.class);
            int result = insert(eb);
            return result;
        } catch (IllegalAccessException e) {
            logger.error("DeliveryOrderDao.addDeliveryOrder(...), eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error("DeliveryOrderDao.addDeliveryOrder(...), eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    public int updateDeliveryOrder(DeliveryOrder newDeliveryOrder){
        try{
            EBDeliveryOrder eb = BeanCopyUtil.transform(newDeliveryOrder, EBDeliveryOrder.class);
            int result = update(eb);
            return result;
        } catch (IllegalAccessException e) {
            logger.error("DeliveryOrderDao.updateDeliveryOrder(...), eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error("DeliveryOrderDao.updateDeliveryOrder(...), eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    /**
     * 通过deliveryId更改配送状态，mobile,status,userId,
     * 请注意该方法只能被变成配送中的逻辑调用
     * 
     * @param userId
     * @param mobile
     * @param status
     * @param deliveryId
     * @return
     */
    public int updateDeliveryOrderStatus(int stationId, int userId, long mobile, int status, long deliveryId) {
        int result = this.jdbcTemplate
                .update("update delivery_order set station_id = ? , status = ?,taker_id = ?, taker_mobile = ? , created_at= ? , updated_at = ?  where id = ? ",
                        stationId, status, userId, mobile, new Timestamp(System.currentTimeMillis()), new Timestamp(
                                System.currentTimeMillis()), deliveryId);
        return result;
    }

    public int updateDeliveryOrderStatus(long deliveryId, int status) {
        return this.jdbcTemplate.update(
                "update delivery_order set status = ?,  created_at= ? , updated_at = ?  where id = ? ", status,
                new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), deliveryId);
    }

    /**
     * 根据配送单号查询配送单是否存在
     * 
     * @param status
     * @param deliveryId
     * @return
     */
    public boolean isDeliveryOrderExist(int deliveryOrderId) {
        String sql = "select ele_order_id from delivery_order where id = ? ";
        Map map = jdbcTemplate.queryForMap(sql);
        if (CollectionUtils.isEmpty(map)) {
            return false;
        }

        return true;
    }

    /**
     * 根据站点ID获得该站点下的配送单信息
     * 
     * @param status
     * @param deliveryId
     * @return
     */
    public List<DeliveryOrder> listDeliveryOrdersByStationId(int station_id) {
        List<EBDeliveryOrder> ebList = this.select("where station_id = ?  and status = ?", station_id, 1);
        List<DeliveryOrder> orders = new ArrayList<>();
        if(ebList!= null){
            return convertEbToDto(ebList, "listDeliveryOrdersByStationId(...)");
        }else {
            return null;
        }
    }

    /**
     * 根据饿单号查询配送单信息
     * 
     * @param status
     * @param deliveryId
     * @return
     */
    public DeliveryOrder getDeliveryOrderByEleOrderId(long eleOrderId)  {
        EBDeliveryOrder eb = this.selectOneOrNull("where ele_order_id = ?", eleOrderId);
        if(eb != null){
            return convertEbToDto(eb, "getDeliveryOrderByEleOrderId(...)");
        }
        return null;
    }

    /**
     * 根据配送单号和站点来查询配送单
     * 
     * @param status
     * @param deliveryId
     * @return
     */
    public DeliveryOrder getDeliveryOrderByIdAndStationId(long id, int stationId) {
        EBDeliveryOrder eb = this.selectOneOrNull("where id = ?  and station_id = ?", id, stationId);
        if(eb != null){
            return convertEbToDto(eb, "getDeliveryOrderByIdAndStationId(...)");
        }
        return null;
    }

    /**
     * 根据饿单号查询配送单信息
     * 
     * @param status
     * @param deliveryId
     * @return
     */
    public DeliveryOrder getWaitToDeliveryDeliveryOrderByEleOrderId(long eleOrderId) {
        EBDeliveryOrder eb = this.selectOneOrNull("where ele_order_id = ?  and status = 1", eleOrderId);
        if(eb != null){
            return convertEbToDto(eb, "getWaitToDeliveryDeliveryOrderByEleOrderId(...)");
        }
        return null;
    }

    /**
     * 根据站点来查询该站点下待配送的配送单
     * 
     * @param stationId
     * @return
     */
    public List<DeliveryOrder> getWaitToDeliveryDeliveryOrderByStationId(int stationId) {
        List<EBDeliveryOrder> ebs = this.select("where station_id = ? and  status = 1 ", stationId);
        List<DeliveryOrder>orders = new ArrayList<>();
        if(ebs != null){
            return convertEbToDto(ebs, "getWaitToDeliveryDeliveryOrderByStationId(...)");
        }
        return null;
    }

    /**
     * 
     * @param status
     * @param taker
     * @param passed_by
     * @param payment_type
     * @param start
     * 如果不传，默认为12小时内
     * @param end
     * 如果不传，默认为当前时间
     * @return
     */
    public List<DeliveryOrder> getDeliveryOrdersByFilter(Integer status, Integer taker, Integer passed_by,
            Integer payment_type, Timestamp start, Timestamp end) {
        if (start == null)
            start = new Timestamp(System.currentTimeMillis() - 12 * 1000 * Times.ONE_HOUR);
        if (end == null)
            end = new Timestamp(System.currentTimeMillis());
        logger.debug("start{},end {}", start, end);
        if (start.getTime() > end.getTime())
            return Collections.emptyList();

        Criteria criteria = new Criteria();
        criteria.and("d.created_at >= ?", start).and("d.created_at <= ?", end);
        if (status != null)
            criteria.and("d.status=?", status);
        if (taker != null)
            criteria.and("d.taker_id=?", taker);
        if (payment_type != null)
            criteria.and("d.payment_type=?", payment_type);
        if (passed_by == null) {
            List<Object> params = criteria.getParameterList();

            List<EBDeliveryOrder> ebs = this.jdbcTemplate.query(this.sql_selectAllColumnsClause + " d where " + criteria.getClause(),
                    params.toArray(new Object[params.size()]), this.rowMapper);
            if(ebs != null){
                return convertEbToDto(ebs, "getDeliveryOrdersByFilter(...)");
            }
            return null;

        }
        criteria.and("r.operator_id=?", passed_by);
        List<Object> params = criteria.getParameterList();

        List<EBDeliveryOrder> ebs = this.jdbcTemplate.query(this.sql_selectAllColumnsClause
                        + " d right join delivery_order_record r on d.id=r.delivery_order_id where " + criteria.getClause(),
                params.toArray(new Object[params.size()]), this.rowMapper);
        if(ebs != null){
            return convertEbToDto(ebs, "getDeliveryOrdersByFilter(...)");
        }
        return null;
    }

    public List<DeliveryOrder> getDeliveryOrdersByEleIdList(List<Long> eleOrderIdList) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("eleOrderIdList", eleOrderIdList);

        List<EBDeliveryOrder>ebs = namedParameterJdbcTemplate.query(this.sql_selectAllColumnsClause
                + " where ele_order_id in (:eleOrderIdList) ", parameters, this.rowMapper);
        if(ebs != null){
            return convertEbToDto(ebs, "getDeliveryOrdersByEleIdList(...)");
        }
        return null;

    }

    public List<DeliveryOrder> getDeliveryOrdersByIdList(List<Long> idList) {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("idList", idList);

        List<EBDeliveryOrder> ebs = namedParameterJdbcTemplate.query(this.sql_selectAllColumnsClause + " where id in (:idList) ",
                parameters, this.rowMapper);
        if(ebs != null){
            return convertEbToDto(ebs, "getDeliveryOrdersByIdList(...)");
        }
        return null;
    }

    public DeliveryOrder getDeliveryOrdersById(Long orderId) {
        EBDeliveryOrder eb = this.selectOneOrNull(" where id = ? ", orderId);
        if(eb != null){
            return convertEbToDto(eb, "getDeliveryOrdersById(...)");
        }
        return null;
    }

    /**
     * 
     * @param users
     * @param lastSettleMaxId
     * 上次结算时的配送单最大ID。第一次结算的时候为null或者传0
     * @return
     */
    public Map<String, Object> getTakerNoSettleCount(User user, int rstId, Timestamp lastSettleTime) {
        String sql = "select taker_id,sum(total_amount) as no_settle_count from delivery_order where taker_id= ?  and rst_id= ?  and created_at > ? and payment_type= ? and status != ?";
        Map<String, Object> result = this.jdbcTemplate.queryForMap(sql, user.getId(), rstId, lastSettleTime,
                DeliveryOrderContant.CASH_ON_DELIVERY, DeliveryOrderContant.WAITTO_DELIVERY);
        return result;
    }

    public Long getDifferentPaymentOrdersCount(int taker_id, int rst_id, int payment, int status,
            Timestamp lastSettleTime) {
        String sql = "select count(1) from delivery_order where taker_id = ? and rst_id = ?  and payment_type = ? and status = ? and created_at > ?";
        Long count = this.jdbcTemplate.queryForLong(sql, taker_id, rst_id, payment, status, lastSettleTime);
        return count;
    }

    /**
     * 获取下次配送员配送单List集合
     * 
     * @param takerId
     * @param Id
     * @return
     */
    public List<DeliveryOrder> getTheNextDeliveryOrderList(int takerId, Timestamp timestamp) {
        List<EBDeliveryOrder> ebs = this.select("where taker_id = ? and created_at >= ?", takerId, timestamp);
        if(ebs != null){
            return convertEbToDto(ebs, "getTheNextDeliveryOrderList(...)");
        }
        return null;
    }

    /**
     * 初始化获取配送员配送单List集合(当天凌晨往前推)
     * 
     * @param takerId
     * @param Id
     * @return
     */
    public List<DeliveryOrder> getInitDeliveryOrderList(int takerId, String currentDate) {
        List<EBDeliveryOrder> ebs = this.select("where taker_id = ? and created_at < ?", takerId, currentDate);
        if(ebs != null){
            return convertEbToDto(ebs, "getInitDeliveryOrderList(...)");
        }
        return null;

    }

    /**
     * 分页查询某个配送员某种状态且未结算的配送单
     * 
     * @param pageNow
     * @param pageSize
     * @param takerId
     * @param status
     * @param start
     * @return
     */
    public Pair<List<DeliveryOrder>, Long> selectPageOneTakerOneStatusDeliveryOrder(int rst_id, int pageNow,
            int pageSize, int takerId, int status, Timestamp start) {
        Pair<List<EBDeliveryOrder>, Long> ebPair = this.selectPage(
                "where taker_id= ? and rst_id = ?  and status= ? and created_at > ?", pageNow, pageSize, takerId,
                rst_id, status, start);
        if(ebPair != null){
            return convertEbToDto(ebPair, "selectPageOneTakerOneStatusDeliveryOrder(...)");
        }
        return null;
    }

    /**
     * 分页查询某个配送员某种支付方式某种状态且未结算的配送单
     * 
     * @param pageNow
     * @param pageSize
     * @param takerId
     * @param status
     * @param payment
     * @param start
     * @return
     */
    public Pair<List<DeliveryOrder>, Long> selectPageOneTakerOneStatusOnePaymentDeliveryOrder(int rst_id, int pageNow,
            int pageSize, int takerId, int status, int payment, Timestamp start) {
        Pair<List<EBDeliveryOrder>, Long> ebPair = this.selectPage(
                "where taker_id= ? and rst_id = ? and status= ? and payment_type = ? and created_at > ? ", pageNow,
                pageSize, takerId, rst_id, status, payment, start);
        if(ebPair != null){
            return convertEbToDto(ebPair, "selectPageOneTakerOneStatusOnePaymentDeliveryOrder(...)");
        }
        return null;
    }

    public List<DeliveryOrder> listOneStatusOnePaymentTypeDeliveryOrders(int status, int takerId, int paymentType,
            Timestamp start) {
        List<EBDeliveryOrder> ebs = this.select("where status = ?  and taker_id = ?  and payment_type = ? and created_at > ?", status,
                takerId, paymentType, start);
        if(ebs != null){
            return convertEbToDto(ebs, "listOneStatusOnePaymentTypeDeliveryOrders(...)");
        }
        return null;
    }

    /**
     * 查看某个时间段之前还在配送中的配送单
     * 
     * @param taker_id
     * @param lastSettleTime
     */
    public List<DeliveryOrder> selectDeliveryingOrdersInExpiredHours(int hours, int taker_id, Timestamp lastSettleTime) {

        Timestamp Hour12Ago = new Timestamp(System.currentTimeMillis() - hours * 60l * 60 * 1000);
        logger.debug("{}", Hour12Ago);
        logger.debug("{}", lastSettleTime);
        if (lastSettleTime.before(Hour12Ago)) {
            logger.debug("{}", lastSettleTime);
            List<EBDeliveryOrder> ebs = this.select("where taker_id = ? and status = ? and created_at > ? and created_at < ?", taker_id,
                    DeliveryOrderContant.DELIVERYING, lastSettleTime, Hour12Ago);
            if(ebs != null){
                return convertEbToDto(ebs, "selectDeliveryingOrdersInExpiredHours(...)");
            }
        } else {
            logger.debug("{}", lastSettleTime);
            List<EBDeliveryOrder> ebs = this.select("where taker_id = ? and status = ? and created_at < ?", taker_id,
                    DeliveryOrderContant.DELIVERYING, lastSettleTime);
            if(ebs != null){
                return convertEbToDto(ebs, "selectDeliveryingOrdersInExpiredHours(...)");
            }
        }

        return  null;
    }

    public Map<Integer, List> getDeliveryOrdersByRstIdList(List<Integer> rstIds) {
        Map<Integer, List> rs = new HashMap<>();
        if (rstIds == null)
            return rs;

        for (Integer rstId : rstIds) {
            List orderList = getDeliveryOrdersByRstId(rstId, null);
            rs.put(rstId, orderList);
        }

        return rs;
    }

    public List<DeliveryOrder> getDeliveryOrdersByRstId(int rstId, Integer status) {
        List<EBDeliveryOrder> ebs;
        if (status == null) {
            ebs = this.select("where rst_id = ?", rstId);
        } else {
            ebs = this.select("where rst_id = ? and status = ?", rstId, status);
        }

        if(ebs != null){
            return convertEbToDto(ebs, "getDeliveryOrdersByRstId(...)");
        }
        return null;
    }

    public List<DeliveryOrder> getRstWaiToDeliveryOrder(int rstId) {
        Timestamp Hour48Ago = new Timestamp(System.currentTimeMillis() - 48 * 60l * 60 * 1000);
        List<EBDeliveryOrder> ebs = this.select("where rst_id = ? and status = 1 and created_at > ?", rstId, Hour48Ago);

        if(ebs != null){
            return convertEbToDto(ebs, "getRstWaiToDeliveryOrder");
        }
        return null;
    }

    private DeliveryOrder convertEbToDto(EBDeliveryOrder eb, String methodName) throws SystemException{
        try{
            DeliveryOrder order = BeanCopyUtil.transform(eb, DeliveryOrder.class);
            return order;
        } catch (IllegalAccessException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    private EBDeliveryOrder convertDtoToEb(DeliveryOrder deliveryOrder, String methodName) throws SystemException{
        try{
            EBDeliveryOrder eb = BeanCopyUtil.transform(deliveryOrder, EBDeliveryOrder.class);
            return eb;
        } catch (IllegalAccessException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    private List<EBDeliveryOrder> convertDtoToEb(List<DeliveryOrder> deliveryOrders, String methodName) throws SystemException{
        try{
            List<EBDeliveryOrder> ebs = BeanCopyUtil.transform(deliveryOrders, EBDeliveryOrder.class);
            return ebs;
        } catch (IllegalAccessException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    private List<DeliveryOrder> convertEbToDto(List<EBDeliveryOrder> ebs, String methodName) throws SystemException{
        try{
            List<DeliveryOrder> orders = BeanCopyUtil.transform(ebs, DeliveryOrder.class);
            return orders;
        } catch (IllegalAccessException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        } catch (InstantiationException e) {
            logger.error(DeliveryOrderDao.class.getName() + "." + methodName + ", eb-dto 转换报错:" + e);
            throw ExceptionFactory.newSystemException(ExceptionCode.DELIVERY_ORDER_ERROR_610);
        }
    }

    private Pair<List<DeliveryOrder>, Long> convertEbToDto(Pair<List<EBDeliveryOrder>, Long> ebPair, String methodName) throws SystemException{
        List<EBDeliveryOrder>ebs = ebPair.first;
        List<DeliveryOrder> orders = new ArrayList<>();

        if(ebs != null){
            for(EBDeliveryOrder eb : ebs){
                orders.add(convertEbToDto(eb, methodName));
            }
            return new Pair<>(orders, ebPair.second);
        }

        return null;
    }

    public void batchDeliveryOrderInsert(List<DeliveryOrder> orders) {
        if(orders != null){
            List<EBDeliveryOrder> es = convertDtoToEb(orders, "batchInsert.(...)");
            super.batchInsert(es);
        }
    }

    public Long getDelvieryTimesByCustomerMobile(Long mobile, int orderSrc){
        StringBuffer sb = new StringBuffer();
        sb.append("select count(1) from delivery_order where status = " + DeliveryOrderContant.DELIVERIED);
        sb.append(" and receiver_mobile = ?");
        sb.append(" and source = ?");

        Long count = this.jdbcTemplate.queryForLong(sb.toString(), mobile, orderSrc);

        return count;
    }

    public DeliveryOrder getDeliveryOrder(Long mobile, int orderSrc, int status){
        StringBuffer sb = new StringBuffer();
        sb.append("where receiver_mobile = ?");
        sb.append(" and status = ?");

        EBDeliveryOrder eb = this.selectOneOrNull(sb.toString(), mobile, status);
        if(eb != null)
            return convertEbToDto(eb, "getDeliveryOrder(...)");
        return null;
    }
}
