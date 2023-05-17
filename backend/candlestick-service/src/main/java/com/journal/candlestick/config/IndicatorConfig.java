package com.journal.candlestick.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "indicator")
@Data
public class IndicatorConfig {
    private Integer importanceScanSize;
    private Integer rsiScanSize;
}
