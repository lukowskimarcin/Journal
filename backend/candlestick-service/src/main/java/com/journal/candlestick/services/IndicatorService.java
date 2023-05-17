package com.journal.candlestick.services;

import java.util.List;

import com.journal.candlestick.dtos.CandleStickDto;

public interface IndicatorService {
    List<CandleStickDto> importance(List<CandleStickDto> data, Integer scan);

    List<CandleStickDto> rsi(List<CandleStickDto> data, Integer scan);
}
