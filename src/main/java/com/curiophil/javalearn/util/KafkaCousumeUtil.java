package com.curiophil.javalearn.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class KafkaCousumeUtil {

    private static KafkaConsumer<String, String> kafkaConsumer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "*:9092");
        props.put("sasl.jaas.config", "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"admin\" password=\"!1Sd&Ao1#\";");
        props.put("sasl.mechanism", "SCRAM-SHA-512");
        props.put("security.protocol", "SASL_PLAINTEXT");
        props.put("group.id", "logFn_funcMetric");
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "60000");
        props.put("max.poll.records", 1000);
        props.put("auto.offset.reset", "latest");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        kafkaConsumer = new KafkaConsumer<String, String>(props);

        ArrayList<String> topics = new ArrayList<>();
        topics.add("LOG_314_ecloud_inventory_ecso_log_info1");
        kafkaConsumer.subscribe(topics);

    }

    public static void consume() {
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
            if (!records.isEmpty()) {
                for (ConsumerRecord<String, String> record : records) {
                    System.out.println(JSON.parseObject(record.value(), Map.class));
                }
            }
        }
    }


    public static void main(String[] args) {
        consume();
//        Date now = new Date();
//        List<Date> dates = Lists.newArrayList(now);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(now);
//        for (int i = 1; i <= 10; i++) {
//            calendar.add(Calendar.DATE, -1);
//            dates.add(calendar.getTime());
//        }
//        System.out.println(dates);
    }


}
