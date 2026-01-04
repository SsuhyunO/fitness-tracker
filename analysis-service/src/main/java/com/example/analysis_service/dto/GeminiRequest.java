package com.example.analysis_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Collections;
import java.util.List;

@Getter
public class GeminiRequest {
    private List<Content> contents;

    public GeminiRequest(String text) {
        this.contents = Collections.singletonList(new Content(text));
    }

    @Getter
    @AllArgsConstructor
    static class Content {
        private List<Part> parts;
        public Content(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }

    @Getter
    @AllArgsConstructor
    static class Part {
        private String text;
    }
}