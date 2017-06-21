package com.mawsitsit.Repository;

import com.mawsitsit.Model.Attributes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttributesRepository extends CrudRepository<Attributes, Long> {
}
