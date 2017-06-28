package com.mawsitsit.Controller;

import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Repository.HotelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RESTControllerTest_withH2 {

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
    mockMvc.perform(get("/hotels?stars=4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[2]").exists())
            .andExpect(jsonPath("$.data[3]").doesNotExist());
  }

  @Test
  public void testHotels_withTwoFilterParams() throws Exception {
    mockMvc.perform(get("/hotels?stars=4&has_swimming_pool=true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[1]").exists())
            .andExpect(jsonPath("$.data[2]").doesNotExist());
  }

  public Hotel initHotel() {
    Hotel hotel = new Hotel();
    hotel.setLocation("location");
    hotel.setName("name");
    hotel.setMain_image_src("src");
    hotel.setHas_wifi(true);
    hotel.setHas_parking(true);
    hotel.setHas_pets(true);
    hotel.setHas_restaurant(false);
    hotel.setHas_bar(true);
    hotel.setHas_swimming_pool(true);
    hotel.setHas_air_conditioning(true);
    hotel.setHas_gym(true);
    hotel.setMeal_plan("mealplan");
    hotel.setStars(5);
    return hotel;
  }
}