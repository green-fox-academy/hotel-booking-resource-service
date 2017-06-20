package com.mawsitsit.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class Hotel {
  private String type;
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  private Attributes attributes;
}
