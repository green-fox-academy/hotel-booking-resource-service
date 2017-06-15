package com.mawsitsit.Service;

import com.mawsitsit.Model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitReceiver {
  @Autowired
  StatusChecker statusChecker;

  public void receive(String message) {
    statusChecker.setQueueStatus("ok");
    System.out.println("@@@@@@@@@@@@@@@@ HELLO @@@@@@@@@@@@@@@@");
  }
}
