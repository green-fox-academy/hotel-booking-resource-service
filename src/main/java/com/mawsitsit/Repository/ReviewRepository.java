package com.mawsitsit.Repository;

import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.ResourceEntity;
import com.mawsitsit.Model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends PagingAndSortingRepository<Review, Long>, JpaSpecificationExecutor<ResourceEntity> {
  Page findAllByHotel_id(Long id, Pageable pageable);
  List<Review> findAllByHotel_id(Long id);
}
