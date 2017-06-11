package com.mawsitsit.Model;

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
  private Long id;

  private Boolean status = true;

  public Hearthbeat() {
  }
}
