package com.wtz.kafka.client;

import com.wtz.utils.YamlUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

/**
 * kafka原生客户端-生产者
 *
 * @author wangtianzeng
 */
public class KafkaClientProducerTest {

    public static void main(String[] args) {
        Properties config = new Properties();
        config.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, YamlUtil.getProperty("spring.kafka.bootstrap-servers").toString());
        config.setProperty(ProducerConfig.ACKS_CONFIG, YamlUtil.getProperty("spring.kafka.producer.acks").toString());
        config.setProperty(ProducerConfig.RETRIES_CONFIG, YamlUtil.getProperty("spring.kafka.producer.retries").toString());
        config.setProperty(ProducerConfig.BATCH_SIZE_CONFIG, YamlUtil.getProperty("spring.kafka.producer.batch-size").toString());
        config.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        config.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(config);
        ProducerRecord<String, String> record = new ProducerRecord<>(YamlUtil.getProperty("spring.kafka.template.default-topic").toString(), "test1", "测试消息1");
        producer.send(record, (recordMetadata, e) -> {
            if (null != e){
                System.out.println("send error" + e.getMessage());
            }else {
                System.out.printf("offset:%s,partition:%s%n",recordMetadata.offset(),recordMetadata.partition());
            }
        });
        producer.close();
    }
}
