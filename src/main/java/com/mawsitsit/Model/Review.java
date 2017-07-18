package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Review extends ResourceEntity{
  @JsonIgnore
  @Id
  @GenericGenerator(name = "generator",strategy = "increment")
  @GeneratedValue(generator = "generator")
  private Long id;
  @NotNull
  private Integer rating;
  @NotNull
  private String description;
  private String created_at;
  @JsonIgnore
  @ManyToOne
  @JoinColumn
  private Hotel hotel;

  @PrePersist
  public void prePersist() {
    created_at = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
  }
}

