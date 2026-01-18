package ru.practicum.shareit.validationTest.gatewayBookingController;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
