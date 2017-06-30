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
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class EntityListingService {

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
      hotelRepository.save((Hotel) entity);
    }
    if (entity.getClass().equals(Review.class)) {
      reviewRepository.save(((Review) entity).setCreated_at(ZonedDateTime.now().format(DateTimeFormatter.ofPattern
              ("yyyy-MM-dd'T'HH:mm:ssZ"))));
    }
    EntityContainer entityContainer = singleEntity.getData();
    entityContainer.setId(entity.getId());
    singleEntity.setData(entityContainer);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString() + "/" + entity.getId());
    singleEntity.setLinks(link);
    return singleEntity;
  }

  public Hotel getHotel(Long id) throws EmptyResultDataAccessException {
    Hotel hotel = hotelRepository.findOne(id);
    if (hotel == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    return hotel;
  }

  public Review getReview(Long id) throws EmptyResultDataAccessException {
    Review review = reviewRepository.findOne(id);
    if (review == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    return review;
  }

  public <T extends ResourceEntity> EntityList<EntityContainer> wrapEntity(T entity, HttpServletRequest request) {
    EntityContainer container = new EntityContainer(entity.getClass().getSimpleName(), entity.getId(), entity);
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

  public <T extends ResourceEntity> EntityList updateEntity(Long id, EntityList<EntityContainer<T>>
          incomingAttributes, HttpServletRequest request) throws Exception {
    ResourceEntity entityToUpdate = null;
    if (incomingAttributes.getData().getAttributes().getClass().equals(Hotel.class)) {
      entityToUpdate = hotelRepository.findOne(id);
    } else if (incomingAttributes.getData().getAttributes().getClass().equals(Review.class)) {
      entityToUpdate = reviewRepository.findOne(id);
    }
    if (entityToUpdate == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    ResourceEntity incomingEntity = incomingAttributes.getData().getAttributes();
    Field[] fields = incomingEntity.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.get(incomingEntity) != null) {
        field.set(entityToUpdate, field.get(incomingEntity));
      }
    }
    if (incomingAttributes.getData().getAttributes().getClass().equals(Hotel.class)) {
      hotelRepository.save((Hotel) entityToUpdate);
      return wrapEntity(getHotel(id), request);
    } else if (incomingAttributes.getData().getAttributes().getClass().equals(Review.class)) {
      reviewRepository.save((Review) entityToUpdate);
      return wrapEntity(getReview(id), request);
    } else {
      return null;
    }
  }

  public void deleteHotel(Long id) throws Exception {
    hotelRepository.delete(id);
  }

  public void deleteReview(Long id) throws Exception {
    reviewRepository.delete(id);
  }
}
