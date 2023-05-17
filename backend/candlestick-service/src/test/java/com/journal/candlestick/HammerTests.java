package com.journal.candlestick;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.journal.candlestick.config.CandlestickConfig;
import com.journal.candlestick.dtos.CandleStickDto;
import com.journal.candlestick.dtos.PatternDto;
import com.journal.candlestick.models.PatternName;
import com.journal.candlestick.models.Signal;
import com.journal.candlestick.models.TrapType;
import com.journal.candlestick.services.patterns.buy.Hammer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class HammerTests {

    private List<CandleStickDto> data = new ArrayList<>();
    private Hammer pattern;

    @Autowired
    private CandlestickConfig config;

    @BeforeEach
    void setup() {
        pattern = new Hammer(config);

        data.add(new CandleStickDto(1l, "", 180.0, 200.0, 130.0, 150.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(2l, "", 180.0, 200.0, 130.0, 150.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(3l, "", 180.0, 200.0, 130.0, 150.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(4l, "", 150.0, 160.0, 120.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(5l, "", 180.0, 200.0, 130.0, 150.0, 0l, 0l, 0l, 0.0, false, 0.0));
    }

    @Test
    void validGreenHammer() {
        data.add(new CandleStickDto(6l, "", 80.0, 100.0, 10.0, 90.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.BUY.equals(result.getSignal()));
        assertTrue(PatternName.HAMMER.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void validHammerSmallStem() {
        data.add(new CandleStickDto(6l, "", 75.0, 100.0, 0.0, 75.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.HAMMER.equals(result.getName()));
    }

    @Test
    void hammerWithToBigStem() {
        data.add(new CandleStickDto(6l, "", 100.0, 100.0, 0.0, 60.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);
        assertTrue(result == null);
    }

    @Test
    void hammerStemToLow() {
        data.add(new CandleStickDto(6l, "", 70.0, 100.0, 0.0, 60.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);
        assertTrue(result == null);
    }

    @Test
    void hammerWithOverlapTrap() {
        // RED
        data.add(new CandleStickDto(6l, "", 100.0, 100.0, 0.0, 0.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 80.0, 100.0, 0.0, 90.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.HAMMER.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.INSIDE_BAR.equals(result.getTrapType()));
    }

    @Test
    void hammerOnTopOfPreviousOne() {
        data.add(new CandleStickDto(6l, "", 10180.0, 10200.0, 10100.0, 10190.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.HAMMER.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.BUY_AT_PEAK.equals(result.getTrapType()));
    }

}
