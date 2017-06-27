package com.mawsitsit.Service;

import com.mawsitsit.Model.*;
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

  public HotelList<List<HotelContainer>> createList(HttpServletRequest request, Pageable pageable) {
    Page page = hotelRepository.findAll(pageable);
    List<Hotel> hotels = page.getContent();
    List<HotelContainer> hotelContainers = new ArrayList();
    for (Hotel hotel : hotels) {
      hotelContainers.add(new HotelContainer("hotel", hotel.getId(), hotel));
    }
    return new HotelList(linkBuilder(request, page), hotelContainers);
  }

  public Links linkBuilder(HttpServletRequest request, Page page) {
    Links links = new Links();
    String requestURL = request.getRequestURL().toString();
    String query = request.getQueryString();
    int pageNumber = page.getNumber();
    int totalPage = page.getTotalPages();

    if (page.hasPrevious()) {
      links.setSelf(requestURL + "?" + query);
      links.setPrev(requestURL + "?" + query.replaceFirst(String.valueOf
              (pageNumber), String.valueOf(pageNumber-1)));
    } else {
      if (query == null) {
        links.setSelf(requestURL);
      } else {
        links.setSelf(requestURL + "?" + query);
      }
    }
    if (page.hasNext()) {
      if (query == null) {
        links.setNext(requestURL + "?page=" + (pageNumber + 1));
        links.setLast(requestURL + "?page=" + (totalPage - 1));
      } else if (query.contains("page")) {
        links.setNext(requestURL + "?" + query.replaceFirst(String.valueOf
                (pageNumber), String.valueOf(pageNumber + 1)));
        links.setLast(requestURL + "?" + query.replaceFirst(String.valueOf
                (pageNumber), String.valueOf(totalPage - 1)));
      } else {
        links.setNext(requestURL + "?page=" + (pageNumber + 1) + "&" + query);
        links.setLast(requestURL + "?page=" + (totalPage - 1) + "&" + query);
      }
    }
    return links;
  }

  public HotelList addHotel(HotelList<HotelContainer> singleHotel, HttpServletRequest request) {
    Hotel hotel = singleHotel.getData().getAttributes();
    hotelRepository.save(hotel);
    HotelContainer hotelContainer = singleHotel.getData();
    hotelContainer.setId(hotel.getId());
    singleHotel.setData(hotelContainer);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString() + "/" + hotel.getId());
    singleHotel.setLinks(link);
    return singleHotel;
  }
}

