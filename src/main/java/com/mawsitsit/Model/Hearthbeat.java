package com.mawsitsit.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Hearthbeat {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private boolean status;

  public Hearthbeat() {
  }

  public Hearthbeat(boolean status) {
    this.status = status;
  }
}
