package me.ele.talaris.robot.util;

import me.ele.talaris.robot.SystemConstants;

import java.io.FileInputStream;
import java.io.InputStream;

import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by Daniel on 15/5/20.
 */
public class PropertyUtil {
    private static Logger logger = Logger.getLogger(PropertyUtil.class.getName());
    private static String configDir;
    private static String talarisAppDir;
    private static final String SERVER_ENDPOINT_PROP_NAME         = "ServerEndpoint";
    private static final String AUTO_CONFIRM_WEBSERVICE_PROP_NAME = "AutoConfirmWebService";
    private static final String ACCESS_TOKEN_PROP_NAME            = "AccessToken";
    private static final String TIME_INTERVAL_PROP_NAME           = "TimeInterval";

    private static final String DEFAULT_ACCESS_TOKEN_VALUE = "0993d700-f87f-11e4-90fe-4054e479b41b";
    private static final Long   DEFAULT_TIME_INTERVAL_VALUE = 10800000l;

    static{
        try{
            Properties globalProps = getProperties(SystemConstants.GLOBAL_PROPERTIES);
            configDir = globalProps.getProperty(SystemConstants.CONFIG_HOME_DIR);
            talarisAppDir = globalProps.getProperty(SystemConstants.ROBOTSERVICE_DIR);
            logger.info("PropertyUtil.globalProps="+globalProps);
            logger.info("PropertyUtil.configDir="+configDir);
            logger.info("PropertyUtil.talarisAppDir="+talarisAppDir);

        }catch(Exception e){
            logger.info("Failed to initialize" + e);
        }
    }
    public static String getConfigDir(){
        return configDir;
    }    
    public static String getTalarisAppDir(){
        return talarisAppDir;
    }    
    
    public static Properties getConfigProperties(String filename){
        return PropertyUtil.getProperties(getConfigDir() + "/" + filename);
    }

    public static Properties getTalarisAppProperties(String filename){
        return PropertyUtil.getProperties(getTalarisAppDir() + "/" + filename);
    }

    public static Properties getStatusRobotProperties(){
        Properties prop = getTalarisAppProperties(SystemConstants.STATUS_ROBOT_PROPERTIES);
        return prop;
    }

    public static String getServerEndpointInProperties(){
        Properties prop = PropertyUtil.getStatusRobotProperties();
        String serverEndpoint = prop.getProperty(SERVER_ENDPOINT_PROP_NAME);
        return serverEndpoint;
    }

    public static String getAutoConfirmWebServiceInProperties(){
        Properties prop = PropertyUtil.getStatusRobotProperties();
        String autoConfirmWebservice = prop.getProperty(AUTO_CONFIRM_WEBSERVICE_PROP_NAME);
        return autoConfirmWebservice;
    }

    public static String getAccessTokenInProperties(){
        Properties prop = PropertyUtil.getStatusRobotProperties();
        String accessToken = prop.getProperty(ACCESS_TOKEN_PROP_NAME);
        return accessToken;
    }

    public static Long getTimeIntervalInProperties(){
        Properties prop = PropertyUtil.getStatusRobotProperties();
        Long accessToken = Long.valueOf(prop.getProperty(TIME_INTERVAL_PROP_NAME));
        return accessToken;
    }

    public static Properties getProperties(String filename) {        
        Properties props = null;
        InputStream stream = null;
        try{

                if(filename.startsWith("/")){
                    stream = new FileInputStream(filename);
                    props = new Properties();
                    props.load(stream);
                }else{
                    ClassLoader loader = Thread.currentThread().getContextClassLoader();
                    stream = loader.getResourceAsStream(filename);
                    props = new Properties();
                    props.load(stream);            
                }
                logger.info("PropertyUtil.getLocal."+filename+" = "+props);
        }catch(Exception e){
            logger.info("PropertyUtil.getProperties: " + e);
            e.printStackTrace();
        }finally{
            if(stream!=null)
                try{ stream.close();}catch(Exception ex){}
        }
        return props;
    }    

    /**
     * Get property as String.
     */
    public static String getProperty ( Properties _props, String _key )
    {
            return getProperty(_props, _key, null);
    }

    /**
     * Get property as String.
     */
    public static String getProperty ( Properties _props, String _key, String _defValue )
    {
            if ( _props == null || _key == null )
                    return _defValue;
            String val = _props.getProperty( _key, _defValue );    
            return val;
    }    

    
}
