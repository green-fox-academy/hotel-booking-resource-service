package com.mawsitsit.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelContainer {
  private String type;
  private Long id;
  @Valid
  @NotNull
  private Hotel attributes;
}
