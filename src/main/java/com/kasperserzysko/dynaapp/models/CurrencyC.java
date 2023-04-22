package com.kasperserzysko.dynaapp.models;


import lombok.Data;

import java.util.ArrayList;


@Data
public class CurrencyC{  //C because we get data from API table C
    private String table;
    private String currency;
    private String code;
    private ArrayList<RateC> rates;


    @Data
    public class RateC{
        private String no;
        private String effectiveDate;
        private double bid;
        private double ask;
    }
}