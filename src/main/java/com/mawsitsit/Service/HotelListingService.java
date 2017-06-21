package com.mawsitsit.Service;

import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.HotelContainer;
import com.mawsitsit.Model.HotelList;
import com.mawsitsit.Model.Links;
import com.mawsitsit.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HotelListingService {

  @Autowired
  private HotelRepository hotelRepository;

  public HotelList createList(){
    List<Hotel> hotels = (List)hotelRepository.findAll();
    List hotelContainers = new ArrayList();
    for(Hotel hotel : hotels) {
      hotelContainers.add(new HotelContainer("hotel", hotel.getId(), hotel));
    }
    return new HotelList(new Links(),hotelContainers);
  }
}
