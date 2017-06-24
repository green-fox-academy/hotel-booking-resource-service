package com.mawsitsit.Service;

import com.mawsitsit.BookingresourceApplication;
import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Repository.HotelRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingresourceApplication.class)
public class HotelListingServiceTest {

  @MockBean
  private HotelRepository hotelRepository;

  @MockBean
  private MockHttpServletRequest request;

  private List<Hotel> database = new ArrayList<>();

  @Autowired
  private HotelListingService hotelListingService;

  @Before
  public void setUp() {
    for (int i = 0; i < 50; i++) {
      database.add(new Hotel());
    }
  }

  @Test
  public void testCreateList_withFirstPage() {
    PageRequest pageable = new PageRequest(0, 20);
    Page returnedPage = new PageImpl<>(database, pageable, 50);
    BDDMockito.given(hotelRepository.findAll(Matchers.any(Pageable.class))).willReturn(returnedPage);

    BDDMockito.given(request.getRequestURL()).willReturn(new StringBuffer("testURL"));

    assertEquals("testURL", hotelListingService.createList(request, pageable).getLinks().getSelf());
    assertEquals("testURL?page=1", hotelListingService.createList(request, pageable).getLinks().getNext());
    assertEquals("testURL?page=2", hotelListingService.createList(request, pageable).getLinks().getLast());
    assertNull(hotelListingService.createList(request, pageable).getLinks().getPrev());
  }

  @Test
  public void testCreateList_withMiddlePage() {
    PageRequest pageable = new PageRequest(1, 20);
    Page returnedPage = new PageImpl<>(database, pageable, 50);
    BDDMockito.given(hotelRepository.findAll(Matchers.any(Pageable.class))).willReturn(returnedPage);

    BDDMockito.given(request.getRequestURL()).willReturn(new StringBuffer("testURL"));
    BDDMockito.given(request.getQueryString()).willReturn("page=1");

    assertEquals("testURL?page=1", hotelListingService.createList(request, pageable).getLinks().getSelf());
    assertEquals("testURL?page=2", hotelListingService.createList(request, pageable).getLinks().getNext());
    assertEquals("testURL?page=2", hotelListingService.createList(request, pageable).getLinks().getLast());
    assertEquals("testURL?page=0", hotelListingService.createList(request, pageable).getLinks().getPrev());
  }

  @Test
  public void testCreateList_withLastPageAndSizeOf5() {
    PageRequest pageable = new PageRequest(9, 5);
    Page returnedPage = new PageImpl<>(database, pageable, 50);
    BDDMockito.given(hotelRepository.findAll(Matchers.any(Pageable.class))).willReturn(returnedPage);

    BDDMockito.given(request.getRequestURL()).willReturn(new StringBuffer("testURL"));
    BDDMockito.given(request.getQueryString()).willReturn("page=9");

    assertEquals("testURL?page=9", hotelListingService.createList(request, pageable).getLinks().getSelf());
    assertNull(hotelListingService.createList(request, pageable).getLinks().getNext());
    assertNull(hotelListingService.createList(request, pageable).getLinks().getLast());
    assertEquals("testURL?page=8", hotelListingService.createList(request, pageable).getLinks().getPrev());
  }
}