package com.journal.candlestick.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.journal.candlestick.clients.Mt5ServiceProxy;
import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.config.IndicatorConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.dtos.CandlestickWithPatternsDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CandlestickServiceImpl implements CandlestickService {
    private final Mt5ServiceProxy mt5Service;
    private final IndicatorService indicatorService;
    private final CandleRecognitionService candleRecognitionService;

    private final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    private final CandlestickConfig candlestickConfig;

    private final IndicatorConfig indicatorConfig;

    private List<CandleStickDto> calculateData(List<CandleStickDto> data) {
        data = indicatorService.importance(data, indicatorConfig.getImportanceScanSize());
        data = indicatorService.rsi(data, indicatorConfig.getRsiScanSize());
        df.setTimeZone(TimeZone.getTimeZone("GMT"));

        data.stream().forEach(candle -> candle
                .setHumanTime(df.format(Date.from(Instant.ofEpochSecond(candle.getTime())))));

        return data;
    }

    @Override
    public List<CandleStickDto> copyRatesFromPos(String symbol, String timeFrame, Integer count) {
        Integer extra_candles = indicatorConfig.getImportanceScanSize() > indicatorConfig.getRsiScanSize()
                ? indicatorConfig.getImportanceScanSize()
                : indicatorConfig.getRsiScanSize();
        List<CandleStickDto> data = mt5Service.copyRatesFromPos(symbol, timeFrame, count + extra_candles);
        return calculateData(data).subList(data.size() - count, data.size());
    }

    @Override
    public List<CandleStickDto> copyRatesFrom(String symbol, String timeFrame, Long dateFrom, Integer count) {
        Integer extra_candles = indicatorConfig.getImportanceScanSize() > indicatorConfig.getRsiScanSize()
                ? indicatorConfig.getImportanceScanSize()
                : indicatorConfig.getRsiScanSize();
        List<CandleStickDto> data = mt5Service.copyRatesFrom(symbol, timeFrame, dateFrom, count + extra_candles);
        return calculateData(data).subList(data.size() - count, data.size());
    }

    @Override
    public List<CandleStickDto> copyRatesFromRange(String symbol, String timeFrame, Long dateFrom, Long dateTo) {
        List<CandleStickDto> data = mt5Service.copyRatesFromRange(symbol, timeFrame, dateFrom, dateTo);
        Integer extra_candles = indicatorConfig.getImportanceScanSize() > indicatorConfig.getRsiScanSize()
                ? indicatorConfig.getImportanceScanSize()
                : indicatorConfig.getRsiScanSize();

        List<CandleStickDto> preData = mt5Service.copyRatesFrom(symbol, timeFrame, dateFrom, extra_candles);
        preData.subList(0, preData.size() - 1).addAll(data);
        data = calculateData(preData).subList(extra_candles, preData.size());

        return data;
    }

    @Override
    public List<PatternDto> checkPatternsFromPos(String symbol, String timeFrame) {
        List<CandleStickDto> data = mt5Service.copyRatesFromPos(symbol, timeFrame, candlestickConfig.getScanSize() + 1);
        return candleRecognitionService.detect(data.subList(0, data.size() - 1));
    }

    @Override
    public List<PatternDto> checkPatternsFrom(String symbol, String timeFrame, Long dateFrom) {
        List<CandleStickDto> data = mt5Service.copyRatesFrom(symbol, timeFrame, dateFrom,
                candlestickConfig.getScanSize() + 1);

        return candleRecognitionService.detect(data.subList(0, data.size() - 1));
    }

    @Override
    public List<CandlestickWithPatternsDto> scanPatternsFromTo(String symbol, String timeFrame, Long dateFrom,
            Long dateTo) {

        List<CandleStickDto> data = copyRatesFromRange(symbol, timeFrame, dateFrom, dateTo);
        List<CandlestickWithPatternsDto> result = new ArrayList<>(data.stream().map(candle -> {
            List<PatternDto> patterns = checkPatternsFrom(symbol, timeFrame, candle.getTime());
            return new CandlestickWithPatternsDto(candle, patterns);
        }).toList());

        Collections.sort(result, (a, b) -> a.getTime().compareTo(b.getTime()));
        return result;
    }
}
