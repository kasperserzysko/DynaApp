package com.kasperserzysko.dynaapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
public class CurrencyA { //A because we get data from API table A

    private String table;
    private String currency;
    private String code;
    private ArrayList<RateA> rates;


    @Data
    @NoArgsConstructor
    public class RateA {
        private String no;
        private String effectiveDate;
        private double mid;
    }
}



