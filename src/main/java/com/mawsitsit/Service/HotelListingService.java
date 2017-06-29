package com.mawsitsit.Service;

import com.mawsitsit.Model.*;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Repository.ReviewRepository;
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

  @Autowired
  private ReviewRepository reviewRepository;

  public EntityList<List<EntityContainer>> createList(HttpServletRequest request, Page page) {
    List<ResourceEntity> entities = page.getContent();
    List<EntityContainer> entityContainers = new ArrayList();
    for (ResourceEntity entity : entities) {
      entityContainers.add(new EntityContainer(entity.getClass().getSimpleName(), entity.getId(), entity));
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

  public <T extends ResourceEntity> EntityList<EntityContainer<T>> addEntity
          (EntityList<EntityContainer<T>> singleEntity, HttpServletRequest request) {
    ResourceEntity entity = singleEntity.getData().getAttributes();
    if (entity.getClass().equals(Hotel.class)) {
      hotelRepository.save((Hotel)entity);
    }
    if (entity.getClass().equals(Review.class)) {
      reviewRepository.save((Review)entity);
    }
    EntityContainer entityContainer = singleEntity.getData();
    entityContainer.setId(entity.getId());
    singleEntity.setData(entityContainer);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString() + "/" + entity.getId());
    singleEntity.setLinks(link);
    return singleEntity;
  }

  public EntityList<EntityContainer> getHotel(Long id, HttpServletRequest request) throws EmptyResultDataAccessException {
    Hotel hotel = hotelRepository.findOne(id);
    if (hotel == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    EntityContainer container = new EntityContainer("Hotel", id, hotel);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString());
    return new EntityList<>(link, container);
  }

  public EntityList<EntityContainer> getReview(Long id, HttpServletRequest request) throws
          EmptyResultDataAccessException {
    Review review = reviewRepository.findOne(id);
    if (review == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    EntityContainer container = new EntityContainer("Review", id, review);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString());
    return new EntityList<>(link, container);
  }

  public Page queryHotels(Specification<ResourceEntity> specs, Pageable pageable) {
    return specs == null ? hotelRepository.findAll(pageable) : hotelRepository.findAll(specs, pageable);
  }

  public Page queryReviews(Specification<ResourceEntity> specs, Pageable pageable) {
    return specs == null ? reviewRepository.findAll(pageable) : reviewRepository.findAll(specs, pageable);
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
