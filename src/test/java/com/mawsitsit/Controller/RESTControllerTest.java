package com.mawsitsit.Controller;

import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Model.HotelContainer;
import com.mawsitsit.Repository.HearthbeatRepository;
import com.mawsitsit.Repository.HotelRepository;
import com.mawsitsit.Service.MessageHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RESTControllerTest {
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
    List returnValue = new ArrayList();
    returnValue.add(new Hotel());
    BDDMockito.given(hotelRepository.findAll()).willReturn(returnValue);
    mockMvc.perform(get("/hotels"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.data[0].type").value("hotel"))
            .andExpect(jsonPath("$.data[0].attributes.has_wifi").value(false));

  }
}