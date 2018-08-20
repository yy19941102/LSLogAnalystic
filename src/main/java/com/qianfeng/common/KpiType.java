package com.qianfeng.common;

import org.apache.calcite.rel.core.Values;
import org.apache.hadoop.mapred.JobHistory;

/**
 * Created by MaoMao on 2018/8/20 14:41
 *
 * @Description:Kpi的枚举
 */
public enum  KpiType {
    NEW_USER("new_user"),

    BROWSER_NEW_USER("browser_new_user");

    public String kpiName;

     KpiType(String kpiName) {
        this.kpiName = kpiName;
    }

    /**
     * 根据kpiName来放回kpiType的枚举
     * @param name
     * @return
     */
    public static KpiType valueOfKpiName(String name) {
        for (KpiType kpi:values()){
            if (name.equals(kpi.kpiName)){
                return kpi;
            }
        }
        return null;
    }
}
