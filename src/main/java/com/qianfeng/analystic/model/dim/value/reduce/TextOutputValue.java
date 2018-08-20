package com.qianfeng.analystic.model.dim.value.reduce;

import com.qianfeng.analystic.model.dim.base.BaseDimension;
import com.qianfeng.common.KpiType;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by MaoMao on 2018/8/20 14:56
 *
 * @Description:用于map输出的value的数据类型
 */
public class TextOutputValue extends BaseStatusValueWritable {
    private String id; // 泛指id:uuid  mid  sid ...
    private long time;

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.id);
        out.writeLong(this.time);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.id = in.readUTF();
        this.time = in.readLong();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public KpiType getKpi() {
        return null;
    }
}
