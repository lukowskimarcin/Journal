package com.journal.candlestick.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.services.patterns.CandlestickPattern;
import com.journal.candlestick.services.patterns.buy.EmbracingBull;
import com.journal.candlestick.services.patterns.buy.Hammer;
import com.journal.candlestick.services.patterns.buy.MorningStar;
import com.journal.candlestick.services.patterns.buy.Penetration;
import com.journal.candlestick.services.patterns.sell.DarkCloudVeil;
import com.journal.candlestick.services.patterns.sell.EmbracingBear;
import com.journal.candlestick.services.patterns.sell.EveningStar;
import com.journal.candlestick.services.patterns.sell.FallingStar;
import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;

@Service
public class CandleRecognitionServiceImpl implements CandleRecognitionService {
    private List<CandlestickPattern> patterns = new ArrayList<>();

    public CandleRecognitionServiceImpl(CandlestickConfig config) {
        patterns.add(new Hammer(config));
        patterns.add(new EmbracingBull(config));
        patterns.add(new MorningStar(config));
        patterns.add(new Penetration(config));

        patterns.add(new DarkCloudVeil(config));
        patterns.add(new EmbracingBear(config));
        patterns.add(new EveningStar(config));
        patterns.add(new FallingStar(config));
    }

    @Override
    public List<PatternDto> detect(List<CandleStickDto> data) {
        List<PatternDto> result = new ArrayList<>();
        Collections.sort(data, (a, b) -> a.getTime().compareTo(b.getTime()));
        patterns.forEach(pattern -> {
            PatternDto candle = pattern.match(data);
            if (candle != null) {
                result.add(candle);
            }
        });

        return result;
    }
}
