package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review {
  @JsonIgnore
  @Id
  @GeneratedValue (strategy = GenerationType.AUTO)
  private Long id;
  @NotNull
  private Integer rating;
  @NotNull
  private String dateTime = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
  @NotNull
  private String description;
}

