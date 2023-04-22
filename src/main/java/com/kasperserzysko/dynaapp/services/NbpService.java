package com.kasperserzysko.dynaapp.services;

import com.google.gson.Gson;
import com.kasperserzysko.dynaapp.contracts.CurrencyDto;
import com.kasperserzysko.dynaapp.exceptions.BadRequestException;
import com.kasperserzysko.dynaapp.exceptions.NotFoundException;
import com.kasperserzysko.dynaapp.mappers.CurrencyMapper;
import com.kasperserzysko.dynaapp.models.CurrencyA;
import com.kasperserzysko.dynaapp.models.CurrencyC;
import com.kasperserzysko.dynaapp.models.CurrencyCode;
import com.kasperserzysko.dynaapp.tools.Connector;
import com.kasperserzysko.dynaapp.tools.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NbpService {


    private final static String API_URL = "http://api.nbp.pl/api/exchangerates/rates/";
    private final CurrencyMapper mapper;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Connector connector;

    public CurrencyDto getAvgRate(Date date, String code) throws IOException, NotFoundException, BadRequestException {
        Gson gson = new Gson();
        log.info(date.toString());
        var response = connector.getResponse(API_URL + "A/" + CurrencyCode.valueOf(code.toUpperCase()) + "/" + dateFormat.format(date));
        return mapper.mapToDto.apply(gson.fromJson(response, CurrencyA.class));
    }

    public String getMaxAndMin(String code, int quotations) throws NotFoundException, BadRequestException, IOException {
        Gson gson = new Gson();
        var response = connector.getResponse(API_URL + "A/" + CurrencyCode.valueOf(code.toUpperCase()) + "/last/" + quotations);
        List<Float> midList = gson.fromJson(response, CurrencyA.class)
                .getRates()
                .stream()
                .map(rate -> (float) rate.getMid())
                .toList();

        return "Min: " + Utils.findMin(midList) + "\nMax: " + Utils.findMax(midList);
    }

    public String getBiggestDifference(String code, int quotations) throws NotFoundException, BadRequestException, IOException {
        Gson gson = new Gson();
        var response = connector.getResponse(API_URL + "C/" + CurrencyCode.valueOf(code.toUpperCase()) + "/last/" + quotations);
        List<Float> differenceList = gson.fromJson(response, CurrencyC.class)
                .getRates()
                .stream()
                .map(rate -> (float) (rate.getAsk() - rate.getBid()))
                .toList();

        return "Major difference: " + Utils.findMax(differenceList);
    }

}
