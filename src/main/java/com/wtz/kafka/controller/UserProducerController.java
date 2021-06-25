package com.wtz.kafka.controller;

import com.wtz.avro.User;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 发送消息
 *
 * @author wangtianzeng
 */
@RestController
@RequestMapping("/kafka/producer")
public class UserProducerController {

    @Resource
    private KafkaTemplate<String,User> kafkaTemplate;

    @GetMapping("/send")
    public void push(){
        User user = new User();
        user.setName("test");
        user.setFavoriteNumber(1);
        user.setFavoriteColor("red");
        //发送消息
        kafkaTemplate.send("kafka-test", user);
        System.out.println("user TOPIC 发送成功");
    }
}
