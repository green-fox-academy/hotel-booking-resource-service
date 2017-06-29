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
  private HotelListingService hotelListingService;

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
  public HotelList listHotels(@RequestParam LinkedHashMap<String, Object> allRequestParams, Pageable pageable,
                              HttpServletRequest request) {
    return hotelListingService.createList(request, hotelListingService.query(parameterHandler.getParameters
            (allRequestParams), pageable));
  }

  @ResponseStatus(code = HttpStatus.OK)
  @GetMapping("/api/hotels/{id}")
  public HotelList singleHotel(@PathVariable Long id, HttpServletRequest request) {
    return hotelListingService.getHotel(id, request);
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/api/hotels")
  public HotelList createHotel(@RequestBody @Valid HotelList<HotelContainer> singleHotel, HttpServletRequest request) {
    return hotelListingService.addHotel(singleHotel, request);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @PatchMapping("/api/hotels/{id}")
  public HotelList updateHotel(@PathVariable Long id, @RequestBody HotelList<HotelContainer> incomingAttributes, HttpServletRequest request) throws Exception {
   return hotelListingService.updateHotel(id, incomingAttributes, request);
  }

  @ResponseStatus(code = HttpStatus.OK)
  @DeleteMapping("/api/hotels/{id}")
  public void deleteHotel(@PathVariable Long id, HttpServletRequest request) throws Exception {
    hotelListingService.deleteHotel(id);
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
    Response response = new Response();
    response.addError(new Error(404, "Not Found", String.format("No hotel found by id: %s",
            requestUri.substring(requestUri.lastIndexOf('/') + 1))));
    return response;
  }
}
