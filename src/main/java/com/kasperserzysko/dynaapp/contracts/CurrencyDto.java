package com.kasperserzysko.dynaapp.contracts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyDto {

    private String name;
    private int units;
    private float avg;
}
