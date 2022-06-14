package com.koshelev.feign_clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.exchange_rates.name}" , url = "${feign.exchange_rates.url}")
public interface ExchangeRatesFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "latest.json?app_id=${feign.exchange_rates.app_id}" +
            "&base=${feign.exchange_rates.base}&")
    JsonNode getCurrentExchangeRates(@RequestParam(name = "symbols") String currencyCode);

    @RequestMapping(method = RequestMethod.GET, value = "historical/{yesterdays_date}?app_id=${feign.exchange_rates.app_id}" +
            "&base=${feign.exchange_rates.base}&")
    JsonNode getYesterdayExchangeRates(@PathVariable("yesterdays_date") String yesterdaysDate,
                                       @RequestParam(name = "symbols") String currencyCode);
}