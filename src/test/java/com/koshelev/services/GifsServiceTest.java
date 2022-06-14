package com.koshelev.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.feign_clients.GifsFeignClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

@SpringBootTest(classes = GifsService.class)
public class GifsServiceTest {

    @MockBean
    private ExchangeRatesService exchangeRatesService;

    @MockBean
    private GifsFeignClient gifsFeignClient;

    @Autowired
    private GifsService gifsService;

    @Test
    public void getGifUrlPerRelationExchangeRates_onValidRequest_ForRichGif() throws InvalidRequestArgument {
        BigDecimal currentRate = new BigDecimal("45.22222222222222");
        BigDecimal yesterdayRate = new BigDecimal("45.22222222222221");
        Mockito.when(exchangeRatesService.getCurrentExchangeRates("RUB")).thenReturn(currentRate);
        Mockito.when(exchangeRatesService.getYesterdayExchangeRates("RUB")).thenReturn(yesterdayRate);
        String richGifUrl = "https://media1.giphy.com/media/lptjRBxFKCJmFoibP3/giphy.gif?cid=14b0846dac67258c1da91229bbd1eda5e9eeecba9af44111&rid=giphy.gif&ct=g";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode original = mapper.createObjectNode();
        original.put("url", richGifUrl);
        ObjectNode images = mapper.createObjectNode();
        images.set("original", original);
        ObjectNode data = mapper.createObjectNode();
        data.set("images", images);
        ObjectNode responseJson = mapper.createObjectNode();
        responseJson.set("data", data);
        Mockito.when(gifsFeignClient.getRandomRichGif()).thenReturn(responseJson);
        Assertions.assertEquals(richGifUrl, gifsService.getGifUrlPerRelationExchangeRates("RUB").getUrl());
        Mockito.verify(gifsFeignClient, Mockito.times(0)).getRandomBrokeGif();
        Mockito.verify(gifsFeignClient, Mockito.times(1)).getRandomRichGif();
    }

    @Test
    public void getGifUrlPerRelationExchangeRates_onValidRequest_ForBrokeGif() throws InvalidRequestArgument {
        BigDecimal currentRate = new BigDecimal("45.22222222222221");
        BigDecimal yesterdayRate = new BigDecimal("45.22222222222222");
        Mockito.when(exchangeRatesService.getCurrentExchangeRates("RUB")).thenReturn(currentRate);
        Mockito.when(exchangeRatesService.getYesterdayExchangeRates("RUB")).thenReturn(yesterdayRate);
        String brokeGif = "https://media0.giphy.com/media/3JSGmRNDBgv7dOt3VD/giphy.gif?cid=14b0846d0a11523e3943713d51be80b921107600dd74f22e&rid=giphy.gif&ct=g";
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode original = mapper.createObjectNode();
        original.put("url", brokeGif);
        ObjectNode images = mapper.createObjectNode();
        images.set("original", original);
        ObjectNode data = mapper.createObjectNode();
        data.set("images", images);
        ObjectNode responseJson = mapper.createObjectNode();
        responseJson.set("data", data);
        Mockito.when(gifsFeignClient.getRandomBrokeGif()).thenReturn(responseJson);
        Assertions.assertEquals(brokeGif, gifsService.getGifUrlPerRelationExchangeRates("RUB").getUrl());
        Mockito.verify(gifsFeignClient, Mockito.times(1)).getRandomBrokeGif();
        Mockito.verify(gifsFeignClient, Mockito.times(0)).getRandomRichGif();
    }

    @Test
    public void getGifUrlPerRelationExchangeRates_withInvalidCurrencyCode() throws InvalidRequestArgument {
        Mockito.when(exchangeRatesService.getCurrentExchangeRates("QWE")).thenThrow(InvalidRequestArgument.class);
        Mockito.when(exchangeRatesService.getYesterdayExchangeRates("QWE")).thenThrow(InvalidRequestArgument.class);
        Assertions.assertThrows(InvalidRequestArgument.class, ()-> gifsService.getGifUrlPerRelationExchangeRates("QWE"));
    }
}
