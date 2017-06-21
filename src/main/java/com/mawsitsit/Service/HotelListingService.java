package com.mawsitsit.Service;

import com.mawsitsit.Model.HotelList;
import com.mawsitsit.Model.Links;
import com.mawsitsit.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelListingService {

  @Autowired
  private HotelRepository hotelRepository;

  public HotelList createList(){
    List hotels = (List)hotelRepository.findAll();
    return new HotelList(new Links(),hotels);

  }
}
