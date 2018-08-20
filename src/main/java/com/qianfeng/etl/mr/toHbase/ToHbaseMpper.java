package com.qianfeng.etl.mr.toHbase;

import com.qianfeng.common.EventLogConstants;
import com.qianfeng.etl.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;


import java.io.IOException;
import java.util.Map;
import java.util.zip.CRC32;

/**
 * 将hdfs中收集的数据进行清洗,然后存储到Hbase中
 */
public class ToHbaseMpper extends Mapper<Object, Text, NullWritable, Put> {
    private static final Logger logger = Logger.getLogger(ToHbaseMpper.class);
    // 定义输入、输出、过滤记录数
    private int inputRecords, outputRecords, filterRecords = 0;
    private final byte[] family = Bytes.toBytes(EventLogConstants.EVENT_LOG_FAMILY_NAME);

    // 获取crc32的对象----crc文件用来校验内容的完整性
    private CRC32 crc = new CRC32();

    @Override
    protected void map(Object key, Text value, Context context) {
        this.inputRecords++;
        String log = value.toString();
        try {
            if (StringUtils.isEmpty(log.trim())) {
                this.filterRecords++;
                return;
            }
            // 正常调用日志工具类方法解析log
            Map<String, String> info = LogUtil.handleLog(log);
            // 获取事件  可以根据事件名来分别存储数据
            String eventName = info.get(EventLogConstants.EVENT_COLUMN_NAME_EVENT_NAME);
            // 获取事件的枚举
            EventLogConstants.EventEnum event = EventLogConstants.EventEnum.valueOfAlias(eventName);

            switch (event) {
                case LAUNCH:
                case EVENT:
                case PAGEVIEW:
                case CHARGEREQUEST:
                case CHARGEREFUND:
                case CHARGESUCCESS:
                    // 处理存储
                    this.handleLong(info, context, eventName);
                    break;
                default:
                    filterRecords++;
                    logger.warn("该时间的数据暂不支持处理,事件为：" + eventName);
            }
        } catch (Exception e) {
            this.filterRecords++;
            logger.warn("数据解析异常", e);
        }
    }

    /**
     * 处理存储,将数据存储到hbase中,row-key设计
     *
     * @param info
     * @param context
     * @param eventName
     */
    private void handleLong(Map<String, String> info, Context context, String eventName) {
        String server_time = info.get(EventLogConstants.EVENT_COLUMN_NAME_SERVER_TIME);
        String uuid = info.get(EventLogConstants.EVENT_COLUMN_NAME_UUID);
        String umid = info.get(EventLogConstants.EVENT_COLUMN_NAME_MEMBER_ID);
        try {
            // 判断server_time是否为空
            if (StringUtils.isNotEmpty(server_time)) {
                // 创建row-key
                String rowkey = bulidRowKey(server_time, uuid, umid, eventName);
                // 获取hbase的put对象
                Put put = new Put(Bytes.toBytes(rowkey));
                // 循环info将所有的k-v数据存储到row-key行中
                for (Map.Entry<String, String> en : info.entrySet()) {
                    if (StringUtils.isNotEmpty(en.getValue())) {
                        // 将kv添加到put中
                        put.addColumn(family, Bytes.toBytes(en.getKey()), Bytes.toBytes(en.getValue()));
                    }
                }
                context.write(NullWritable.get(), put);
                outputRecords++;
            } else {
                this.filterRecords++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * row-key设计：server_time_crc32(uuid_memberId_eventName)
     *
     * @param server_time
     * @param uuid
     * @param umid
     * @param eventName
     * @return 1523000001  1523000002  a1523000001  b1523000002
     * 问题：
     * 时间戳或者序列数据放在rowkwy前边,将会造成region的积压问题
     * <p>
     * 进行region的预创建：
     * 将时间戳或者列数据存储到值,然后根据二级索引查找
     */
    private String bulidRowKey(String server_time, String uuid, String umid, String eventName) {
        StringBuffer sb = new StringBuffer();
        sb.append(server_time + "_");
        if (StringUtils.isNotEmpty(server_time)) {
            // 对crc32的值初始化
            this.crc.reset();
            if (StringUtils.isNotEmpty(uuid)) {
                crc.update(uuid.getBytes());
            }
            if (StringUtils.isNotEmpty(umid)) {
                crc.update(umid.getBytes());
            }
            if (StringUtils.isNotEmpty(eventName)) {
                crc.update(eventName.getBytes());
            }
            // 将crc32的值模于10^8  模于数越大,长度会越小
            sb.append(this.crc.getValue() % 100000000l);
        }
        return sb.toString();
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        logger.info("inputRecords:" + inputRecords + "  filterRecords:" + filterRecords + "  outputRecords:" + outputRecords);
    }
}
