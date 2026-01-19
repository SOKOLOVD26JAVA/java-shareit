package ru.practicum.shareit.gatewayBookingController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.Headers;
import ru.practicum.booking.dto.BookingRequestDto;
import ru.practicum.booking.dto.BookingResponseDto;
import ru.practicum.client.ShareItBookingClient;
import ru.practicum.controller.GatewayBookingController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewayBookingController.class)
public class GatewayBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShareItBookingClient shareItClient;


    @Test
    void createNotValidBookingTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);
        BookingRequestDto request = new BookingRequestDto();


        when(shareItClient.createBooking(eq(123L), any(BookingRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/bookings")
                        .header(Headers.SHARER_USER_ID, "123")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void bookingApprovedTestWithOutHeader() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);


        when(shareItClient.bookingApprove(eq(123L), eq(1L), eq(true))).thenReturn(response);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingTestWithOutHeader() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);


        when(shareItClient.getBooking(eq(123L), eq(1L))).thenReturn(response);

        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingByIdAndStatusTestWithOutHeader() throws Exception {
        List<BookingResponseDto> responsetList = new ArrayList<>();
        BookingResponseDto response = new BookingResponseDto();
        responsetList.add(response);
        response.setId(1L);

        when(shareItClient.getBookingByIdAndStatus(eq(123L), eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings")
                        .param("state", "state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getBookingByOwnerAndStatusTestWithOutHeader() throws Exception {
        List<BookingResponseDto> responsetList = new ArrayList<>();
        BookingResponseDto response = new BookingResponseDto();
        responsetList.add(response);
        response.setId(1L);

        when(shareItClient.getBookingByOwnerAndStatus(eq(123L), eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings/owner")
                        .param("state", "state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBookingTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);
        BookingRequestDto request = new BookingRequestDto();
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusHours(1));


        when(shareItClient.createBooking(eq(123L), any(BookingRequestDto.class))).thenReturn(response);

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


        when(shareItClient.bookingApprove(eq(123L), eq(1L), eq(true))).thenReturn(response);

        mockMvc.perform(patch("/bookings/1")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId()));
    }

    @Test
    void getBookingTest() throws Exception {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(1L);


        when(shareItClient.getBooking(eq(123L), eq(1L))).thenReturn(response);

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

        when(shareItClient.getBookingByIdAndStatus(eq(123L), eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("state", "state")
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

        when(shareItClient.getBookingByOwnerAndStatus(eq(123L), eq("state"))).thenReturn(responsetList);
        mockMvc.perform(get("/bookings/owner")
                        .header(Headers.SHARER_USER_ID, "123")
                        .param("state", "state")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.getId()));
    }


}
