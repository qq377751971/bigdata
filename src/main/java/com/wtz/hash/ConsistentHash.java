package com.wtz.hash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHash {

    /**
     * 假设我们一共初始化有8个节点(可以是ip, 就理解为ip吧);
     * 把 1024个虚拟节点跟 8个资源节点相对应
     */
    public static Map<Integer, String> nodeMap = new HashMap<>();
    public static int V_redisS = 1024; // 假设我们的环上有1024个虚拟节点
    static TreeMap<Integer, String> virtualHashRingMap = new TreeMap<>();
    private static final Integer REAL_redis_COUNT = 8;
    static {
        nodeMap.put(0, "redis_0");
        nodeMap.put(1, "redis_1");
        nodeMap.put(2, "redis_2");
        nodeMap.put(3, "redis_3");
        nodeMap.put(4, "redis_4");
        nodeMap.put(5, "redis_5");
        nodeMap.put(6, "redis_6");
        nodeMap.put(7, "redis_7");


        for (Integer i = 0; i < V_redisS; i++) {
            // 每个虚拟节点跟其取模的余数的 redisMap 中的key相对应;
            // 下面删除虚拟节点的时候, 就可以根据取模规则来删除 TreeMap中的节点了;
            virtualHashRingMap.put(i, nodeMap.get(i % REAL_redis_COUNT));
        }
    }

    /**
     * 输入一个id
     *
     * @param value
     * @return
     */
    public static String getRealServerredis(String value) {
        // 1. 传递来一个字符串, 得到它的hash值
        Integer vredis = value.hashCode() % 1024;
        // 2.找到对应节点最近的key的节点值
        String realredis = virtualHashRingMap.ceilingEntry(vredis).getValue();


        return realredis;
    }

    /**
     * 模拟删掉一个物理可用资源节点, 其他资源可以返回其他节点
     */
    public static void dropBadredis(String redisName) {
        int redisk = -1;
        // 1. 遍历 redisMap 找到故障节点 redisName对应的key;
        for (Map.Entry<Integer, String> entry : nodeMap.entrySet()) {
            if (redisName.equalsIgnoreCase(entry.getValue())) {
                redisk = entry.getKey();
                break;
            }
        }
        if (redisk == -1) {
            System.err.println(redisName + "在真实资源节点中无法找到, 放弃删除虚拟节点!");
            return;
        }

        // 2. 根据故障节点的 key, 对应删除所有 chMap中的虚拟节点
        Iterator<Map.Entry<Integer, String>> iter = virtualHashRingMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, String> entry = iter.next();
            int key = entry.getKey();
            String value = entry.getValue();
            if (key % REAL_redis_COUNT == redisk) {
                System.out.println("删除物理节点对应的虚拟节点: [" + value + " = " + key + "]");
                iter.remove();
            }
        }
    }

    public static void main(String[] args) {
        // 1. 一个字符串请求(比如请求字符串存储到8个节点中的某个实际节点);
        String requestValue = "技术自由圈";
        // 2. 打印虚拟节点和真实节点的对应关系;
        System.out.println(virtualHashRingMap);
        // 3. 核心: 传入请求信息, 返回实际调用的节点信息
        System.out.println(getRealServerredis(requestValue));
        // 4. 删除某个虚拟节点后
        dropBadredis("redis_2");
        System.out.println("==========删除 redis_2 之后: ================");
        System.out.println(getRealServerredis(requestValue));
        System.out.println(virtualHashRingMap);
    }
}
