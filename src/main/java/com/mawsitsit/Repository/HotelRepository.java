package com.mawsitsit.Repository;

import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.ResourceEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends PagingAndSortingRepository<Hotel, Long>, JpaSpecificationExecutor<ResourceEntity> {
}
