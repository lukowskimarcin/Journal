package com.journal.candlestick.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journal.candlestick.dtos.CandlestickWithPatternsDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.services.CandlestickService;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/candlestick")
@AllArgsConstructor
public class PatternsController {
    private final CandlestickService candlestickService;

    @GetMapping("/check_patterns_from_pos/{symbol}/{timeFrame}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<PatternDto> checkPatternsFromPos(@PathVariable String symbol,
            @PathVariable String timeFrame) {
        return candlestickService.checkPatternsFromPos(symbol, timeFrame);
    }

    @GetMapping("/check_patterns_from/{symbol}/{timeFrame}/{dateFrom}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<PatternDto> checkPatternsFrom(@PathVariable String symbol,
            @PathVariable String timeFrame,
            @PathVariable Long dateFrom) {
        return candlestickService.checkPatternsFrom(symbol, timeFrame, dateFrom);
    }

    @GetMapping("/scan_patterns_from/{symbol}/{timeFrame}/{dateFrom}/to/{dateTo}")
    @Retry(name = "candlestick_retry_conf")
    @RateLimiter(name = "candlestick_rate_limiter_conf")
    public List<CandlestickWithPatternsDto> scanPatternsFromTo(@PathVariable String symbol,
            @PathVariable String timeFrame,
            @PathVariable Long dateFrom,
            @PathVariable Long dateTo) {
        return candlestickService.scanPatternsFromTo(symbol, timeFrame, dateFrom, dateTo);
    }

}
