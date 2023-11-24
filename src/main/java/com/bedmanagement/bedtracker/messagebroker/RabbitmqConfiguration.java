//package com.bedmanagement.bedtracker.messagebroker;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitAdmin;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitmqConfiguration {
//
////    // spring bean for queue (store json messages)
////    @Bean
////    public Queue jsonQueue(){
////        return new Queue(RabbitMqProperties.QUEUE_NAME);
////    }
////
////    // spring bean for rabbitmq exchange
////    @Bean
////    public DirectExchange exchange(){
////        return new DirectExchange(RabbitMqProperties.EXCHANGE_NAME);
////    }
////
////    // binding between json queue and exchange using routing key
////    @Bean
////    public Binding jsonBinding(){
////        return BindingBuilder
////                .bind(jsonQueue())
////                .to(exchange())
////                .with(RabbitMqProperties.ROUTING_KEY);
////    }
////
//    @Bean
//    public MessageConverter converter(){
//        return new Jackson2JsonMessageConverter();
//    }
////
////    @Bean
////    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
////        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
////        rabbitTemplate.setMessageConverter(converter());
////        return rabbitTemplate;
////    }
////
////    @Bean
////    public AmqpAdmin pimAmqpAdmin(ConnectionFactory connectionFactory) {
////        return new RabbitAdmin(connectionFactory);
////    }
//}
//
////    @Bean
////    public ObjectMapper objectMapper(){
////        return JsonMapper.builder().findAndAddModules().build();
////    }
//
//
//
//
//
