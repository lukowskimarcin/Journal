package com.journal.candlestick.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "candlestick.pattern")
@Data
public class CandlestickConfig {
    private Integer scanSize;
    private Integer trapScan;
    private Double maxShadowPercent;

    private Hammer hammer = new Hammer();
    private MorningStar morningStar = new MorningStar();
    private EveningStar eveningStar = new EveningStar();
    private FallingStar fallingStar = new FallingStar();

    @Data
    public static class Hammer {
        private Double maxStem;
        private Double exceptionStem;
    }

    @Data
    public static class MorningStar {
        private Double maxMiddleCandlePercent;
    }

    @Data
    public static class EveningStar {
        private Double maxMiddleCandlePercent;
    }

    @Data
    public static class FallingStar {
        private Double maxStem;
        private Double exceptionStem;
    }

}
