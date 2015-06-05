package me.ele.talaris.robot.status;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import me.ele.talaris.robot.util.PropertyUtil;
import me.ele.talaris.robot.util.ServiceUtil;

import javax.ws.rs.core.MediaType;
import java.util.logging.Logger;


/**
 * Created by Daniel on 15/5/11.
 */
public class RobotOrderStatusMgmt {
    public static String baseUrl = null;
    public static final String HEADER_TOKEN = "HTTP-ACCESS-TOKEN";
    public static String TOKEN_VALUE = null;
    public static Long SLEEP_TIME = null;



    public static Logger log = Logger.getLogger(RobotOrderStatusMgmt.class.getName());
    public static void main(String [] args){
        if(baseUrl == null){
            baseUrl = PropertyUtil.getServerEndpointInProperties() + PropertyUtil.getAutoConfirmWebServiceInProperties();
            log.info("AutoConfirm Webservice: " + baseUrl);
        }
        if(TOKEN_VALUE == null){
            TOKEN_VALUE = PropertyUtil.getAccessTokenInProperties();
            log.info("Access Token: " + TOKEN_VALUE);
        }

        if(SLEEP_TIME == null){
            SLEEP_TIME = PropertyUtil.getTimeIntervalInProperties();
            log.info("Sleep Time: " + SLEEP_TIME);
        }

//        TOKEN_VALUE = TOKEN_VALUE.replace("-", "\\-");

        while(true){
            Client c = ServiceUtil.getClient();
            WebResource resource = c.resource(baseUrl);

            log.info("request url:" + baseUrl);
            log.info("token:" + TOKEN_VALUE);

            try {
                String rs = resource.accept(MediaType.APPLICATION_JSON).header(HEADER_TOKEN, TOKEN_VALUE).post(new GenericType<String>() {
                });
                log.info("update order status robot run, result:" + rs);

                Thread.sleep(SLEEP_TIME);
            } catch (InterruptedException e) {
                log.info(e.toString());
            } catch (Exception e){
                log.info(e.toString());
            }
        }
    }
}
