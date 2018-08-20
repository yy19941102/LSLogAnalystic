package com.qianfeng.analystic.model.dim.value.reduce;

import com.qianfeng.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * Created by MaoMao on 2018/8/20 19:27
 *
 * @Description:
 */
public abstract class BaseStatusValueWritable implements Writable {
    // 获取Kpi的类型
    public abstract KpiType getKpi();
}
