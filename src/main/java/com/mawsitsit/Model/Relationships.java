package com.mawsitsit.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Relationships<T> {
  private EntityList reviews;
  private T data;
}
