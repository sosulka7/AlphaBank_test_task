package com.koshelev.services;

import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.feign_clients.ExchangeRatesFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ExchangeRatesService {

    private final ExchangeRatesFeignClient exchangeRatesFeignClient;

    public BigDecimal getCurrentExchangeRates(String currencyCode) throws InvalidRequestArgument {
        String rate = exchangeRatesFeignClient.getCurrentExchangeRates(currencyCode).path("rates")
                .path(currencyCode.toUpperCase()).asText();
        if (rate.equals("")){
            throw new InvalidRequestArgument("Invalid query argument 'code'. The specified currency code will not be found.");
        }
        return new BigDecimal(rate);
    }

    public BigDecimal getYesterdayExchangeRates(String currencyCode) throws InvalidRequestArgument {
        String yesterdayDate = getYesterdayDateString() + ".json";
        String rate = exchangeRatesFeignClient.getYesterdayExchangeRates(yesterdayDate, currencyCode).path("rates")
                .path(currencyCode.toUpperCase()).asText();
        if (rate.equals("")){
            throw new InvalidRequestArgument("Invalid query argument 'code'. The specified currency code will not be found.");
        }
        return new BigDecimal(rate);
    }

    private String getYesterdayDateString(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = Date.from(Instant.now());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        return dateFormat.format(date);
    }

}
