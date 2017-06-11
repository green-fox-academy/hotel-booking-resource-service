package com.mawsitsit.Controller;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Service.StatusChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.web.bind.annotation.RestController
public class RestController {

  @Autowired
  private
  StatusChecker statusChecker;

  @GetMapping("/hearthbeat")
  public Status checkApp () {
    return statusChecker.serviceStatus();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "Hello World!";
  }
}
