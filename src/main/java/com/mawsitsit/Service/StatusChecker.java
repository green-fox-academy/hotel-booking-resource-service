package com.mawsitsit.Service;

import com.mawsitsit.Model.Status;
import com.mawsitsit.Repository.HearthbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusChecker {
  private HearthbeatRepository hearthbeatRepository;

  @Autowired
  public StatusChecker(HearthbeatRepository hearthbeatRepository) {
    this.hearthbeatRepository = hearthbeatRepository;
  }

  public Status serviceStatus() {
    return hearthbeatRepository.count() > 0 ? new Status("ok") : new Status("error");
  }
}
