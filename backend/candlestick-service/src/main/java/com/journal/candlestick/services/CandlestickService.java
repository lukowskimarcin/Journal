package com.journal.candlestick.services;

import java.util.List;

import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.dtos.CandlestickWithPatternsDto;

public interface CandlestickService {

        List<CandleStickDto> copyRatesFromPos(String symbol, String timeFrame, Integer count);

        List<CandleStickDto> copyRatesFrom(String symbol, String timeFrame, Long dateFrom, Integer count);

        List<CandleStickDto> copyRatesFromRange(String symbol, String timeFrame, Long dateFrom, Long dateTo);

        List<PatternDto> checkPatternsFromPos(String symbol, String timeFrame);

        List<PatternDto> checkPatternsFrom(String symbol, String timeFrame, Long dateFrom);

        List<CandlestickWithPatternsDto> scanPatternsFromTo(String symbol, String timeFrame, Long dateFrom,
                        Long dateTo);

}
