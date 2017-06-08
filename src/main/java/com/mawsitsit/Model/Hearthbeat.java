package com.mawsitsit.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Hearthbeat {

  @Id
  private Boolean status = true;

  public Hearthbeat() {
  }
}
