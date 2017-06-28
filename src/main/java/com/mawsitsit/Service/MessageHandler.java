package com.mawsitsit.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

@Service
public class MessageHandler {

  private Channel channel;
  private Connection connection;

  MessageHandler() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connection = connectionFactory.newConnection();
    channel = connection.createChannel();
    channel.queueDeclare("heartbeat", true, false, false, null);
  }

  void sendMessage() throws IOException, TimeoutException {
    channel.basicPublish("", "heartbeat", MessageProperties.PERSISTENT_TEXT_PLAIN,"message".getBytes());
  }

  void getMessage() throws IOException {
    GetResponse response = channel.basicGet("heartbeat", false);
    if (response != null) {
      long deliveryTag = response.getEnvelope().getDeliveryTag();
      channel.basicAck(deliveryTag, false);
    }
  }

  public Integer getCount() throws IOException {
    AMQP.Queue.DeclareOk declare = channel.queueDeclarePassive("heartbeat");
    return declare.getMessageCount();
  }

  public void emptyQueue() throws IOException {
    channel.queuePurge("heartbeat");
  }

  public void dispatch(Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    String message = mapper.writeValueAsString(object);
    channel.queueDeclare(System.getenv("EVENT_QUEUE"), true, false, false, null);
    channel.basicPublish("", System.getenv("EVENT_QUEUE"), null, message.getBytes());
  }

  @PreDestroy
  public void teardown() throws IOException, TimeoutException {
    channel.queueDelete("heartbeat");
    channel.close();
    connection.close();
  }
}
