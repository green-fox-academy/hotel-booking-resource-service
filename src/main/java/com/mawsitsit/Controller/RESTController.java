package com.mawsitsit.Controller;

import com.mawsitsit.Model.*;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Service.MessageHandler;
import com.mawsitsit.Service.HotelListingService;
import com.mawsitsit.Service.StatusChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
public class RESTController {

  @Autowired
  private
  StatusChecker statusChecker;

  @Autowired
  private MessageHandler messageHandler;

  @Autowired
  private HotelListingService hotelListingService;

  @Autowired
  private HotelRepository hotelRepository;

  private Logger logger = LoggerFactory.getLogger(RESTController.class);

  @GetMapping("/heartbeat")
  public Status checkApp(HttpServletRequest httpServletRequest) throws IOException, TimeoutException {
    logger.info("HTTP-REQUEST " + httpServletRequest.getRequestURI());
    return statusChecker.serviceStatus();
  }

  @RequestMapping("/count")
  public Integer count() throws IOException {
    return messageHandler.getCount();
  }

  @RequestMapping("/purge")
  public Integer purge() throws IOException {
    messageHandler.emptyQueue();
    return messageHandler.getCount();
  }

  @RequestMapping({"/", "/index"})
  public String main() {
    return "String return for testing purposes ";
  }

  @GetMapping(value = "/hotels", produces = "application/vnd.api+json")
  public HotelList listHotels(HttpServletRequest httpServletRequest, Pageable pageable) {
    logger.info("HTTP-REQUEST " + httpServletRequest.getRequestURI());
    return hotelListingService.createList(httpServletRequest, pageable);
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/hotels")
  public SingleHotel createHotel(@RequestBody SingleHotel singleHotel, HttpServletRequest request) {
    return hotelListingService.addHotel(singleHotel, request);
  }

  @RequestMapping("/addHotel")
  public void addHotel() {
    for (int i = 0; i < 200; i++) {
      hotelRepository.save(new Hotel());
    }
  }

//  @ExceptionHandler(Exception.class)
//  public void badRequest(Exception e) {
//    logger.warn("HTTP-ERROR " + e.getStackTrace()[0].getMethodName());
//  }

}
