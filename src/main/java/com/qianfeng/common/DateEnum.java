package com.qianfeng.common;

/**
 * Created by MaoMao on 2018/8/20 11:05
 *
 * @Description:
 */
public enum DateEnum {
    YEAR("year"),
    SEASON("season"),
    MONTH("month"),
    WEEK("week"),
    DAY("day"),
    HOUR("hour");

    public final String typeName;

    DateEnum(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 根据typeName来放回时间枚举
     * @param name
     * @return
     */
    public static DateEnum valueOfName(String name) {
        for (DateEnum dataEnum : values()) {
            if (name.equals(dataEnum.typeName)) {
                return dataEnum;
            }
        }
        return null;
    }
}
