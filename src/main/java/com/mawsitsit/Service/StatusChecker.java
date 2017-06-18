package com.mawsitsit.Service;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Repository.HearthbeatRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
@Setter
public class StatusChecker {
  private HearthbeatRepository hearthbeatRepository;
  private MessageHandler messageHandler;
  private String queueStatus;

  @Autowired
  public StatusChecker(HearthbeatRepository hearthbeatRepository, MessageHandler messageHandler) {
    this.hearthbeatRepository = hearthbeatRepository;
    this.messageHandler = messageHandler;
  }

  private void checkMQStatus() throws IOException, TimeoutException {
    if (messageHandler.getCount() == 0) {
      messageHandler.sendMessage();
      messageHandler.getMessage();
      queueStatus = "ok";
    } else {
      queueStatus = "error";
    }
  }

  public Status serviceStatus() throws IOException, TimeoutException {
    checkMQStatus();
    return hearthbeatRepository.count() > 0 ? new Status("ok", queueStatus) : new Status("error", queueStatus);
  }
}
