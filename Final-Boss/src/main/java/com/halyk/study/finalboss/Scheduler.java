package com.halyk.study.finalboss;

import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.Url;
import com.halyk.study.finalboss.repositories.ApiServiceRepository;
import com.halyk.study.finalboss.services.ErrorMessageService;
import com.halyk.study.finalboss.services.UrlService;
import com.halyk.study.finalboss.utils.TelegramBot;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final UrlService urlService;

    private final ApiServiceRepository apiServiceRepository;

    private final ErrorMessageService errorMessageService;

    private final TelegramBot telegramBot;


    @Scheduled(fixedDelay = 10000)
    public void run() {
        List<ApiService> apiServices = apiServiceRepository.findAllByOrderByName();
        for (ApiService apiService : apiServices) {
            for (Url url : apiService.getUrls()) {
                if(!url.getHttpMethod().getName().equals("UNKNOWN")) {
                    int statusCode = getRequestCode(url.getHttpMethod().getName().toUpperCase(), url.getAddress());

                    if (statusCode == Integer.MIN_VALUE) {
                        errorMessageService.saveErrorMessage(url, statusCode);
                        telegramBot.sendExceptionMessage(apiService, url.getAddress());
                        LOGGER.info(String.format("URI: %s could not be reached.", url.getAddress()));
                    } else if (statusCode < 200 || (statusCode >= 300 && statusCode < 400)
                            || (statusCode >= 500)) {
                        errorMessageService.saveErrorMessage(url, statusCode);
                        telegramBot.sendExceptionMessage(apiService, url.getAddress(), statusCode);
                        LOGGER.info(String.format("URI: %s is down.", url.getAddress()));
                    } else {
                        LOGGER.info(String.format("URI: %s works properly.", url.getAddress()));
                    }
                }
            }
        }
    }

    private int getRequestCode(String httpMethod, String address) {
        HttpUriRequest request = null;
        switch (httpMethod) {
            case "GET":
                request = new HttpGet(address);
                break;
            case "POST":
                request = new HttpPost(address);
                break;
            case "PUT":
                request = new HttpPut(address);
                break;
            case "DELETE":
                request = new HttpDelete(address);
                break;
            case "PATCH":
                request = new HttpPatch(address);
                break;
            case "OPTIONS":
                request = new HttpOptions(address);
                break;
            case "HEAD":
                request = new HttpHead(address);
                break;
            case "TRACE":
                request = new HttpTrace(address);
                break;
        }

        try {
            HttpResponse response = HttpClientBuilder.create()
                    .build()
                    .execute(request);

            return response.getStatusLine().getStatusCode();
        } catch (Exception e) {
            return Integer.MIN_VALUE;
        }
    }
}
