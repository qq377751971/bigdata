package com.wtz.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.wtz.utils.YamlUtil;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

/**
 * canal test
 *
 * @author wangtianzeng
 */
public class CanalTest {

    public static void main(String[] args) {
        // single
//        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(YamlUtil.getProperty("canal.single.host").toString(), Integer.parseInt(YamlUtil.getProperty("canal.single.port").toString())), YamlUtil.getProperty("canal.destination").toString(), YamlUtil.getProperty("canal.user").toString(), YamlUtil.getProperty("canal.password").toString());
        // cluster
        CanalConnector connector = CanalConnectors.newClusterConnector(Arrays.asList(new InetSocketAddress(YamlUtil.getProperty("canal.single.host").toString(), Integer.parseInt(YamlUtil.getProperty("canal.single.port").toString()))), YamlUtil.getProperty("canal.destination").toString(), YamlUtil.getProperty("canal.user").toString(), YamlUtil.getProperty("canal.password").toString());
//        CanalConnector connector = CanalConnectors.newClusterConnector(YamlUtil.getProperty("zookeeper.address").toString(), YamlUtil.getProperty("canal.destination").toString(), YamlUtil.getProperty("canal.user").toString(), YamlUtil.getProperty("canal.password").toString());
        int batchSize = 1000;
        int emptyCount = 0;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            int totalEmptyCount = 120;
            while (emptyCount < totalEmptyCount) {
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } finally {
            connector.disconnect();
        }

    }

    private static void printEntry(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            CanalEntry.RowChange rowChage;
            try {
                rowChage = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry,
                        e);
            }

            CanalEntry.EventType eventType = rowChage.getEventType();
            System.out.printf("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s%n",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType);
            System.out.println();
            //判断是否是DDL语句
            if (rowChage.getIsDdl()) {
                System.out.println("================》;isDdl: true,sql:" + rowChage.getSql());
            }
            for (CanalEntry.RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == CanalEntry.EventType.DELETE) {
                    printColumn(rowData.getBeforeColumnsList());
                } else if (eventType == CanalEntry.EventType.INSERT) {
                    printColumn(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------&gt; before");
                    printColumn(rowData.getBeforeColumnsList());
                    System.out.println("-------&gt; after");
                    printColumn(rowData.getAfterColumnsList());
                }
            }
        }
    }

    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns) {
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
}
