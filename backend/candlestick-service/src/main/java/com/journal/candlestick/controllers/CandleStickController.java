package com.journal.candlestick.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.services.CandlestickService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/candlestick")
@AllArgsConstructor
public class CandleStickController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CandlestickService candlestickService;

    @GetMapping("/data_from_pos/{symbol}/{timeFrame}/{count}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<CandleStickDto> copyRatesFromPos(@PathVariable String symbol,
            @PathVariable String timeFrame,
            @PathVariable Integer count) {

        logger.info("Fetch candlestick data for {} {}", symbol, timeFrame);
        return candlestickService.copyRatesFromPos(symbol, timeFrame, count);
    }

    @GetMapping("/data_from/{symbol}/{timeFrame}/{dateFrom}/{count}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<CandleStickDto> copyRatesFrom(@PathVariable String symbol,
            @PathVariable String timeFrame,
            @PathVariable Long dateFrom,
            @PathVariable Integer count) {

        logger.info("Fetch candlestick data for {} {} since: {}", symbol, timeFrame, dateFrom);
        return candlestickService.copyRatesFrom(symbol, timeFrame, dateFrom, count);
    }

    @GetMapping("/data_from_range/{symbol}/{timeFrame}/{dateFrom}/to/{dateTo}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<CandleStickDto> copyRatesFrom(@PathVariable String symbol,
            @PathVariable String timeFrame,
            @PathVariable Long dateFrom,
            @PathVariable Long dateTo) {

        logger.info("Fetch candlestick data for {} {} since: {} to {}", symbol, timeFrame, dateFrom, dateTo);
        return candlestickService.copyRatesFromRange(symbol, timeFrame, dateFrom, dateTo);
    }

}
