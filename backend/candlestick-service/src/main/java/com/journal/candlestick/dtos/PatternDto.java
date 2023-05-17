package com.journal.candlestick.dtos;

import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class PatternDto {
    private Signal signal;
    private PatternName name;

    private Double high;
    private Double low;

    private boolean trap;
    private TrapType trapType;
}
