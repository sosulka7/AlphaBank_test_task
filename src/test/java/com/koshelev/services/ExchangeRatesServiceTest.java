package com.koshelev.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.feign_clients.ExchangeRatesFeignClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest(classes = ExchangeRatesService.class)
public class ExchangeRatesServiceTest {

    @MockBean
    private ExchangeRatesFeignClient exchangeRatesFeignClient;

    @Autowired
    private ExchangeRatesService exchangeRatesService;

    @Test
    public void getCurrentExchangeRates_onAValidRequest() throws InvalidRequestArgument {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseFeignClient = mapper.createObjectNode();
        responseFeignClient.put("base", "USD");
        ObjectNode rates = mapper.createObjectNode();
        rates.put("RUB", "60.01000");
        responseFeignClient.set("rates", rates);
        Mockito.when(exchangeRatesFeignClient.getCurrentExchangeRates("RUB")).thenReturn(responseFeignClient);
        BigDecimal currentRate = exchangeRatesService.getCurrentExchangeRates("RUB");
        Assertions.assertEquals(new BigDecimal("60.01000"), currentRate);
    }

    @Test
    public void getCurrentExchangeRates_withAnInvalidCurrencyCode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseFeignClient = mapper.createObjectNode();
        responseFeignClient.put("base", "USD");
        ObjectNode rates = mapper.createObjectNode();
        responseFeignClient.set("rates", rates);
        Mockito.when(exchangeRatesFeignClient.getCurrentExchangeRates("QWE")).thenReturn(responseFeignClient);
        Assertions.assertThrows(InvalidRequestArgument.class, ()-> exchangeRatesService.getCurrentExchangeRates("QWE"));
    }


    @Test
    public void getYesterdayExchangeRates_onAValidRequest() throws InvalidRequestArgument {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseFeignClient = mapper.createObjectNode();
        responseFeignClient.put("base", "USD");
        ObjectNode rates = mapper.createObjectNode();
        rates.put("RUB", "30.00100");
        responseFeignClient.set("rates", rates);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Date.from(Instant.now());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        String yesterdayDate = dateFormat.format(date) + ".json";
        Mockito.when(exchangeRatesFeignClient.getYesterdayExchangeRates(yesterdayDate, "RUB")).thenReturn(responseFeignClient);
        BigDecimal currentRate = exchangeRatesService.getYesterdayExchangeRates("RUB");
        Assertions.assertEquals(new BigDecimal("30.00100"), currentRate);
    }

    @Test
    public void getYesterdayExchangeRates_withAnInvalidCurrencyCode(){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode responseFeignClient = mapper.createObjectNode();
        responseFeignClient.put("base", "USD");
        ObjectNode rates = mapper.createObjectNode();
        responseFeignClient.set("rates", rates);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Date.from(Instant.now());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        String yesterdayDate = dateFormat.format(date) + ".json";
        Mockito.when(exchangeRatesFeignClient.getYesterdayExchangeRates(yesterdayDate, "QWE")).thenReturn(responseFeignClient);
        Assertions.assertThrows(InvalidRequestArgument.class, ()-> exchangeRatesService.getYesterdayExchangeRates("QWE"));
    }
}
