package com.qianfeng.common;

/**
 * @Created by MaoMao on 2018/8/17
 * @Description 日志的常量类
 */
public class EventLogConstants {
    /**
     * 事件的枚举：Enumerate
     */
    public static enum EventEnum {
        LAUNCH(1, "launch event", "e_l"), // 开始事件
        PAGEVIEW(2, "page view event", "e_pv"), // 网页浏览事件
        CHARGEREQUEST(3, "charge request event", "e_crt"), // 提交订单事件
        CHARGESUCCESS(4, "charge success event", "e_cs"), // 订单提交成功事件
        CHARGEREFUND(5, "charge refund event", "e_cr"), // 订单退回事件
        EVENT(6, "event", "e_e");

        public final int id; // 事件的id
        public final String name;
        public final String alias; // 事件的别名

        EventEnum(int id, String name, String alias) {
            this.id = id;
            this.name = name;
            this.alias = alias;
        }

        /**
         * 根据别名获取枚举
         * @param alias
         * @return
         */
        public static EventEnum valueOfAlias(String alias) {
            for (EventEnum event : values()) {
                if (alias.equals(event.alias)) { // 传过来的alias在枚举里整合
                    return event;
                }
            }
            return null;
        }

    }

    /**
     * hbase表相关
     */
    public static final String EVENT_LOG_HBASE_NAME = "event_logs";

    public static final String EVENT_LOG_FAMILY_NAME = "info";

    /**
     * 日志收集字段常量
     */
    public static final String LOG_SPARTOR = "\\^A";

    public static final String EVENT_COLUMN_NAME_IP = "ip";

    public static final String EVENT_COLUMN_NAME_VERSION = "ver";

    public static final String EVENT_COLUMN_NAME_SERVER_TIME = "s_time";

    public static final String EVENT_COLUMN_NAME_EVENT_NAME = "en";

    public static final String EVENT_COLUMN_NAME_UUID = "u_ud";

    public static final String EVENT_COLUMN_NAME_MEMBER_ID = "u_mid";

    public static final String EVENT_COLUMN_NAME_SESSION_ID = "u_sd";

    public static final String EVENT_COLUMN_NAME_CLIENT_TIME = "c_time";

    public static final String EVENT_COLUMN_NAME_LANGUAGE = "l";

    public static final String EVENT_COLUMN_NAME_USERAGENT = "b_iev";

    public static final String EVENT_COLUMN_NAME_RESOLUTION = "b_rst";

    public static final String EVENT_COLUMN_NAME_CURRENT_URL = "p_url";

    public static final String EVENT_COLUMN_NAME_PREFFER_URL = "p_ref";

    public static final String EVENT_COLUMN_NAME_TITLE = "tt";

    public static final String EVENT_COLUMN_NAME_PLATFORM = "pl";

    /**
     * 和订单相关
     */
    public static final String EVENT_COLUMN_NAME_ORDER_ID = "oid";

    public static final String EVENT_COLUMN_NAME_ORDER_NAME = "on";

    public static final String EVENT_COLUMN_NAME_CURRENCY_AMOUTN = "cua";

    public static final String EVENT_COLUMN_NAME_CURRENCY_TYPE = "cut";

    public static final String EVENT_COLUMN_NAME_PAYMENT_TYPE = "pt";

    /**
     * 事件相关
     * 点击：点击事件 category   点赞、收藏、喜欢、转发
     * 下单：下单事件
     */
    public static final String EVENT_COLUMN_NAME_EVENT_CATEGORY = "ca";

    public static final String EVENT_COLUMN_NAME_EVENT_ACTION = "ac";

    public static final String EVENT_COLUMN_NAME_EVENT_KV = "kv_";

    public static final String EVENT_COLUMN_NAME_EVENT_DURATION = "du";

    /**
     * 浏览器相关
     */

    public static final String EVENT_COLUMN_NAME_BROWSER_NAME = "browserName";

    public static final String EVENT_COLUMN_NAME_BROWSER_VERSION = "browserVersion";

    public static final String EVENT_COLUMN_NAME_OS_NAME = "osName";

    public static final String EVENT_COLUMN_NAME_OS_VERSION = "osVersion";

    /**
     * 地域相关
     */

    public static final String EVENT_COLUMN_NAME_COUNTRY = "country";

    public static final String EVENT_COLUMN_NAME_PROVINCE = "province";

    public static final String EVENT_COLUMN_NAME_CITY = "city";
}
