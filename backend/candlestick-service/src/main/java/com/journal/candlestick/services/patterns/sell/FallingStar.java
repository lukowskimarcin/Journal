package com.journal.candlestick.services.patterns.sell;

import java.util.List;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.CandlestickPattern;

public class FallingStar extends CandlestickPattern {
    public FallingStar(CandlestickConfig config) {
        super(config);
    }

    @Override
    public PatternDto match(List<CandleStickDto> data) {
        CandleStickDto candle = data.get(data.size() - 1);
        Double candleSize = candle.getHigh() - candle.getLow();
        Double cutOff = candle.getLow() + (config.getFallingStar().getMaxStem() * candleSize);
        Double exceptCutOff = candle.getLow() + (config.getFallingStar().getExceptionStem() * candleSize);

        if ((candle.getClose() <= cutOff && candle.getOpen() <= cutOff) ||
                (candle.getClose() < candle.getOpen() && candle.getOpen() <= exceptCutOff)

        ) {
            TrapType trap = this.checkIfTrap(Signal.SELL, candle.getHigh(), candle.getLow(),
                    data.subList(0, data.size() - 1));
            return new PatternDto(Signal.SELL, PatternName.FALLING_STAR, candle.getHigh(), candle.getLow(),
                    trap != null, trap);
        }
        return null;
    }

}
