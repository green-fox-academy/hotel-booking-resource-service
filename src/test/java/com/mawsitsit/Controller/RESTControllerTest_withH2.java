package com.mawsitsit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.EntityContainer;
import com.mawsitsit.Model.EntityList;
import com.mawsitsit.Model.Review;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Repository.ReviewRepository;
import com.mawsitsit.Service.MessageHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.Charset;

import static com.mawsitsit.Service.EntityListingServiceTest.initReview;
import static org.junit.Assert.*;

import static com.mawsitsit.Service.EntityListingServiceTest.initHotel;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RESTControllerTest_withH2 {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private HotelRepository hotelRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @MockBean
  private MessageHandler messageHandler;

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

    Review review1 = initReview();
    review1.setHotel(hotelRepository.findOne(1L));
    Review review2 = initReview();
    review2.setHotel(hotelRepository.findOne(1L));
    review2.setRating(2);
    Review review3 = initReview();
    review3.setHotel(hotelRepository.findOne(1L));
    review3.setRating(3);
    review3.setDescription("Bad");
    Review review4 = initReview();
    review4.setRating(2);
    review4.setHotel(hotelRepository.findOne(1L));
    reviewRepository.save(review1);
    reviewRepository.save(review2);
    reviewRepository.save(review3);
    reviewRepository.save(review4);
  }

  @Test
  public void testHotels_withOneFilterParam() throws Exception {
    mockMvc.perform(get("/api/hotels?stars=4"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[2]").exists())
            .andExpect(jsonPath("$.data[3]").doesNotExist());
  }

  @Test
  public void testReviews_withOneFilterParam() throws Exception {
    mockMvc.perform(get("/api/hotels/1/reviews?rating=2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[1]").exists())
            .andExpect(jsonPath("$.data[2]").doesNotExist());
  }

  @Test
  public void testHotels_withTwoFilterParams() throws Exception {
    mockMvc.perform(get("/api/hotels?stars=4&has_swimming_pool=false"))
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
  public void testSingleReview_withValidId() throws Exception {
    mockMvc.perform(get("/api/hotels/reviews/2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.attributes.rating").value(2))
            .andExpect(jsonPath("$.data.id").value(2));
  }

  @Test
  public void testSingleHotel_withRelationship() throws Exception {
    mockMvc.perform(get("/api/hotels/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").exists())
            .andExpect(jsonPath("$.data.id").value(1))
            .andExpect(jsonPath("$.data.attributes.average_rating").value(2.0))
            .andExpect(jsonPath("$.relationships.data[0].id").value(1))
            .andExpect(jsonPath("$.included[0].attributes.rating").value(1))
            .andExpect(jsonPath("$.included[1].attributes.rating").exists());
  }

  @Test
  public void testSingleHotel_withInvalidId() throws Exception {
    mockMvc.perform(get("/api/hotels/10"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors[0].status").value(404))
            .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
            .andExpect(jsonPath("$.errors[0].detail").value("No hotels found by id: 10"));
  }

  @Test
  public void testSingleReview_withInvalidId() throws Exception {
    mockMvc.perform(get("/api/hotels/reviews/10"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errors[0].status").value(404))
            .andExpect(jsonPath("$.errors[0].title").value("Not Found"))
            .andExpect(jsonPath("$.errors[0].detail").value("No reviews found by id: 10"));
  }

  @Test
  public void testDeleteHotel_withValidId() throws Exception {
    mockMvc.perform(delete("/api/hotels/2"));
    assertEquals(null, hotelRepository.findOne(2L));
  }

  @Test
  public void testDeleteReview_withValidId() throws Exception {
    mockMvc.perform(delete("/api/hotels/reviews/2"));
    assertEquals(null, reviewRepository.findOne(2L));
  }

  @Test
  public <S> void testUpdateHotel_withValidId() throws Exception {
    Hotel hotel = initHotel();
    hotel.setLocation("Szeged");
    EntityList<EntityContainer, S> entityList = new EntityList<>(null, new EntityContainer("hotel", 1L, hotel),
            null, null);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(entityList);

    mockMvc.perform(patch("/api/hotels/1")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.attributes.location").value("Szeged"))
            .andExpect(jsonPath("$.data.id").value(1));
  }

  @Test
  public <S> void testUpdateReview_withValidId() throws Exception {
    Review review = initReview();
    review.setRating(4);
    EntityList<EntityContainer, S> entityList = new EntityList<>(null, new EntityContainer("review", 1L, review),
            null, null);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(entityList);

    mockMvc.perform(patch("/api/hotels/reviews/1")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.attributes.rating").value(4))
            .andExpect(jsonPath("$.data.id").value(1));
  }
}