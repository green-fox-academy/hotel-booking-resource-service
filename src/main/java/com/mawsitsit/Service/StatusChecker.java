package com.mawsitsit.Service;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Repository.HearthbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusChecker {

  @Autowired
  private HearthbeatRepository hearthbeatRepository;

  public Status serviceStatus() {
    return hearthbeatRepository.count() > 0 ? new Status("ok") : new Status("error");
  }
}
