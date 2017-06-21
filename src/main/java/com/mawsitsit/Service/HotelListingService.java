package com.mawsitsit.Service;

import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.HotelContainer;
import com.mawsitsit.Model.HotelList;
import com.mawsitsit.Model.Links;
import com.mawsitsit.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelListingService {

  @Autowired
  private HotelRepository hotelRepository;

  public HotelList createList(HttpServletRequest request, Pageable pageable) {
    Page page = hotelRepository.findAll(pageable);
    List<Hotel> hotels = page.getContent();
    List hotelContainers = new ArrayList();
    for (Hotel hotel : hotels) {
      hotelContainers.add(new HotelContainer("hotel", hotel.getId(), hotel));
    }
    return new HotelList(linkBuilder(request, page), hotelContainers);
  }

  public Links linkBuilder(HttpServletRequest request, Page page) {
    Links links = new Links();
    links.setSelf(request.getRequestURL().toString());
    if (page.hasPrevious()) {
      links.setPrev(request.getRequestURL().toString() + "?page=" + (page.getNumber() - 1));
    }
    if(page.hasNext()) {
      links.setNext(request.getRequestURL().toString() + "?page=" + (page.getNumber() + 1));
      links.setLast(request.getRequestURL().toString() + "?page=" + page.getTotalPages());
    }
    return links;
  }
}
