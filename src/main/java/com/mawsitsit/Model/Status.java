package com.mawsitsit.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Status {
  private String status = "ok";
  private String database;

  public Status() {
  }

  public Status(String database) {
    this.database = database;
  }
}
