package com.journal.candlestick;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import com.journal.candlestick.services.patterns.buy.MorningStar;

@SpringBootTest
public class MoringStarTests {
    private List<CandleStickDto> data = new ArrayList<>();
    private MorningStar pattern;

    @Autowired
    private CandlestickConfig config;

    @BeforeEach
    void setup() {
        pattern = new MorningStar(config);

        data.add(new CandleStickDto(1l, "", 700.0, 700.0, 600.0, 600.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(2l, "", 600.0, 600.0, 500.0, 500.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(3l, "", 500.0, 500.0, 400.0, 400.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(4l, "", 400.0, 400.0, 300.0, 300.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(5l, "", 300.0, 300.0, 200.0, 200.0, 0l, 0l, 0l, 0.0, false, 0.0));
    }

    @Test
    void validPenetration() {
        data.add(new CandleStickDto(6l, "", 200.0, 200.0, 100.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 100.0, 105.0, 95.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 80.0, 200.0, 100.0, 160.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.BUY.equals(result.getSignal()));
        assertTrue(PatternName.MORNING_STAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void validBigDecissionCandle() {
        data.add(new CandleStickDto(6l, "", 200.0, 200.0, 100.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 100.0, 105.0, 95.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 80.0, 400.0, 100.0, 360.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.BUY.equals(result.getSignal()));
        assertTrue(PatternName.MORNING_STAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void toBigMiddleCandle() {
        data.add(new CandleStickDto(6l, "", 200.0, 200.0, 100.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 100.0, 135.0, 65.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 80.0, 200.0, 100.0, 160.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result == null);
    }

    @Test
    void insideBar() {
        data.add(new CandleStickDto(6l, "", 200.0, 700.0, 0.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 200.0, 200.0, 100.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 100.0, 105.0, 95.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(9l, "", 80.0, 200.0, 100.0, 160.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.BUY.equals(result.getSignal()));
        assertTrue(PatternName.MORNING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.INSIDE_BAR.equals(result.getTrapType()));
    }

    @Test
    void buyAtTop() {
        data.add(new CandleStickDto(6l, "", 2200.0, 2200.0, 2100.0, 2100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 2100.0, 2105.0, 2195.0, 2100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 2180.0, 2200.0, 2100.0, 2160.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.MORNING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.BUY_AT_PEAK.equals(result.getTrapType()));
    }

    @Test
    void tooLongShadowTrap() {
        data.add(new CandleStickDto(6l, "", 200.0, 200.0, 100.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 100.0, 105.0, 95.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(8l, "", 80.0, 400.0, 100.0, 160.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.MORNING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.TOO_LONG_SHADOWS.equals(result.getTrapType()));
    }

}
