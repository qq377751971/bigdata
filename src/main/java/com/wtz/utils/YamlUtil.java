package com.wtz.utils;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * yaml 工具类
 *
 * @author wangtianzeng
 */
public class YamlUtil {

    private final static Map<String, Object> yamlMap;

    static {
        Yaml yaml = new Yaml();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = loader.getResourceAsStream("application.yml");
        yamlMap = yaml.loadAs(inputStream, Map.class);
    }

    public static Object getProperty(String... keys){
        // 根路径
        Map<String, Object> childMap = yamlMap;
        for (String key : keys){
            Object o = childMap.get(key);
            // 非map实例退出
            if (!(o instanceof Map)){
                return childMap.getOrDefault(key, null);
            }
            childMap = (Map<String, Object>) o;
        }
        return childMap;
    }

    public static Object getProperty(String allKey){
        String[] keys = allKey.split("\\.");
        return getProperty(keys);
    }
}
