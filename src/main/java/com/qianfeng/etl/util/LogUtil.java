package com.qianfeng.etl.util;

import com.qianfeng.common.EventLogConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每一行的日志解析工具
 */
public class LogUtil {

    private static Logger logger = Logger.getLogger(LogUtil.class);

    /**
     * 单行日志解析
     *
     * @param log  192.168.216.1^A1256798789.123^A192.168.216.111^1.png?en=e_l&ver=1&u_ud=679f-dfsa-u789-dfaa
     * @return
     */
    //单行日志的解析
    public static Map<String, String> handleLog(String log) {
        Map<String, String> info = new ConcurrentHashMap<String, String>();
        // 判断log是否为空
        if (StringUtils.isNotEmpty(log.trim())) {
            //拆分单行日志
            String[] fields = log.split(EventLogConstants.LOG_SPARTOR);
            if (fields.length == 4) {
                //存储数据到info中
                info.put(EventLogConstants.EVENT_COLUMN_NAME_IP, fields[0]);
                info.put(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME,
                        fields[1].replaceAll("\\.", "")); // 时间戳解析
                // 将参数列表中的k-v解析存储到info
                // 获取?之后的参数列表
                int index = fields[3].indexOf("?");

                //处理参数列表
                handleParams(info, fields[3]);
                //处理ip
                handleIp(info);
                //处理useragent
                handleUserAgent(info);
            } else {
                info.clear();
            }
        }


        return null;
    }

    /**
     * 处理useragent
     */
    private static void handleUserAgent(Map<String, String> info) {
        if (info.containsKey(EventLogConstants.EVENT_COLUMN_NAME_USERAGENT)) {
            UserAgentUtil.UserAgentInfo ua = UserAgentUtil.parseUserAgent(info.get(EventLogConstants.EVENT_COLUMN_NAME_USERAGENT));
            if (ua != null) {
                info.put(EventLogConstants.EVENT_COLUMN_NAME_BROWSER_NAME, ua.getBrowserName());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_BROWSER_VERSION, ua.getBrowserVersion());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_OS_NAME, ua.getOsName());
                info.put(EventLogConstants.EVENT_COLUMN_NAME_OS_VERSION, ua.getOsVersion());
            }
        }
    }

    private static void handleIp(Map<String, String> info) {

    }

    /**
     * 处理参数
     *
     * @param info
     * @param field
     */
    private static void handleParams(Map<String, String> info, String field) {
        if (StringUtils.isNotEmpty(field)) {
            int index = field.indexOf("?");
            if (index > 0) {
                String fields = field.substring(index + 1);
                String[] params = fields.split("&");
                for (String param : params) {
                    try {
                        String[] kvs = param.split("=");
                        String k = URLDecoder.decode(kvs[0], "utf-8");
                        String v = URLDecoder.decode(kvs[1], "utf-8");
                        if (StringUtils.isNotEmpty(k) && StringUtils.isNotEmpty(v)) {
                            info.put(k, v);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
