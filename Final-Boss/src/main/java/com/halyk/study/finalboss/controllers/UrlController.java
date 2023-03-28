package com.halyk.study.finalboss.controllers;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.Url;
import com.halyk.study.finalboss.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;


    @PostMapping("/create-api-service")
    public ResponseEntity<Response> createApiService(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.createApiService(request));
    }

    @PostMapping("/update-api-service")
    public ResponseEntity<Response> updateApiService(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.updateApiService(request));
    }

    @PostMapping("/delete-api-service")
    public ResponseEntity<Response> deleteApiService(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.deleteApiService(request));
    }

    @PostMapping("/get-urls-by-api-services")
    public ResponseEntity<Map<ApiService, List<Url>>> findByUrlsApiService() {
        return ResponseEntity.ok(urlService.findByUrlsApiService());
    }

    @PostMapping("/create-url")
    public ResponseEntity<Response> createUrl(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.createUrl(request));
    }

    @PostMapping("/update-url")
    public ResponseEntity<Response> updateUrl(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.updateUrl(request));
    }

    @PostMapping("/delete-url")
    public ResponseEntity<Response> deleteUrl(@RequestBody Request request) {
        return ResponseEntity.ok(urlService.deleteUrl(request));
    }
}
