package com.mawsitsit.Service;

import com.mawsitsit.Model.ResourceEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class ParameterHandler {

  public Specification<ResourceEntity> getParameters(LinkedHashMap<String, Object> params) {
    params = castBooleans(params);
    List<String> keySet = new ArrayList<>(params.keySet());
    try {
      Specification<ResourceEntity> query = SpecificationBuilder.withParameter(keySet.get(0), params.get(keySet.get(0)));
      for (int i = 1; i < keySet.size(); i++) {
        query = Specifications.where(query).and(SpecificationBuilder.withParameter(keySet.get(i), params.get(keySet.get
                (i))));
      }
      return query;
    } catch(IndexOutOfBoundsException e) {
      return null;
    }
  }

  public LinkedHashMap<String, Object> castBooleans(LinkedHashMap<String, Object> params) {
    params.remove("size");
    params.remove("page");
    List<String> keySet = new ArrayList<>(params.keySet());
    for (String aKeySet : keySet) {
      if (params.get(aKeySet).equals("true") || params.get(aKeySet).equals("false")) {
        params.replace(aKeySet, Boolean.valueOf((String) params.get(aKeySet)));
      }
    }
    return params;
  }
}

