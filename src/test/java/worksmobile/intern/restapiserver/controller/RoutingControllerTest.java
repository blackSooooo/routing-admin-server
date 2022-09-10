package worksmobile.intern.restapiserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import worksmobile.intern.restapiserver.dto.RoutingInformationDto;
import worksmobile.intern.restapiserver.error.Error;
import worksmobile.intern.restapiserver.error.ErrorResponse;
import worksmobile.intern.restapiserver.exception.ResponseException;
import worksmobile.intern.restapiserver.service.RoutingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoutingController.class)
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutingService routingService;

    ObjectMapper om = new ObjectMapper();

    RoutingInformationDto routingInformationDto;

    String id = "1";

    @BeforeEach
    void setup() {
        routingInformationDto = RoutingInformationDto.builder()
                .id(id)
                .path("/blog.json")
                .method("GET")
                .rateLimit(5)
                .baseUrl("https://openapi.naver.com")
                .domain("blog-service")
                .build();
    }

    @Test
    void 등록하지않은_api_테스트() throws Exception {
        mockMvc.perform(get("/routing"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void 라우팅정보_등록_테스트() throws Exception {
        when(routingService.register(any(RoutingInformationDto.class))).thenReturn(routingInformationDto);

        mockMvc.perform(post("/routings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(routingInformationDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(om.writeValueAsString(routingInformationDto)))
                .andDo(print());
    }

    @Test
    void 라우팅정보_조회_테스트() throws Exception {
        RoutingInformationDto secondRoutingInformationDto = RoutingInformationDto.builder()
                .domain("news-service")
                .id("2")
                .method("GET")
                .path("/news.json")
                .baseUrl("https://openapi.naver.com")
                .build();

        List<RoutingInformationDto> result = new ArrayList<>(Arrays.asList(routingInformationDto, secondRoutingInformationDto));

        when(routingService.findAll()).thenReturn(result);

        mockMvc.perform(get("/routings"))
                .andExpect(status().isOk())
                .andExpect(content().string(om.writeValueAsString(result)))
                .andDo(print());
    }

    @Test
    void 라우팅정보_수정_테스트() throws Exception {

        when(routingService.updateById(any(RoutingInformationDto.class), eq(id))).thenReturn(routingInformationDto);

        mockMvc.perform(put("/routings/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(routingInformationDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(om.writeValueAsString(routingInformationDto)))
                .andDo(print());
    }

    @Test
    void 올바른_라우팅정보_삭제_테스트() throws Exception {
        mockMvc.perform(delete("/routings/" + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 잘못된_라우팅정보_삭제_테스트() throws Exception {
        doThrow(new ResponseException(new ErrorResponse(Error.NOT_FOUND)))
                .when(routingService)
                .deleteById(any(String.class));

        mockMvc.perform(delete("/routings/" + id))
                .andExpect(result -> {
                    Assertions.assertTrue(result.getResolvedException().getClass().isAssignableFrom(ResponseException.class));
                })
                .andDo(print());
    }

}