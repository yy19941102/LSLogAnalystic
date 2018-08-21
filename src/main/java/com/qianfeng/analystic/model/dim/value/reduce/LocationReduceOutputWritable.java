package com.qianfeng.analystic.model.dim.value.reduce;

import com.qianfeng.common.KpiType;
import org.apache.hadoop.io.WritableUtils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by MaoMao on 2018/8/20 23:19
 *
 * @Description:地域模块下reduce阶段输出的value类型
 */
public class LocationReduceOutputWritable extends BaseStatusValueWritable {
    private KpiType kpi;
    private int activeUsers; // 地域维度活跃用户个数
    private int sessions; // 地域维度的会话个数
    private int bouceSessions; // 地域维度的跳出会话个数

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(this.activeUsers);
        out.writeInt(this.sessions);
        out.writeInt(this.bouceSessions);
        WritableUtils.writeEnum(out,this.kpi);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.activeUsers=in.readInt();
        this.sessions=in.readInt();
        this.bouceSessions=in.readInt();
        WritableUtils.readEnum(in,KpiType.class);
    }

    @Override
    public KpiType getKpi() {
        return this.kpi;
    }

    public void setKpi(KpiType kpi) {
        this.kpi = kpi;
    }

    public int getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(int activeUsers) {
        this.activeUsers = activeUsers;
    }

    public int getSessions() {
        return sessions;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
    }

    public int getBouceSessions() {
        return bouceSessions;
    }

    public void setBouceSessions(int bouceSessions) {
        this.bouceSessions = bouceSessions;
    }
}
