package com.sofn.bigdata.kafka;

//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.internals.Partitioner;
//import org.apache.kafka.common.Cluster;
//import org.apache.kafka.common.PartitionInfo;
//
//import java.util.List;
//
//public class MarshInfoPartition extends Partitioner {
//
//    @Override
//    public int partition(ProducerRecord<byte[], byte[]> record, Cluster cluster) {
//        List<PartitionInfo> partitions = cluster.partitionsForTopic(record.topic());
//        int numPartitions = partitions.size();
//
//        // key: siteId1001
//        // value: siteId1001\t沼气供热\t沼气进锅炉流量\tA001\tm3/h\t2019-12-25 16:50:14\t0.74\t15min\t20191225
//        String key = new String(record.key());
//        Object value = new String(record.value());
//        int partitionNum = key.hashCode();
////        try {
////            partitionNum = Integer.parseInt((String) key);
////        } catch (Exception e) {
////            partitionNum = key.hashCode();
////        }
//        return Math.abs(partitionNum % numPartitions);
//    }
//}
