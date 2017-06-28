package com.mawsitsit.Service;

import com.mawsitsit.Model.Hotel;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder {

  public static <T> Specification<Hotel> withParameter(String argument, T t) {
    return ((root, query, cb) -> cb.equal(root.get(argument), t));
  }
}
