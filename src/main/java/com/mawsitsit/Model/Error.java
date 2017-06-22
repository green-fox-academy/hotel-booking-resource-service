package com.mawsitsit.Model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Error {
  private int status;
  private String title;
  private String detail;
}
