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
import com.journal.candlestick.services.patterns.sell.FallingStar;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FallingStarTests {

    private List<CandleStickDto> data = new ArrayList<>();
    private FallingStar pattern;

    @Autowired
    private CandlestickConfig config;

    @BeforeEach
    void setup() {
        pattern = new FallingStar(config);

        data.add(new CandleStickDto(1l, "", 0.0, 50.0, 0.0, 40.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(2l, "", 40.0, 80.0, 30.0, 80.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(3l, "", 80.0, 120.0, 80.0, 100.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(4l, "", 100.0, 150.0, 90.0, 140.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(5l, "", 150.0, 200.0, 120.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
    }

    @Test
    void validRedFallingStar() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(Signal.SELL.equals(result.getSignal()));
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void validGreenFallingStar() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 200.0, 220.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void validFallingStarSmallStem() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 180.0, 201.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(!result.isTrap());
    }

    @Test
    void validFallingStartoBigStem() {
        data.add(new CandleStickDto(6l, "", 200.0, 300.0, 180.0, 250.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);
        assertTrue(result == null);
    }

    @Test
    void validFallingStarStemToHigh() {
        data.add(new CandleStickDto(6l, "", 250.0, 300.0, 200.0, 260.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);
        assertTrue(result == null);
    }

    @Test
    void fallingStarWithOverlapTrap() {
        data.add(new CandleStickDto(6l, "", 200.0, 400.0, 0.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 200.0, 300.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.INSIDE_BAR.equals(result.getTrapType()));
    }

    @Test
    void fallingStarWithPartialOverlapTrap() {
        data.add(new CandleStickDto(6l, "", 200.0, 400.0, 0.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 200.0, 300.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
    }

    @Test
    void fallingStarAtBottomTrap() {
        data.add(new CandleStickDto(6l, "", 220.0, 500.0, 220.0, 300.0, 0l, 0l, 0l, 0.0, false, 0.0));
        data.add(new CandleStickDto(7l, "", 200.0, 300.0, 180.0, 180.0, 0l, 0l, 0l, 0.0, false, 0.0));
        PatternDto result = pattern.match(data);

        assertTrue(result != null);
        assertTrue(PatternName.FALLING_STAR.equals(result.getName()));
        assertTrue(result.isTrap());
        assertTrue(TrapType.SELL_AT_BOTTOM.equals(result.getTrapType()));
    }

}
