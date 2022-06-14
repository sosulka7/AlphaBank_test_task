package com.koshelev.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.koshelev.dto.GifUrlResponse;
import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.feign_clients.GifsFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class GifsService {

    private final GifsFeignClient gifsFeignClient;

    private final ExchangeRatesService exchangeRatesService;

    public GifUrlResponse getGifUrlPerRelationExchangeRates(String currencyCode) throws InvalidRequestArgument {
        BigDecimal currentRate = exchangeRatesService.getCurrentExchangeRates(currencyCode);
        BigDecimal yesterdayRate = exchangeRatesService.getYesterdayExchangeRates(currencyCode);
        return currentRate.compareTo(yesterdayRate) > 0 ? new GifUrlResponse(getRichGifUrl())
                : new GifUrlResponse(getBrokeGifUrl());
    }

    public String getRichGifUrl(){
        JsonNode json = gifsFeignClient.getRandomRichGif();
        return getUrlFromJson(json);
    }

    public String getBrokeGifUrl(){
        JsonNode json = gifsFeignClient.getRandomBrokeGif();
        return getUrlFromJson(json);
    }

    private String getUrlFromJson(JsonNode json){
        return json.path("data").path("images").path("original").path("url").asText();
    }
}
