package com.mawsitsit.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Response {
  private List<Error> errors = new ArrayList<>();

  public void addError(Error error) {
    errors.add(error);
  }
}
