package com.journal.candlestick.services;

import java.util.List;

import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;

public interface CandleRecognitionService {
    List<PatternDto> detect(List<CandleStickDto> data);
}
