package com.mawsitsit.Service;

import com.mawsitsit.Model.*;
import com.mawsitsit.Repository.BookingRepository;
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

  @Autowired
  private BookingRepository bookingRepository;

  public <S> EntityList<List<EntityContainer<ResourceEntity>>, S> createList(HttpServletRequest request, Page page) {
    List<ResourceEntity> entities = page.getContent();
    List<EntityContainer<ResourceEntity>> entityContainers = new ArrayList<>();
    for (ResourceEntity entity : entities) {
      entityContainers.add(new EntityContainer<>(entity.getClass().getSimpleName(), entity.getId(), entity));
    }
    return new EntityList<>(linkBuilder(request, page), entityContainers, null, null);
  }

  private Links linkBuilder(HttpServletRequest request, Page page) {
    Links links = new Links();
    String requestURL = request.getRequestURL().toString();
    String query = request.getQueryString();
    int pageNumber = page.getNumber();
    int totalPage = page.getTotalPages();

    links = setPreviousAndSelf(page, query, requestURL, links, pageNumber);
    links = setNextAndLast(page, query, requestURL, links, pageNumber, totalPage);

    return links;
  }

  private Links setPreviousAndSelf(Page page, String query, String requestURL, Links links, int pageNumber) {
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
    return links;
  }

  private Links setNextAndLast(Page page, String query, String requestURL, Links links, int pageNumber, int totalPage) {
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

  public <T extends ResourceEntity, S> EntityList<EntityContainer<T>, S> addEntity
          (EntityList<EntityContainer<T>, S> singleEntity, HttpServletRequest request, Long id) {
    ResourceEntity entity = setResourceEntity(singleEntity, id);
    saveEntity(entity);
    EntityContainer<T> entityContainer = singleEntity.getData();
    entityContainer.setId(entity.getId());
    singleEntity.setData(entityContainer);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString() + "/" + entity.getId());
    singleEntity.setLinks(link);
    return singleEntity;
  }

  private void saveEntity(ResourceEntity entity) {
    if (entity.getClass().equals(Hotel.class)) {
      hotelRepository.save((Hotel) entity);
    } else if (entity.getClass().equals(Review.class)) {
      reviewRepository.save((Review) entity);
    }
  }

  private <T extends ResourceEntity, S> ResourceEntity setResourceEntity(EntityList<EntityContainer<T>, S> singleEntity, Long id) {
    ResourceEntity entity = singleEntity.getData().getAttributes();
    if (entity.getClass().equals(Review.class)) {
      Review review = (Review) entity;
      review.setHotel(getHotel(id));
      entity = review;
    }
    return entity;
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

  public Booking getBooking(Long id) throws EmptyResultDataAccessException {
    Booking booking = bookingRepository.findOne(id);
    if (booking == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    return booking;
  }

  public <T extends ResourceEntity> EntityList wrapEntity(T entity, HttpServletRequest request) {
    EntityContainer<T> container = new EntityContainer<>(entity.getClass().getSimpleName(), entity.getId(), entity);
    Links link = new Links();
    link.setSelf(request.getRequestURL().toString());
    if (entity.getClass().equals(Hotel.class)) {
      return new EntityList<>(link, container, getHotelRelationships(request, entity), getHotelIncluded(entity));
    } else {
      return new EntityList<>(link, container, null, null);
    }
  }

  private <T extends ResourceEntity> Relationships getHotelRelationships(HttpServletRequest request, T entity) {
    Links relationshipLinks = new Links(null, request.getRequestURL().toString() + "/relationships/reviews", null,
            null, request.getRequestURL().toString() + "/reviews");
    List<EntityContainer> list = new ArrayList<>();
    for (Review review : reviewRepository.findAllByHotel_id(entity.getId())) {
      list.add(new EntityContainer(review.getClass().getSimpleName(), review.getId(), null));
    }
    EntityList entityList = new EntityList(relationshipLinks, null, null, null);
    return new Relationships<>(entityList, list);
  }

  private <T extends ResourceEntity> List<EntityContainer<Review>> getHotelIncluded(T entity) {
    List<EntityContainer<Review>> entityContainers = new ArrayList<>();
    for (Review review : reviewRepository.findAllByHotel_id(entity.getId())) {
      entityContainers.add(new EntityContainer<>(review.getClass().getSimpleName(), review.getId(), review));
    }
    return entityContainers;
  }

  public Page queryHotels(Specification<ResourceEntity> specs, Pageable pageable) {
    return specs == null ? hotelRepository.findAll(pageable) : hotelRepository.findAll(specs, pageable);
  }

  public Page queryReviews(Specification<ResourceEntity> specs, Pageable pageable, Long id) {
    return specs == null ? reviewRepository.findAllByHotel_id(id, pageable) : reviewRepository.findAll(specs, pageable);
  }

  public Page queryBookings(Specification<ResourceEntity> specs, Pageable pageable, Long id) {
    return specs == null ? bookingRepository.findAllByHotel_id(id, pageable) : bookingRepository.findAll(specs, pageable);
  }


  public <T extends ResourceEntity, S> EntityList updateEntity(Long id, EntityList<EntityContainer<T>, S>
          incomingAttributes, HttpServletRequest request) throws Exception {
    ResourceEntity entityToUpdate = incomingAttributes.getData().getAttributes().getClass().equals(Hotel.class) ? hotelRepository.findOne(id) : reviewRepository.findOne(id);
    if (entityToUpdate == null) {
      throw new EmptyResultDataAccessException(id.toString(), id.intValue());
    }
    entityToUpdate = updateField(incomingAttributes, entityToUpdate);
    if (incomingAttributes.getData().getAttributes().getClass().equals(Hotel.class)) {
      hotelRepository.save((Hotel) entityToUpdate);
      return wrapEntity(getHotel(id), request);
    } else
      reviewRepository.save((Review) entityToUpdate);
    return wrapEntity(getReview(id), request);
  }

  private <T extends ResourceEntity, S> ResourceEntity updateField(EntityList<EntityContainer<T>, S> incomingAttributes,
                                                                   ResourceEntity entityToUpdate) throws IllegalAccessException {
    ResourceEntity incomingEntity = incomingAttributes.getData().getAttributes();
    Field[] fields = incomingEntity.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      if (field.get(incomingEntity) != null) {
        field.set(entityToUpdate, field.get(incomingEntity));
      }
    }
    return entityToUpdate;
  }

  public void deleteHotel(Long id) throws Exception {
    hotelRepository.delete(id);
  }

  public void deleteReview(Long id) throws Exception {
    reviewRepository.delete(id);
  }
}
