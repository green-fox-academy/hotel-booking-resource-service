package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hotel {
  @JsonIgnore
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  private String location;
  private String name;
  private String main_image_src;
  private boolean has_wifi;
  private boolean has_parking;
  private boolean has_pets;
  private boolean has_restaurant;
  private boolean has_bar;
  private boolean has_swimming_pool;
  private boolean has_air_conditioning;
  private boolean has_gym;
  private String meal_plan;
  private int stars;
}

