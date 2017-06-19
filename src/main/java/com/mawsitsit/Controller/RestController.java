package com.mawsitsit.Controller;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Service.MessageHandler;
import com.mawsitsit.Service.StatusChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @Autowired
  private
  StatusChecker statusChecker;

  @Autowired
  private MessageHandler messageHandler;

  Logger logger = LoggerFactory.getLogger(RestController.class);

  @GetMapping("/heartbeat")
  public Status checkApp () throws IOException, TimeoutException {
    logger.debug("debug msg");
    logger.info("info msg");
    logger.warn("warn msg");
    logger.error("error msg");
    return statusChecker.serviceStatus();
  }

  @RequestMapping("/count")
  public Integer count() throws IOException {
    return messageHandler.getCount();
  }

  @RequestMapping("/purge")
  public Integer purge() throws IOException {
    messageHandler.emptyQueue();
    return messageHandler.getCount();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "Hello World!!";
  }
}
