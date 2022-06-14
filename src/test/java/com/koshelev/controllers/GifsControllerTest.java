package com.koshelev.controllers;

import com.koshelev.dto.GifUrlResponse;
import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.services.GifsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GifsControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private GifsService gifsService;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getGifUrlTest() throws Exception {
        GifUrlResponse gifUrl = new GifUrlResponse("www.giphy.com/gifs/random/1");
        Mockito.when(gifsService.getGifUrlPerRelationExchangeRates("rub")).thenReturn(gifUrl);
        this.mvc.perform(get("/api/v1/gifs?code=rub")).andDo(print())
                .andExpect(view().name("gifs-page"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("gifUrl", "www.giphy.com/gifs/random/1"));
    }

    @Test
    public void getGifUrlTest_withInvalidCurrencyCode() throws Exception {
        Mockito.when(gifsService.getGifUrlPerRelationExchangeRates("qwe")).thenThrow(InvalidRequestArgument.class);
        this.mvc.perform(get("/api/v1/gifs?code=qwe")).andDo(print())
                .andExpect(status().is4xxClientError());
    }
}
