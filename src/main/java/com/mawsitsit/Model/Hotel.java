package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hotel extends ResourceEntity {
  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotNull
  private String location, name, main_image_src;
  @NotNull
  private Boolean has_wifi, has_parking, has_pets, has_restaurant, has_bar, has_swimming_pool, has_air_conditioning, has_gym;
  @NotNull
  private String meal_plan;
  @NotNull
  private Integer stars;
  @Formula("(select AVG(r.rating) FROM review r WHERE r.hotel_id=id)")
  private Double average_rating;
  @JsonIgnore
  @OneToMany(mappedBy = "hotel", cascade = CascadeType.MERGE) // can't touch this!!!!
  private List<Review> reviews = new ArrayList<>();
  @JsonIgnore
  @OneToMany(mappedBy = "hotel", cascade = CascadeType.MERGE) // can't touch this!!!!
  private List<Booking> bookings = new ArrayList<>();
}

