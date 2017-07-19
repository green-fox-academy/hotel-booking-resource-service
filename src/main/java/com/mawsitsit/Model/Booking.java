package com.mawsitsit.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Booking {
  @JsonIgnore
  @Id
  @GenericGenerator(name = "generator", strategy = "increment")
  @GeneratedValue(generator = "generator")
  private Long id;
  @NotNull
  private Integer guests;
  @NotNull
  private String start_date, end_date;
  private String created_at;
  @NotNull
  private String description;
  @JsonIgnore
  @ManyToOne
  @JoinColumn
  private Hotel hotel;

  @PrePersist
  public void prePersist() {
    created_at = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ"));
  }
}
