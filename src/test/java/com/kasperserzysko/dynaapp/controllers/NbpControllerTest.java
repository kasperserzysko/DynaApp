package com.kasperserzysko.dynaapp.controllers;

import com.google.gson.Gson;
import com.kasperserzysko.dynaapp.contracts.CurrencyDto;
import com.kasperserzysko.dynaapp.exceptions.NotFoundException;
import com.kasperserzysko.dynaapp.services.NbpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class NbpControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private NbpService nbpService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void should_returnAvg_whenValidParams() throws Exception {

        String code = "USD";
        CurrencyDto currencyDto = CurrencyDto
                .builder()
                .avg(4.4372f)
                .units(1)
                .name("dolar amerykański")
                .build();

        when(nbpService.getAvgRate(Mockito.any(Date.class), eq(code))).thenReturn(currencyDto);


        mockMvc.perform(get("/rate")
                        .param("date", "2023-2-15")
                        .param("code", "USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(currencyDto.getName()))
                .andExpect(jsonPath("$.units").value(currencyDto.getUnits()))
                .andExpect(jsonPath("$.avg").value(currencyDto.getAvg()));
    }
    @Test
    public void should_ReturnsBadRequest_WhenDateIsFuture() throws Exception {
        mockMvc.perform(get("/rate")
                        .param("date", "2099-04-23")
                        .param("code", "USD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void should_ReturnsBadRequest_WhenCurrencyCodeIsBlank() throws Exception {

        mockMvc.perform(get("/rate")
                        .param("date", "2022-04-23")
                        .param("code", ""))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void should_ReturnsStringMinAndMax_WhenValidParams() throws Exception {
        int quotations = 10;
        String code = "USD";
        String minMaxString = "Min: 1.2\nMax: 1.5";
        when(nbpService.getMaxAndMin(code, quotations)).thenReturn(minMaxString);

        mockMvc.perform(get("/minmax")
                        .param("quotations", "10")
                        .param("code", "USD"))
                .andExpect(status().isOk())
                .andExpect(content().string(minMaxString));
    }

    @Test
    public void should_returnsBadRequest_whenQuotationsLesserThanZero() throws Exception {
        String code = "USD";

        mockMvc.perform(get("/minmax")
                        .param("quotations", "-1")
                        .param("code", code))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void should_ReturnsStringDiff_WhenValidParams() throws Exception {
        String code = "GBP";
        int quotations = 14;
        String expectedResponse = "Major difference: 1.5";

        given(nbpService.getBiggestDifference(code, quotations)).willReturn(expectedResponse);

        MvcResult result = mockMvc.perform(get("/diff")
                        .param("quotations", String.valueOf(quotations))
                        .param("code", code))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponse = result.getResponse().getContentAsString();

        assertEquals(expectedResponse, actualResponse);
    }
}