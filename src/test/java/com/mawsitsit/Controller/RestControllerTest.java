package com.mawsitsit.Controller;

import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Repository.HearthbeatRepository;
import com.mawsitsit.Service.RabbitReceiver;
import com.mawsitsit.Service.StatusChecker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
@WebAppConfiguration
@EnableWebMvc
public class RestControllerTest {
  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
          MediaType.APPLICATION_JSON.getSubtype(),
          Charset.forName("utf8"));

  private MockMvc mockMvc;

  @MockBean
  private HearthbeatRepository hearthbeatRepo;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private StatusChecker statusChecker;

  @Autowired
  private RabbitReceiver receiver;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void testHeartbeat_withDatabaseOk() throws Exception {
    long returned = 1;
    BDDMockito.given(hearthbeatRepo.count()).willReturn(returned);
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("ok"));
  }

  @Test
  public void testHeartBeat_withFaultyDatabase() throws Exception {
    long returned = 0;
    BDDMockito.given(hearthbeatRepo.count()).willReturn(returned);
    mockMvc.perform(get("/hearthbeat"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$.status").value("ok"))
            .andExpect(jsonPath("$.database").value("error"));
  }

//  @Test
//  public void testHeartBeat_withEmptyQueue() throws Exception {
//
//  }
}