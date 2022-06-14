package com.koshelev.dto;

import lombok.Data;

@Data
public class GifUrlResponse {
    private String url;

    public GifUrlResponse() {
    }

    public GifUrlResponse(String url) {
        this.url = url;
    }
}
