package com.journal.candlestick.services.patterns.sell;

import java.util.List;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.CandlestickPattern;

public class DarkCloudVeil extends CandlestickPattern {
    public DarkCloudVeil(CandlestickConfig config) {
        super(config);
    }

    private boolean toLongShadows(CandleStickDto candle, CandleStickDto decissionCandle) {
        Double downShadow = decissionCandle.getClose() - decissionCandle.getLow();
        Double percent = downShadow / (decissionCandle.getHigh() - decissionCandle.getLow()) * 100;
        return percent > config.getMaxShadowPercent();
    }

    @Override
    public PatternDto match(List<CandleStickDto> data) {
        CandleStickDto candle = data.get(data.size() - 2);
        Double candleSize = candle.getClose() - candle.getOpen();
        Double decissionPoint = candle.getOpen() + candleSize / 2;
        CandleStickDto decissionCandle = data.get(data.size() - 1);

        if (candle.getClose() > candle.getOpen()
                && decissionCandle.getClose() < decissionPoint) {
            Double high = candle.getHigh() > decissionCandle.getHigh() ? candle.getHigh() : decissionCandle.getHigh();
            Double low = candle.getLow() < decissionCandle.getLow() ? candle.getLow() : decissionCandle.getLow();

            TrapType trap = this.checkIfTrap(Signal.SELL, high, low, data.subList(0, data.size() - 2));

            if (toLongShadows(candle, decissionCandle)) {
                trap = TrapType.TOO_LONG_SHADOWS;
            }

            return new PatternDto(Signal.SELL, PatternName.DARK_CLOUD_VEIL, high, low, trap != null, trap);
        }

        return null;
    }
}
