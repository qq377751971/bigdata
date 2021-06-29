package com.wtz.canal;

import com.alibaba.otter.canal.client.kafka.KafkaCanalConnector;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.alibaba.otter.canal.protocol.Message;
import com.wtz.utils.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * canal kafka test
 * 
 * @author wangtianzeng 
 */
public class CanalKafkaTest {

    protected final static Logger logger  = LoggerFactory.getLogger(CanalKafkaTest.class);

    private KafkaCanalConnector connector;

    private static volatile boolean         running = false;

    private Thread                          thread  = null;

    private Thread.UncaughtExceptionHandler handler = (t, e) -> logger.error("parse events has an error", e);

    public CanalKafkaTest(String zkServers, String servers, String topic, Integer partition, String groupId){
        connector = new KafkaCanalConnector(servers, topic, partition, groupId, null, false);
    }

    public static void main(String[] args) {
        try {
            final CanalKafkaTest kafkaCanalClientExample = new CanalKafkaTest(YamlUtil.getProperty("zookeeper.address").toString(),
                    YamlUtil.getProperty("spring.kafka.bootstrap-servers").toString(),
                    YamlUtil.getProperty("spring.kafka.template.default-topic").toString(),
                    null,
                    YamlUtil.getProperty("spring.kafka.consumer.group-id").toString());
            logger.info("## start the kafka consumer: {}-{}", YamlUtil.getProperty("spring.kafka.template.default-topic").toString(), YamlUtil.getProperty("spring.kafka.consumer.group-id").toString());
            kafkaCanalClientExample.start();
            logger.info("## the canal kafka consumer is running now ......");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    logger.info("## stop the kafka consumer");
                    kafkaCanalClientExample.stop();
                } catch (Throwable e) {
                    logger.warn("##something goes wrong when stopping kafka consumer:", e);
                } finally {
                    logger.info("## kafka consumer is down.");
                }
            }));
            while (running)
                ;
        } catch (Throwable e) {
            logger.error("## Something goes wrong when starting up the kafka consumer:", e);
            System.exit(0);
        }
    }

    public void start() {
        Assert.notNull(connector, "connector is null");
        thread = new Thread(this::process);
        thread.setUncaughtExceptionHandler(handler);
        thread.start();
        running = true;
    }

    public void stop() {
        if (!running) {
            return;
        }
        running = false;
        if (thread != null) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }

    private void process() {
        while (!running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        while (running) {
            try {
                connector.connect();
                connector.subscribe();
                while (running) {
                    try {
                        List<Message> messages = connector.getListWithoutAck(100L, TimeUnit.MILLISECONDS); // 获取message
                        if (messages == null) {
                            logger.info("message is null");
                            continue;
                        }
                        for (Message message : messages) {
                            long batchId = message.getId();
                            int size = message.getEntries().size();
                            if (batchId == -1 || size == 0) {
//                            if (batchId == -1) {
                                // try {
                                // Thread.sleep(1000);
                                // } catch (InterruptedException e) {
                                // }
                            } else {
                                // printSummary(message, batchId, size);
                                // printEntry(message.getEntries());
                                logger.info(message.toString());
                            }
                        }

                        connector.ack(); // 提交确认
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        connector.unsubscribe();
        connector.disconnect();
    }
}
