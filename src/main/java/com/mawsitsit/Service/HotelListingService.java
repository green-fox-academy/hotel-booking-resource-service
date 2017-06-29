package com.mawsitsit.Service;

import com.mawsitsit.Model.*;
import com.mawsitsit.Repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
public class HotelListingService {

  @Autowired
  private HotelRepository hotelRepository;

  public EntityList<List<EntityContainer>> createList(HttpServletRequest request, Page page) {
    List<Hotel> hotels = page.getContent();
    List<EntityContainer> entityContainers = new ArrayList();
    for (Hotel hotel : hotels) {
      entityContainers.add(new EntityContainer("hotel", hotel.getId(), hotel));
    }
    return new EntityList(linkBuilder(request, page), entityContainers);
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
              (pageNumber), String.valueOf(pageNumber - 1)));
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

  public EntityList<EntityContainer<Hotel>> addHotel(EntityList<EntityContainer<Hotel>> singleHotel, HttpServletRequest
          request) {
    Hotel hotel = singleHotel.getData().getAttributes();
    hotelRepository.save(hotel);
    EntityContainer entityContainer = singleHotel.getData();
    entityContainer.setId(hotel.getId());
    singleHotel.setData(entityContainer);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString() + "/" + hotel.getId());
    singleHotel.setLinks(link);
    return singleHotel;
  }

  public EntityList<EntityContainer> getHotel(Long id, HttpServletRequest request) throws EmptyResultDataAccessException {
    Hotel hotel = hotelRepository.findOne(id);
    if (hotel == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    EntityContainer container = new EntityContainer("hotel", id, hotel);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString());
    return new EntityList<EntityContainer>(link, container);
  }

  public Page query(Specification<Hotel> specs, Pageable pageable) {
    return specs == null ? hotelRepository.findAll(pageable) : hotelRepository.findAll(specs, pageable);
  }

  public EntityList updateHotel(Long id, EntityList<EntityContainer<Hotel>> incomingAttributes, HttpServletRequest
          request)
          throws Exception {
    Hotel hotelToUpdate = hotelRepository.findOne(id);
    if (hotelToUpdate == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    Hotel incomingHotel = incomingAttributes.getData().getAttributes();
    Field[] fields = incomingHotel.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.get(incomingHotel) != null) {
        field.set(hotelToUpdate, field.get(incomingHotel));
      }
    }
    hotelRepository.save(hotelToUpdate);
    return getHotel(id, request);
  }

  public void deleteHotel(Long id) throws Exception {
    hotelRepository.delete(id);
  }
}
