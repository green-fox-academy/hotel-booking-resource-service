package com.mawsitsit.Controller;

import com.mawsitsit.Model.*;
import com.mawsitsit.Model.Error;
import com.mawsitsit.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeoutException;

@RestController
public class RESTController {

  @Autowired
  private
  StatusChecker statusChecker;

  @Autowired
  private EntityListingService entityListingService;

  @Autowired
  private ParameterHandler parameterHandler;


  @GetMapping("/heartbeat")
  public Status checkApp(HttpServletRequest request) throws IOException, TimeoutException {
    return statusChecker.serviceStatus();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "String return for testing purposes ";
  }

  @GetMapping(value = "/api/hotels", produces = "application/vnd.api+json")
  public EntityList listHotels(@RequestParam LinkedHashMap<String, Object> allRequestParams, Pageable pageable,
                               HttpServletRequest request) {
    return entityListingService.createList(request, entityListingService.queryHotels(parameterHandler.getParameters
            (allRequestParams), pageable));
  }

  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/api/hotels/{id}")
  public EntityList singleHotel(@PathVariable Long id, HttpServletRequest request) {
    return entityListingService.wrapEntity(entityListingService.getHotel(id), request);
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/api/hotels")
  public EntityList createHotel(@RequestBody @Valid EntityList<EntityContainer<Hotel>, Object> singleHotel,
                                HttpServletRequest request) {
    return entityListingService.addEntity(singleHotel, request, null);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @PatchMapping("/api/hotels/{id}")
  public EntityList updateHotel(@PathVariable Long id, @RequestBody EntityList<EntityContainer<Hotel>, Object>
          incomingAttributes, HttpServletRequest request) throws Exception {
    return entityListingService.updateEntity(id, incomingAttributes, request);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @DeleteMapping("/api/hotels/{id}")
  public void deleteHotel(@PathVariable Long id, HttpServletRequest request) throws Exception {
    entityListingService.deleteHotel(id);
  }

  @GetMapping(value = {"/api/hotels/{id}/reviews", "/api/hotels/{id}/relationships/reviews"}, produces =
          "application/vnd.api+json")
  public EntityList listReviews(@RequestParam LinkedHashMap<String, Object> allRequestParams, @PathVariable Long id, Pageable pageable,
                                HttpServletRequest request) {
    allRequestParams.put("hotel", id);
    return entityListingService.createList(request, entityListingService.queryReviews(parameterHandler.getParameters
            (allRequestParams), pageable, id));
  }

  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/api/hotels/reviews/{reviewId}")
  public EntityList singleReview(@PathVariable Long reviewId, HttpServletRequest request) {
    return entityListingService.wrapEntity(entityListingService.getReview(reviewId), request);
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/api/hotels/{id}/reviews")
  public EntityList createReview(@RequestBody @Valid EntityList<EntityContainer<Review>, Object> singleReview,
                                   @PathVariable Long id, HttpServletRequest
          request) {
    return entityListingService.addEntity(singleReview, request, id);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @PatchMapping("/api/hotels/reviews/{reviewId}")
  public EntityList updateReview(@PathVariable Long reviewId, @RequestBody EntityList<EntityContainer<Review>, Object>
          incomingAttributes, HttpServletRequest request) throws Exception {
    return entityListingService.updateEntity(reviewId, incomingAttributes, request);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @DeleteMapping("/api/hotels/reviews/{reviewId}")
  public void deleteReview(@PathVariable Long reviewId, HttpServletRequest request) throws Exception {
    entityListingService.deleteReview(reviewId);
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response badRequest(MethodArgumentNotValidException e, HttpServletRequest request) {
    Response response = new Response();
    response.addError(new Error(400, "Bad Request", String.format("The attribute fields: %s are missing.",
            Validator.getMissingFields(e.getBindingResult()))));
    return response;
  }

  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  @ExceptionHandler(EmptyResultDataAccessException.class)
  public Response notFound(EmptyResultDataAccessException e, HttpServletRequest request) {
    String requestUri = request.getRequestURI();
    String[] params = requestUri.split("/");
    Response response = new Response();
    response.addError(new Error(404, "Not Found", String.format("No %s found by id: %s", params[params.length - 2],
            requestUri.substring(requestUri.lastIndexOf('/') + 1))));
    return response;
  }
}
