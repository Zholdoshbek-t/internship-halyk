package com.halyk.study.finalboss.controllers;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.services.HttpMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HttpMethodController {

    private final HttpMethodService httpMethodService;


    @GetMapping("/get-http-methods")
    public ResponseEntity<Response> getHttpMethods() {
        return ResponseEntity.ok(httpMethodService.getHttpMethods());
    }

    @PostMapping("/create-http-method")
    public ResponseEntity<Response> createHttpMethod(@RequestBody Request request) {
        return ResponseEntity.ok(httpMethodService.createHttpMethod(request));
    }

    @PostMapping("/update-http-method")
    public ResponseEntity<Response> updateHttpMethod(@RequestBody Request request) {
        return ResponseEntity.ok(httpMethodService.updateHttpMethod(request));
    }

    @PostMapping("/delete-http-method")
    public ResponseEntity<Response> deleteHttpMethod(@RequestBody Request request) {
        return ResponseEntity.ok(httpMethodService.deleteHttpMethod(request));
    }
}
