package com.mawsitsit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.HotelContainer;
import com.mawsitsit.Model.HotelList;
import com.mawsitsit.Repository.HotelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.Charset;

import static org.junit.Assert.*;

import static com.mawsitsit.Service.HotelListingServiceTest.initHotel;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RESTControllerTest_withH2 {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private HotelRepository hotelRepository;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    Hotel hotel1 = initHotel();
    Hotel hotel2 = initHotel();
    hotel2.setStars(4);
    Hotel hotel3 = initHotel();
    hotel3.setStars(4);
    hotel3.setHas_swimming_pool(false);
    Hotel hotel4 = initHotel();
    hotel4.setStars(4);
    hotel4.setHas_swimming_pool(false);
    hotelRepository.save(hotel1);
    hotelRepository.save(hotel2);
    hotelRepository.save(hotel3);
    hotelRepository.save(hotel4);
  }

  @Test
  public void testHotels_withOneFilterParam() throws Exception {
    mockMvc.perform(get("/api/hotels?stars=4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[2]").exists())
            .andExpect(jsonPath("$.data[3]").doesNotExist());
  }

  @Test
  public void testHotels_withTwoFilterParams() throws Exception {
    mockMvc.perform(get("/api/hotels?stars=4&has_swimming_pool=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[1]").exists())
            .andExpect(jsonPath("$.data[2]").doesNotExist());
  }

  @Test
  public void testSingleHotel_withValidId() throws Exception {
    mockMvc.perform(get("/api/hotels/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.attributes.stars").value(5))
            .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  public void testSingleHotel_withInvalidId() throws Exception {
    mockMvc.perform(get("/api/hotels/10"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors[0].status").value(404))
            .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
            .andExpect(jsonPath("$.errors[0].detail").value("No hotel found by id: 10"));
  }

  @Test
  public void testDeleteHotel_withValidId() throws Exception {
    mockMvc.perform(delete("/api/hotels/1"));
    assertEquals(null, hotelRepository.findOne(1L));
  }

  @Test
  public void testUpdateHotel_withValidId() throws Exception {
    Hotel hotel = initHotel();
    hotel.setLocation("Szeged");
    HotelList<HotelContainer> hotelList = new HotelList<>(null, new HotelContainer("hotel", 1L, hotel));

    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(hotelList);

    mockMvc.perform(patch("/api/hotels/1")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.attributes.location").value("Szeged"))
            .andExpect(jsonPath("$.data.id").value(1));
  }
}