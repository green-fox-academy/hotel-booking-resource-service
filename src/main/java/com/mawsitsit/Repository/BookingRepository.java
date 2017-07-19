package com.mawsitsit.Repository;

import com.mawsitsit.Model.Booking;
import com.mawsitsit.Model.ResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends PagingAndSortingRepository<Booking, Long>, JpaSpecificationExecutor<ResourceEntity> {
  Page findAllByHotel_id(Long id, Pageable pageable);
  List<Booking> findAllByHotel_id(Long id);
}
