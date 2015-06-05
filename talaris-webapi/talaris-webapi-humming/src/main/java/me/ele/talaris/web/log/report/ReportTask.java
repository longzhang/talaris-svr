package me.ele.talaris.web.log.report;

import me.ele.talaris.dao.InterfaceLogDao;
import me.ele.talaris.errorlog.processor.ErrorLogProcessor;
import me.ele.talaris.hermes.service.IHermesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author zhengwen
 *
 */
@Service
public class ReportTask implements InitializingBean {
    private final static Logger logger = LoggerFactory.getLogger(ReportTask.class);
    @Autowired
    InterfaceLogDao interfaceLogDao;
    @Autowired
    IHermesService coffeeHermesService;
    @Autowired
    ErrorLogProcessor errorLogProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    InterfaceLog interfaceLog = CountAspect.log_Queue.poll();
                    // 不为空，则写表
                    if (interfaceLog != null) {
                        try {
                            interfaceLogDao.insert(interfaceLog);
                        } catch (Exception e) {
                            logger.error("接口线程写数据失败", e);
                            errorLogProcessor.writeErrorInfo("接口线程写数据失败");
                            try {
                                coffeeHermesService.safeSendAlarm("记录接口线程异常", 1);
                            } catch (Exception e2) {
                                logger.error("记录接口线程异常且发送报警异常");
                                errorLogProcessor.writeErrorInfo(e);
                            }
                        }
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();
        logger.info("日志记录线程启动成功");
    }
}
