package com.mawsitsit.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
  private String status = "ok";
  private String database;
  private String queue;

  public Status() {
  }

  public Status(String database, String queue) {
    this.database = database;
    this.queue = queue;
  }
}
