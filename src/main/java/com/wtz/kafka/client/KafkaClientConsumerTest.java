package com.wtz.kafka.client;

import com.wtz.utils.YamlUtil;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * kafka原生客户端-消费者
 *
 * @author wangtianzeng
 */
public class KafkaClientConsumerTest {

    public static void main(String[] args) {
        Properties config = new Properties();
        config.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, YamlUtil.getProperty("spring.kafka.bootstrap-servers").toString());
        config.setProperty(ConsumerConfig.GROUP_ID_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.group-id").toString());
        config.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.enable-auto-commit").toString());
        config.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.auto-commit-interval").toString());
        config.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.session-timeout").toString());
        config.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.max-poll-records").toString());
        config.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, YamlUtil.getProperty("spring.kafka.consumer.auto-offset-reset").toString());
        config.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        config.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config);
        consumer.subscribe(Collections.singleton(YamlUtil.getProperty("spring.kafka.template.default-topic").toString()));
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Long.parseLong(YamlUtil.getProperty("spring.kafka.listener.poll-timeout").toString()));
            records.forEach((record)-> System.out.println("receive: key ==="+record.key()+" value ===="+record.value()+" topic ==="+record.topic()));
        }
    }
}
