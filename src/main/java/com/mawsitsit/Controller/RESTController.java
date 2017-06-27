package com.mawsitsit.Controller;

import com.mawsitsit.Model.*;
import com.mawsitsit.Model.Error;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Service.MessageHandler;
import com.mawsitsit.Service.HotelListingService;
import com.mawsitsit.Service.StatusChecker;
import com.mawsitsit.Service.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
  public Status checkApp(HttpServletRequest request) throws IOException, TimeoutException {
//    logger.info(request.getServerName() + " HTTP-REQUEST " + request.getRequestURI());
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
  public HotelList listHotels(HttpServletRequest request, Pageable pageable) {
    logger.info(request.getServerName() + " HTTP-REQUEST " + request.getRequestURI());
    return hotelListingService.createList(request, pageable);
  }

  @ResponseStatus(code = HttpStatus.CREATED)
  @PostMapping("/hotels")
  public HotelList createHotel(@RequestBody @Valid HotelList<HotelContainer> singleHotel, HttpServletRequest request){
    logger.info(request.getServerName() + " HTTP-REQUEST " + request.getRequestURI());
    return hotelListingService.addHotel(singleHotel, request);
    }

  @RequestMapping("/addHotel")
  public void addHotel() {
    for (int i = 0; i < 200; i++) {
      hotelRepository.save(new Hotel());
    }
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Response badRequest(MethodArgumentNotValidException e, HttpServletRequest request) {
    logger.warn(request.getServerName() + " HTTP-REQUEST " + request.getRequestURI());
    Response response = new Response();
    response.addError(new Error(400, "Bad Request", String.format("The attribute fields: %s are missing.",
            Validator.getMissingFields(e.getBindingResult()))));
    return response;
  }

}
