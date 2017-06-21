package com.mawsitsit.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hotel {
  private String type;
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  @OneToOne
  @JoinColumn (name = "attribute_id")
  private Attributes attributes;

  public Hotel(String type, Attributes attributes) {
    this.type = type;
    this.attributes = attributes;
  }
}
