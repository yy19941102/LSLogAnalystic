package com.qianfeng.etl.mr.toHbase;

import com.qianfeng.common.EventLogConstants;
import com.qianfeng.util.TimeUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.mapred.TableMapReduceUtil;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 解析数据到hbase的Runner类
 * [hadoop@hadoop01 ~]$ export HADOOP_CLASSPATH=/home/hadoop/installed/hbase-1.2.0-cdh5.13.2/lib/*:classpath
 */
public class ToHbaseRunner implements Tool {
    private static final Logger logger = Logger.getLogger(ToHbaseRunner.class);
    private Configuration conf = null;

    @Override
    public void setConf(Configuration conf) {
        this.conf = HBaseConfiguration.create();
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }

    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(), new ToHbaseRunner(), args);
        } catch (Exception e) {
            logger.warn("运行清洗数据到hbase中异常");
        }
    }

    // yarn jar  .jar package.class -d 2018-08-17
    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = this.getConf();

        Job job = Job.getInstance(conf, "to hbase");
        job.setJarByClass(ToHbaseRunner.class);

        // map阶段
        job.setMapperClass(ToHbaseMpper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Put.class);

        // 判断hbase的表是否存在
        this.isExistsHbaseTable(conf);

        // 设置输入参数和设置job的输入路径处理
        setInputArgs(job, args, conf);

        // reduce的设置
        // 本地提交本地运行 add
        TableMapReduceUtil.initTableReduceJob(EventLogConstants.EVENT_LOG_HBASE_NAME,null,null,null,true);
        job.setNumReduceTasks(0);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    /**
     * 判断hbase中表是否存在
     *
     * @param conf
     */
    private void isExistsHbaseTable(Configuration conf) {
        HBaseAdmin ha = null;
        try {
            ha = new HBaseAdmin(conf);
            TableName tn = TableName.valueOf(EventLogConstants.EVENT_LOG_HBASE_NAME);
            // 判断是否存在,存在则不管,否则创建
            if (!ha.tableExists(tn)) {
                HTableDescriptor hdc = new HTableDescriptor(tn);
                HColumnDescriptor hcd = new HColumnDescriptor(EventLogConstants.EVENT_LOG_FAMILY_NAME);
                hdc.addFamily(hcd);
                // 提交创建
                ha.createTable(hdc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ha != null) { // 关闭对象
                try {
                    ha.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }

    /**
     * 运行yarn jar *.jar .class -d 2018-08-18
     *
     * @param job
     * @param args
     * @param conf
     */
    private void setInputArgs(Job job, String[] args, Configuration conf) {
        FileSystem fs = null;
        try {
            String date = null;
            // 循环参数列表
            for (int i = 0; i < args.length; i++) {
                if ("-d".equals(args[i])) {
                    if (i + 1 < args.length) {
                        date = args[i + 1];
                        break;
                    }
                }
            }
            // 如果date为空或者无效,则默认使用昨天的date,将date设置到conf中
            if (date == null || !TimeUtil.isValidateData(date)) {
                date = TimeUtil.getYesterday();
            }
            // 设置输入的路径   /log/08/05/**.log
            fs = FileSystem.get(conf);
            String[] dates = date.split("-");
            Path inputPath = new Path("/logs/" + dates[1] + "/" + dates[2]);
            if (fs.exists(inputPath)) {
                FileInputFormat.addInputPath(job, inputPath);
            } else {
                throw new RuntimeException("job的运行数据目录不存在.");
            }
        } catch (Exception e) {
            logger.warn("设置作业的输入路径异常.", e);
        } finally {
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
    }
}
