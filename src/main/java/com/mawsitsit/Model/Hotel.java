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
  private String location, name, main_image_src;
  @NotNull
  private Boolean has_wifi, has_parking, has_pets, has_restaurant, has_bar, has_swimming_pool, has_air_conditioning, has_gym;
  @NotNull
  private String meal_plan;
  @NotNull
  private Integer stars;
}

