package com.halyk.study.wsdl.controller;

import com.halyk.study.wsdl.dto.Response;
import com.halyk.study.wsdl.dto.RestResponse;
import com.halyk.study.wsdl.service.EnvelopeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequiredArgsConstructor
public class SoapController {

    private final EnvelopeService envelopeService;

    private final WebClient webClient;

    @GetMapping("/a")
    public Response getResponse() {
        try {
            Response response = webClient.method(HttpMethod.GET)
                    .uri("localhost:9090/api/departments/get/all")
                    .bodyValue(Response.builder().departmentName("IT").build())
                    .retrieve()
                    .bodyToMono(Response.class)
                    .block();

            if (response != null) {
                System.out.println(response.getDepartmentName());
            }

            return response;
        } catch (Exception e) {
            System.out.println("EXCEPTION");
        }
        return null;
    }

    @GetMapping(value = "/get-num-response/{num}")
    public ResponseEntity<RestResponse> getNumberToWordsResponse(@PathVariable Integer num) {
        return ResponseEntity.ok(envelopeService.getNumberToWordsResponse(num));
    }
}
