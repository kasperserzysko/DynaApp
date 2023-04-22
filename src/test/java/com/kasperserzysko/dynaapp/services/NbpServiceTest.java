package com.kasperserzysko.dynaapp.services;

import com.kasperserzysko.dynaapp.contracts.CurrencyDto;
import com.kasperserzysko.dynaapp.exceptions.BadRequestException;
import com.kasperserzysko.dynaapp.exceptions.NotFoundException;
import com.kasperserzysko.dynaapp.mappers.CurrencyMapper;
import com.kasperserzysko.dynaapp.models.CurrencyCode;
import com.kasperserzysko.dynaapp.tools.Connector;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class NbpServiceTest {

    private NbpService nbpService;


    @BeforeEach
    void setUp() {
        nbpService = new NbpService(new CurrencyMapper(), new Connector());
    }

    @Test
    void should_throwIllegalArgumentException_with_wrongCode() throws Exception {
        Date date = new Date(1681123123123L);       //2023-04-10
        assertThrows(IllegalArgumentException.class, () -> nbpService.getAvgRate(date, "XYZ"));
    }

    @Test
    void should_throwBadRequestException_with_dateInTheFuture() {
        Date date = new Date(1823123123123L);       //2027-10-10
        assertThrows(BadRequestException.class, () -> nbpService.getAvgRate(date, "usd"));
    }
    @Test
    void should_throwNotFoundException_with_lackOfData() {
        Date date = new Date(1523123123123L);       //2018-04-07
        assertThrows(NotFoundException.class, () -> nbpService.getAvgRate(date, "usd"));
    }

}