package com.kasperserzysko.dynaapp.mappers;

import com.kasperserzysko.dynaapp.contracts.CurrencyDto;
import com.kasperserzysko.dynaapp.models.CurrencyA;
import com.kasperserzysko.dynaapp.models.CurrencyCode;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CurrencyMapper {

    public final Function<CurrencyA, CurrencyDto> mapToDto = currencyA -> CurrencyDto.builder()
                .name(currencyA.getCurrency())
                .avg((float)currencyA.getRates().get(0).getMid())
                .units(checkUnits(CurrencyCode.valueOf(currencyA.getCode())))
                .build();

    private int checkUnits(CurrencyCode code) {
        switch (code){
            case CLP, HUF, ISK, INR, KRW, JPY -> {
                return 100;
            }
            case IDR -> {
                return 10000;
            }
            default -> {
                return 1;
            }
        }

    }

}
