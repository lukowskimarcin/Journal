package com.journal.candlestick.services.patterns.buy;

import java.util.List;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.CandlestickPattern;

public class Hammer extends CandlestickPattern {
    public Hammer(CandlestickConfig config) {
        super(config);
    }

    @Override
    public PatternDto match(List<CandleStickDto> data) {
        CandleStickDto candle = data.get(data.size() - 1);
        Double candleSize = candle.getHigh() - candle.getLow();
        Double cutOff = candle.getHigh() - (config.getHammer().getMaxStem() * candleSize);
        Double exceptCutOff = candle.getHigh() - (config.getHammer().getExceptionStem() * candleSize);

        if ((candle.getClose() >= cutOff && candle.getOpen() >= cutOff)
                || (candle.getOpen() < candle.getClose() && candle.getOpen() >= exceptCutOff)) {
            TrapType trap = this.checkIfTrap(Signal.BUY, candle.getHigh(), candle.getLow(),
                    data.subList(0, data.size() - 1));
            return new PatternDto(Signal.BUY, PatternName.HAMMER, candle.getHigh(), candle.getLow(),
                    trap != null, trap);
        }
        return null;
    }
}