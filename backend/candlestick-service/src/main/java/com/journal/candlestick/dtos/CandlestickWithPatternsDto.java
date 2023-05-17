package com.journal.candlestick.dtos;

import java.util.List;

import lombok.Getter;

@Getter
public class CandlestickWithPatternsDto extends CandleStickDto {
    private final List<PatternDto> patterns;

    public CandlestickWithPatternsDto(CandleStickDto candle, List<PatternDto> patterns) {
        this.patterns = patterns;
        this.setTime(candle.getTime());
        this.setHumanTime(candle.getHumanTime());
        this.setOpen(candle.getOpen());
        this.setHigh(candle.getHigh());
        this.setLow(candle.getLow());
        this.setClose(candle.getClose());
        this.setTickVolume(candle.getTickVolume());
        this.setSpread(candle.getSpread());
        this.setRealVolume(candle.getRealVolume());
        this.setRsi(candle.getRsi());
        this.setImportant(candle.isImportant());
        this.setCandleSize(candle.getCandleSize());
    }

}
