package com.qianfeng.analystic.model.dim.base;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by MaoMao on 2018/8/20 19:15
 *
 * @Description:Kpi的维度类--Kpi是各项指标
 */
public class KpiDimension extends BaseDimension {
    private int id;
    private String KpiName;

    public KpiDimension() {

    }

    public KpiDimension(String kpiName) {
        this.KpiName = kpiName;
    }

    public KpiDimension(int id, String kpiName) {
        this(kpiName);
        this.id = id;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.id);
        out.writeUTF(this.KpiName);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readInt();
        this.KpiName = in.readUTF();
    }

    @Override
    public int compareTo(BaseDimension o) {
        // compareTo()用于Number对象与方法的参数进行比较：相等返回0；指定的数小于参数返回-1；指定的数大于参数返回1
        if (this == o) {
            return 0;
        }
        KpiDimension other = (KpiDimension) o;
        int tmp = this.id - other.id;
        if (tmp != 0) {
            return tmp;
        }
        tmp = this.KpiName.compareTo(other.KpiName);
        return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KpiDimension that = (KpiDimension) o;

        if (id != that.id) return false;
        return KpiName.equals(that.KpiName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + KpiName.hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKpiName() {
        return KpiName;
    }

    public void setKpiName(String kpiName) {
        KpiName = kpiName;
    }
}
