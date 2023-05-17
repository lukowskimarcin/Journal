package com.journal.candlestick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("com.journal.candlestick.clients")
public class CandlestickServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandlestickServiceApplication.class, args);
	}

}