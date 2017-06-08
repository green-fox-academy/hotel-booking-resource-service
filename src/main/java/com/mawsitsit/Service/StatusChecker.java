package com.mawsitsit.Service;

import com.mawsitsit.Repository.HearthbeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusChecker {

  @Autowired
  private HearthbeatRepository hearthbeatRepository;

  public boolean serviceStatus() {
    return hearthbeatRepository.count() > 0;
  }

}
