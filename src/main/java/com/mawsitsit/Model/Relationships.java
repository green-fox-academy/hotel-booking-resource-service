package com.mawsitsit.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Relationships<T> {
  private EntityList reviews;
  private T data;
}
