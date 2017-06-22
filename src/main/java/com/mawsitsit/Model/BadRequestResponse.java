package com.mawsitsit.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BadRequestResponse {
  private List<ErrorMessage> errorMessages;

  public void addError(ErrorMessage errorMessage) {
    errorMessages.add(errorMessage);
  }
}
