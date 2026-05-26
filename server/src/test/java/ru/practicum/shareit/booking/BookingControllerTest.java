package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.Headers;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;



import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookingService bookingService;

    @Test
    void createBookingTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);
        BookingRequestDto request = new BookingRequestDto();


        when(bookingService.createBooking(eq(123L), any(BookingRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void bookingApprovedTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);


        when(bookingService.bookingApprove(eq(123L),eq(1L),eq(true))).thenReturn(response);

        mockMvc.perform(patch("/bookings/1")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("approved","true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void getBookingTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);


        when(bookingService.getBooking(eq(123L),eq(1L))).thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                        .header(Headers.SHARER_USER_ID, "123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void getBookingByIdAndStatusTest() throws Exception {
        List<BookingResponseDto> responsetList = new ArrayList<>();
        BookingResponseDto response = new BookingResponseDto();
        responsetList.add(response);
        response.setId(1L);

        when(bookingService.getBookingByIdAndStatus(eq(123L),eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("state","state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId()));
    }

    @Test
    void getBookingByOwnerAndStatusTest() throws Exception {
        List<BookingResponseDto> responsetList = new ArrayList<>();
        BookingResponseDto response = new BookingResponseDto();
        responsetList.add(response);
        response.setId(1L);

        when(bookingService.getBookingByOwnerAndStatus(eq(123L),eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings/owner")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("state","state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId()));
    }
}
