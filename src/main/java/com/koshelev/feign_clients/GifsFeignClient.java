package com.koshelev.feign_clients;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "${feign.gifs.name}" , url = "${feign.gifs.url}")
public interface GifsFeignClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v1/gifs/random?api_key=${feign.gifs.api_key}&tag=rich")
    JsonNode getRandomRichGif();

    @RequestMapping(method = RequestMethod.GET, value = "/v1/gifs/random?api_key=${feign.gifs.api_key}&tag=broke")
    JsonNode getRandomBrokeGif();
}
