package com.journal.candlestick.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.journal.candlestick.dtos.CandleStickDto;

@FeignClient(name = "mt5-service")
public interface Mt5ServiceProxy {

        @GetMapping("/api/v1/mt5/copy_rates_from/{symbol}/{timeFrame}/{dateFrom}/{count}")
        public List<CandleStickDto> copyRatesFrom(
                        @PathVariable("symbol") String symbol,
                        @PathVariable("timeFrame") String timeFrame,
                        @PathVariable("dateFrom") Long dateFrom,
                        @PathVariable("count") Integer count);

        @GetMapping("/api/v1/mt5/copy_rates_from_pos/{symbol}/{timeFrame}/{count}")
        public List<CandleStickDto> copyRatesFromPos(
                        @PathVariable("symbol") String symbol,
                        @PathVariable("timeFrame") String timeFrame,
                        @PathVariable("count") Integer count);

        @GetMapping("/api/v1/mt5/copy_rates_range/{symbol}/{timeFrame}/{dateFrom}/{dateTo}")
        public List<CandleStickDto> copyRatesFromRange(
                        @PathVariable("symbol") String symbol,
                        @PathVariable("timeFrame") String timeFrame,
                        @PathVariable("dateFrom") Long dateFrom,
                        @PathVariable("dateTo") Long dateTo);
}
