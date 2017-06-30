package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Accessors(chain = true)
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
}

