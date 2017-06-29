package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hotel extends ResourceEntity{
  @JsonIgnore
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  @NotNull
  private String location;
  @NotNull
  private String name;
  @NotNull
  private String main_image_src;
  @NotNull
  private Boolean has_wifi;
  @NotNull
  private Boolean has_parking;
  @NotNull
  private Boolean has_pets;
  @NotNull
  private Boolean has_restaurant;
  @NotNull
  private Boolean has_bar;
  @NotNull
  private Boolean has_swimming_pool;
  @NotNull
  private Boolean has_air_conditioning;
  @NotNull
  private Boolean has_gym;
  @NotNull
  private String meal_plan;
  @NotNull
  private Integer stars;
}

