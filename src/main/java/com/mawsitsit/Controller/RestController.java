package com.mawsitsit.Controller;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Service.MessageHandler;
import com.mawsitsit.Service.StatusChecker;
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
  private MessageHandler handler;

  @GetMapping("/heartbeat")
  public Status checkApp () throws IOException, TimeoutException {
    if (handler.getCount() == 0) {
      handler.sendMessage();
      handler.getMessage();
      statusChecker.setQueueStatus("ok");
    } else {
      statusChecker.setQueueStatus("error");
    }
    return statusChecker.serviceStatus();
  }

  @RequestMapping("/count")
  public Integer count() throws IOException {
    return handler.getCount();
  }

  @RequestMapping("/purge")
  public Integer purge() throws IOException {
    handler.emptyQueue();
    return handler.getCount();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "Hello World!";
  }
}
