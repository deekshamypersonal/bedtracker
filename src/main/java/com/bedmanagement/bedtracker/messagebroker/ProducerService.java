//package com.bedmanagement.bedtracker.messagebroker;
//
//
//import com.bedmanagement.bedtracker.common.EmailNotifierModel;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProducerService {
//
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
////    @Autowired
////    ObjectMapper objectMapper;
//
//    public void buildAndSendMail(String receiverMail,String subject,String message){
//        sendMail(new EmailNotifierModel(receiverMail,subject,message));
//    }
//
//    public void sendMail(EmailNotifierModel emailNotifierModel){
////var json=objectMapper.wr
//        rabbitTemplate.convertAndSend(RabbitMqProperties.EXCHANGE_NAME, RabbitMqProperties.ROUTING_KEY, emailNotifierModel);
//
//
//
//    }
//
//
//}
