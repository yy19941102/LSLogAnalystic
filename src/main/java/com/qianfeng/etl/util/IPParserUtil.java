package com.qianfeng.etl.util;

import com.qianfeng.etl.util.ip.IPSeeker;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @Auther: lyd
 * @Date: 2018/8/15 16:01
 * @Description:ip解析工具类
 */
public class IPParserUtil extends IPSeeker{
    private static final Logger logger = Logger.getLogger(IPParserUtil.class);

    RegionInfo info = new RegionInfo();

    /**
     * 用于解析ip
     * @param ip
     * @return  对于没有解析出来的字段，默认为unknown
     */
    public RegionInfo parserIp(String ip){
        if(StringUtils.isEmpty(ip)){
            logger.warn("解析的ip为空.");
            return info;
        }

        try{
            //通过ipSeekeer来获取ip所对应的信息  贵州省铜仁地区| 局域网
            String country = IPSeeker.getInstance().getCountry(ip);
            if(country.equals("局域网")){
                info.setCountry("中国");
                info.setProvince("北京市");
                info.setCity("昌平区");
            } else if(country != null && !country.trim().isEmpty()){
                //查找返回的字符串中是否有省
                int index = country.indexOf("省");
                info.setCountry("中国");
                if(index > 0){
                    //证明有省份
                    info.setProvince(country.substring(0,index+1));
                    //查找是否有市
                    int index2 = country.indexOf("市");
                    if(index2 > 0){
                        info.setCity(country.substring(index+1,index2+1));
                    }
                } else {
                    //查找不到省
                    String flag = country.substring(0,2);
                    switch (flag){
                        case "内蒙":
                            //设置省份
                            info.setProvince(flag+"古");
                            country.substring(3);
                            index = country.indexOf("市");
                            if(index > 0){
                                info.setCity(country.substring(0,index+1));
                            }
                            break;

                        case "广西":
                        case "宁夏":
                        case "新疆":
                        case "西藏":
                            //设置省份
                            info.setProvince(flag);
                            country.substring(2);
                            index = country.indexOf("市");
                            if(index > 0){
                                info.setCity(country.substring(0,index+1));
                            }
                            break;

                        case "北京":
                        case "上海":
                        case "重庆":
                        case "天津":
                            info.setProvince(flag+"市");
                            country.substring(2);
                            index = country.indexOf("区");
                            if(index > 0){
                                char ch = country.charAt(index-1);
                                if(ch != '小' || ch != '校' || ch != '军'){
                                    info.setCity(country.substring(0,index+1));
                                }
                            }

                            index = country.indexOf("县");
                            if(index > 0){
                                info.setCity(country.substring(0,index+1));
                            }
                            break;

                        case "香港":
                        case "澳门":
                        case "台湾":
                            info.setProvince(flag+"特别行政区");
                            break;

                        default:
                            break;
                    }

                }
            }
        } catch (Exception e){
            logger.warn("解析ip异常.",e);
        }
        return info;
    }

    /**
     * 用于封装ip解析出来的国家、省、市信息
     */
    public static class RegionInfo{
        private static final String DEFAULT_VALUE = "unknown";
        private String country = DEFAULT_VALUE; //国家
        private String province = DEFAULT_VALUE; //省
        private String city = DEFAULT_VALUE; //市

        public RegionInfo(){

        }

        public RegionInfo(String country, String province, String city) {
            this.country = country;
            this.province = province;
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return "RegionInfo{" +
                    "country='" + country + '\'' +
                    ", province='" + province + '\'' +
                    ", city='" + city + '\'' +
                    '}';
        }
    }
}
