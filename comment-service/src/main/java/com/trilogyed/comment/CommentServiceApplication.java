package com.trilogyed.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableEurekaClient
public class CommentServiceApplication {

	public static final String TOPIC_EXCHANGE_NAME ="comment-exchange";
	public static final String QUEUE_NAME_CREATE ="comment-queue-create";
	public static final String QUEUE_NAME_UPDATE ="comment-queue-update";
	public static final String QUEUE_NAME_DELETE ="comment-queue-delete";
	public static final String ROUTING_KEY_CREATE ="comment.queue.create";
	public static final String ROUTING_KEY_UPDATE ="comment.queue.update";
	public static final String ROUTING_KEY_DELETE ="comment.queue.delete";

	@Bean
	Queue queueCreate(){return new Queue(QUEUE_NAME_CREATE,false); }

	@Bean
	Queue queueUpdate(){return new Queue(QUEUE_NAME_UPDATE,false); }

	@Bean
	Queue queueDelete(){return new Queue(QUEUE_NAME_DELETE,false); }

	@Bean
	TopicExchange exchange(){return new TopicExchange(TOPIC_EXCHANGE_NAME);}

	@Bean
	Binding bindingCreate(Queue queueCreate, TopicExchange exchange){
		return  BindingBuilder.bind(queueCreate).to(exchange).with((ROUTING_KEY_CREATE));
	}

	@Bean
	Binding bindingUpdate(Queue queueUpdate, TopicExchange exchange){
		return  BindingBuilder.bind(queueUpdate).to(exchange).with((ROUTING_KEY_UPDATE));
	}

	@Bean
	Binding bindingDelete(Queue queueDelete, TopicExchange exchange){
		return  BindingBuilder.bind(queueDelete).to(exchange).with((ROUTING_KEY_DELETE));
	}

	@Bean
	public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {return new Jackson2JsonMessageConverter();}



	public static void main(String[] args) {
		SpringApplication.run(CommentServiceApplication.class, args);
	}

}
