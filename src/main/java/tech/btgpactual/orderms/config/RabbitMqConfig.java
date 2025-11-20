package tech.btgpactual.orderms.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String ORDER_CREATED_QUEUE = "btg-pactual-order-created";
    public static final String ORDER_EXCHANGE = "btg-pactual-order-exchange";
    public static final String ORDER_CREATED_ROUTING_KEY = "order.created";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue orderCreatedQueue(){
        return new Queue(ORDER_CREATED_QUEUE);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
        public Binding binding() {
            return BindingBuilder
                    .bind(orderCreatedQueue())       // Pega a Fila
                    .to(orderExchange())             // Liga ela na Exchange
                    .with(ORDER_CREATED_ROUTING_KEY); // Com esta regra (chave)
        }
}
