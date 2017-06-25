package com.mawsitsit.Service;

import com.mawsitsit.Model.Hotel;
import com.mawsitsit.Repository.HotelRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class HotelListingServiceTest {

  @InjectMocks
  private HotelListingService hotelListingService;

  @Mock
  private HotelRepository hotelRepository;

  @Mock
  private MockHttpServletRequest request;

  private List<Hotel> database = new ArrayList<>();

  @Before
  public void setUp() {
    for (int i = 0; i < 50; i++) {
      Hotel hotel = new Hotel();
      hotel.setId((long)(i + 1));
      database.add(hotel);
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
  public void testCreateList_withLastPageAndSizeOf10() {
    PageRequest pageable = new PageRequest(4, 10);
    Page returnedPage = new PageImpl<>(database.subList(40, 49), pageable, 50);
    BDDMockito.given(hotelRepository.findAll(Matchers.any(Pageable.class))).willReturn(returnedPage);

    BDDMockito.given(request.getRequestURL()).willReturn(new StringBuffer("testURL"));
    BDDMockito.given(request.getQueryString()).willReturn("page=4");

    Long id = 41L;

    assertEquals("testURL?page=4", hotelListingService.createList(request, pageable).getLinks().getSelf());
    assertNull(hotelListingService.createList(request, pageable).getLinks().getNext());
    assertNull(hotelListingService.createList(request, pageable).getLinks().getLast());
    assertEquals("testURL?page=3", hotelListingService.createList(request, pageable).getLinks().getPrev());
    assertEquals(id, hotelListingService.createList(request, pageable).getData().get(0).getId());
  }
}