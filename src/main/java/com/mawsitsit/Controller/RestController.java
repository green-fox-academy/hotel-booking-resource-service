package com.mawsitsit.Controller;

import com.mawsitsit.Model.Hearthbeat;
import com.mawsitsit.Model.Status;
import com.mawsitsit.Repository.HearthbeatRepository;
import com.mawsitsit.Service.StatusChecker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @Autowired
  private
  StatusChecker statusChecker;

  @Autowired
  private
  RabbitTemplate rabbitTemplate;

  @Autowired
  private HearthbeatRepository hearthbeatRepo;

  @GetMapping("/hearthbeat")
  public Status checkApp () {
    statusChecker.setQueueStatus("error");
    rabbitTemplate.convertAndSend("heartbeat","Message");
    return statusChecker.serviceStatus();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "Hello World!";
  }

  @RequestMapping("/add")
  public String add() {
    hearthbeatRepo.save(new Hearthbeat());
    return "ok";
  }
}
