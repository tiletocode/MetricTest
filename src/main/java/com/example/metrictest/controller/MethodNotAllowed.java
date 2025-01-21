package com.example.metrictest.controller;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/methodnotallowed")
public class MethodNotAllowed {

    @GetMapping
    public String triggerException() throws HttpRequestMethodNotSupportedException {
        throw new HttpRequestMethodNotSupportedException("POST", "This endpoint only supports GET requests.");
    }
}
