package com.qianfeng.analystic.model.dim.base;

import org.apache.hadoop.io.WritableComparable;

/**
 * Created by MaoMao on 2018/8/20 10:38
 *
 * @Description:维度的顶级父类，它的子类有所有维度类：平台、时间、浏览器等
 */
public abstract class BaseDimension implements WritableComparable<BaseDimension> {
    // do nothing
}
