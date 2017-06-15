package com.mawsitsit.Repository;

import com.mawsitsit.Model.Hearthbeat;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HearthbeatRepository extends CrudRepository<Hearthbeat, Long> {
}
