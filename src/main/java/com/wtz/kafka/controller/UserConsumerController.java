package com.wtz.kafka.controller;

import com.wtz.avro.User;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.BeanUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 发送消息
 *
 * @author wangtianzeng
 */
@RestController
@RequestMapping("/kafka/consumer")
public class UserConsumerController {

    @KafkaListener(topics = {"kafka-test"})
    public void receive(GenericRecord genericRecord) throws Exception{
        System.out.println("数据接收：user + "+  genericRecord.toString());
        //业务处理类,mybatispuls 自动生成的类
        User user = new User();
        //将收的数据复制过来
        BeanUtils.copyProperties(genericRecord, user);
        try {
            //落库
            System.out.printf("user:%s,color:%s,number:%d", user.getName(), user.getFavoriteColor(), user.getFavoriteNumber());
//            log.info("数据入库");
//            electronicsPackageService.save(electronicsPackageVO);
        } catch (Exception e) {
            throw new Exception("插入异常"+e);
        }
    }
}
