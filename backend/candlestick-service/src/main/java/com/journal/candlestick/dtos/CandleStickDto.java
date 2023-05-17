package com.journal.candlestick.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CandleStickDto {
    private Long time;
    private String humanTime;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long tickVolume;
    private Long spread;
    private Long realVolume;

    private Double rsi;

    // czy wieksza od polowy z 20 poprzednich
    private boolean important;
    private Double candleSize;
}
