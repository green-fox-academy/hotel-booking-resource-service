package com.mawsitsit.Repository;

import com.mawsitsit.Model.ResourceEntity;
import com.mawsitsit.Model.Review;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, JpaSpecificationExecutor<ResourceEntity> {
}
