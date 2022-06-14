package com.koshelev.controllers;

import com.koshelev.exceptions.InvalidRequestArgument;
import com.koshelev.services.GifsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/v1/gifs")
@RequiredArgsConstructor
public class GifsController {

    private final GifsService gifsService;

    @GetMapping()
    public String getGifUrl(@RequestParam(name = "code") String currencyCode, Model model) throws InvalidRequestArgument {
        model.addAttribute("gifUrl", gifsService.getGifUrlPerRelationExchangeRates(currencyCode).getUrl());
        return "gifs-page";
    }
}
