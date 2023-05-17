package com.journal.candlestick.services.patterns.sell;

import java.util.List;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.CandlestickPattern;

public class EveningStar extends CandlestickPattern {
    public EveningStar(CandlestickConfig config) {
        super(config);
    }

    private boolean toLongShadows(CandleStickDto candle, CandleStickDto decissionCandle) {
        Double downShadow = decissionCandle.getClose() - decissionCandle.getLow();
        Double percent = downShadow / (decissionCandle.getHigh() - decissionCandle.getLow()) * 100;
        return percent > config.getMaxShadowPercent();
    }

    @Override
    public PatternDto match(List<CandleStickDto> data) {
        CandleStickDto candle = data.get(data.size() - 3);
        Double candleSize = Math.abs(candle.getClose() - candle.getOpen());
        Double decissionPoint = candle.getOpen() + candleSize / 2;
        CandleStickDto middleCandle = data.get(data.size() - 2);
        CandleStickDto decissionCandle = data.get(data.size() - 1);
        Double decissionCandleSize = Math.abs(decissionCandle.getClose() - decissionCandle.getOpen());
        Double middleCandleSize = Math.abs(middleCandle.getHigh() - middleCandle.getLow());
        Double compareSize = candleSize > decissionCandleSize ? decissionCandleSize : candleSize;

        if (candle.getClose() > candle.getOpen()
                && decissionCandle.getClose() < decissionPoint
                && (middleCandleSize / compareSize * 100) < config.getEveningStar().getMaxMiddleCandlePercent()) {

            Double high = Math.max(candle.getHigh(), Math.max(decissionCandle.getHigh(), middleCandle.getHigh()));
            Double low = Math.min(candle.getLow(), Math.min(decissionCandle.getLow(), middleCandle.getLow()));
            TrapType trap = this.checkIfTrap(Signal.SELL, high, low, data.subList(0, data.size() - 3));

            if (toLongShadows(candle, decissionCandle)) {
                trap = TrapType.TOO_LONG_SHADOWS;
            }
            return new PatternDto(Signal.SELL, PatternName.EVENING_STAR, high, low, trap != null, trap);
        }

        return null;
    }
}