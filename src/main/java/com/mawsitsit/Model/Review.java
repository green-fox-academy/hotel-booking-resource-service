package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review extends ResourceEntity{
  @JsonIgnore
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  @NotNull
  private Integer rating;
  @NotNull
  private String description;
  private String created_at;
  @JsonIgnore
  @ManyToOne(cascade = CascadeType.ALL)
  @JsonUnwrapped
  private Hotel hotel;
}

