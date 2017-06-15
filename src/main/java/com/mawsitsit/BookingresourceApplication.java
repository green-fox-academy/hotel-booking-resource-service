package com.mawsitsit;

import com.mawsitsit.Controller.RestController;
import com.mawsitsit.Model.Hearthbeat;
import com.mawsitsit.Repository.HearthbeatRepository;
import com.mawsitsit.Service.RabbitReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookingresourceApplication implements CommandLineRunner {

	@Autowired
	private
	HearthbeatRepository hearthbeatRepo;

	@Bean
	MessageListenerAdapter listenerAdapter(RabbitReceiver receiver) {
		return new MessageListenerAdapter(receiver, "receive");
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		return new RabbitTemplate(connectionFactory);
	}

	@Bean
	SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter
					listenerAdapter) {
		SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames("heartbeat");
		container.setMessageListener(listenerAdapter);
		return container;
	}

	public static void main(String[] args) {
		SpringApplication.run(BookingresourceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		hearthbeatRepo.save(new Hearthbeat());
	}
}
