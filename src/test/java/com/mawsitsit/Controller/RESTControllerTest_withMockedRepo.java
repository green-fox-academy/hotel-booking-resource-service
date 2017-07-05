package com.mawsitsit.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.EntityContainer;
import com.mawsitsit.Model.EntityList;
import com.mawsitsit.Model.Review;
import com.mawsitsit.Repository.HearthbeatRepository;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Service.MessageHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RESTControllerTest_withMockedRepo {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  private MockMvc mockMvc;

  @MockBean
  private HearthbeatRepository hearthbeatRepo;

  @MockBean
  private MessageHandler messageHandler;

  @MockBean
  private HotelRepository hotelRepository;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testHeartbeat_withDatabaseOk_queueHasItems() throws Exception {
    long returned = 1;
    BDDMockito.given(messageHandler.getCount()).willReturn((int) returned);
    BDDMockito.given(hearthbeatRepo.count()).willReturn(returned);
    mockMvc.perform(get("/heartbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("ok"))
            .andExpect(jsonPath("$.queue").value("error"));
  }

  @Test
  public void testHeartBeat_withFaultyDatabase_emptyQueue() throws Exception {
    long returned = 0;
    BDDMockito.given(messageHandler.getCount()).willReturn((int) returned);
    BDDMockito.given(hearthbeatRepo.count()).willReturn(returned);
    mockMvc.perform(get("/heartbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("error"))
            .andExpect(jsonPath("$.queue").value("ok"));
  }

  @Test
  public void testHotels_withOneEntry() throws Exception {
    contentType = new MediaType(MediaType.APPLICATION_JSON.getType(), "vnd.api+json", Charset.forName
            ("utf8"));
    List returnValue = new ArrayList();
    Hotel hotel = new Hotel();
    hotel.setHas_wifi(false);
    returnValue.add(hotel);
    Page<Hotel> page = new PageImpl<Hotel>(returnValue);
    BDDMockito.given(hotelRepository.findAll(Matchers.any(Pageable.class))).willReturn(page);
    mockMvc.perform(get("/api/hotels"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.links.self").exists())
            .andExpect(jsonPath("$.links.next").doesNotExist())
            .andExpect(jsonPath("$.data[0].type").value("Hotel"))
            .andExpect(jsonPath("$.data[0].attributes.has_wifi").value(false));
  }

  @Test
  public void testHotels_withPost_withInvalidHotel() throws Exception {
    Hotel hotel = new Hotel();

    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setType("hotel");
    entityContainer.setAttributes(hotel);

    EntityList<EntityContainer> singleHotel = new EntityList<>(null, entityContainer);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(singleHotel);

    mockMvc.perform(post("/api/hotels")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.errors[0].status").value(400))
            .andExpect(jsonPath("$.errors[0].title").value("Bad Request"))
            .andExpect(jsonPath("$.errors[0].detail").exists());
  }

  @Test
  public void testReviews_withPost_withInvalidReview() throws Exception {
    Review review = new Review();

    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setType("review");
    entityContainer.setAttributes(review);

    EntityList<EntityContainer> singleReview = new EntityList<>(null, entityContainer);

    ObjectMapper mapper = new ObjectMapper();
    String jsonInput = mapper.writeValueAsString(singleReview);

    mockMvc.perform(post("/api/hotels/1/reviews")
            .contentType(contentType)
            .content(jsonInput))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.errors[0].status").value(400))
            .andExpect(jsonPath("$.errors[0].title").value("Bad Request"))
            .andExpect(jsonPath("$.errors[0].detail").exists());
  }
}