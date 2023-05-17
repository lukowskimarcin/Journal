package com.journal.candlestick.services.patterns;

import java.util.List;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;

public abstract class CandlestickPattern {

    protected final CandlestickConfig config;

    public CandlestickPattern(CandlestickConfig config) {
        this.config = config;
    }

    public abstract PatternDto match(List<CandleStickDto> data);

    protected TrapType checkIfTrap(Signal signal, Double high, Double low, List<CandleStickDto> data) {
        List<CandleStickDto> lastFive = data.subList(data.size() - config.getTrapScan(), data.size());

        boolean overlap = data.stream().anyMatch(c -> c.getHigh() >= high && c.getLow() <= low);
        if (overlap) {
            return TrapType.INSIDE_BAR;
        }

        if (Signal.BUY.equals(signal)) {
            boolean partialOverlap = lastFive.stream().anyMatch(c -> c.getLow() < low);
            if (partialOverlap) {
                return TrapType.BUY_AT_PEAK;
            }

        } else if (Signal.SELL.equals(signal)) {
            boolean partialOverlap = lastFive.stream().anyMatch(c -> c.getHigh() > high);
            if (partialOverlap) {
                return TrapType.SELL_AT_BOTTOM;
            }
        }

        return null;
    }
}
