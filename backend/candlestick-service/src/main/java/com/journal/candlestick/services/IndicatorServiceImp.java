package com.journal.candlestick.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.journal.candlestick.dtos.CandleStickDto;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class IndicatorServiceImp implements IndicatorService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public List<CandleStickDto> importance(List<CandleStickDto> data, Integer scan) {
        logger.info("Calculate importance  scan: {}", scan);

        int index = 0;
        for (CandleStickDto candle : data) {
            if (index >= scan) {
                Double candleSize = Math.round((candle.getHigh() - candle.getLow()) * 100.0) / 100.0;
                List<CandleStickDto> previous = data.subList(index - scan, index);
                long smallerCandlesCount = previous.stream().filter(c -> candleSize > (c.getHigh() - c.getLow()))
                        .count();

                candle.setImportant(smallerCandlesCount > (scan / 2) + 1);
                candle.setCandleSize(candleSize);
            }
            index++;
        }

        return data;
    }

    private double calcSmmaUp(List<CandleStickDto> candlesticks, double n, int i, double avgUt1) {
        if (avgUt1 == 0) {
            double sumUpChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks.get(i - j).getClose() - candlesticks.get(i - j).getOpen();

                if (change > 0) {
                    sumUpChanges += change;
                }
            }
            return sumUpChanges / n;
        } else {
            double change = candlesticks.get(i).getClose() - candlesticks.get(i).getOpen();
            if (change < 0) {
                change = 0;
            }
            return ((avgUt1 * (n - 1)) + change) / n;
        }
    }

    private double calcSmmaDown(List<CandleStickDto> candlesticks, double n, int i, double avgDt1) {
        if (avgDt1 == 0) {
            double sumDownChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks.get(i - j).getClose() - candlesticks.get(i - j).getOpen();

                if (change < 0) {
                    sumDownChanges -= change;
                }
            }
            return sumDownChanges / n;
        } else {
            double change = candlesticks.get(i).getClose() - candlesticks.get(i).getOpen();
            if (change > 0) {
                change = 0;
            }
            return ((avgDt1 * (n - 1)) - change) / n;
        }
    }

    @Override
    public List<CandleStickDto> rsi(List<CandleStickDto> data, Integer scan) {
        logger.info("Calculate RSI  scan: {}", scan);
        double ut1 = 0;
        double dt1 = 0;
        for (int i = 0; i < data.size(); i++) {
            if (i < scan) {
                continue;
            }
            ut1 = calcSmmaUp(data, scan, i, ut1);
            dt1 = calcSmmaDown(data, scan, i, dt1);

            double value = Math.round((100.0 - 100.0 / (1.0 + ut1 / dt1)) * 100) / 100;
            data.get(i).setRsi(value);
        }
        return data;
    }

}
