package com.halyk.study.finalboss.services.impl;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.dtos.enums.Status;
import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.HttpMethod;
import com.halyk.study.finalboss.entities.Url;
import com.halyk.study.finalboss.mappers.ResponseMapper;
import com.halyk.study.finalboss.repositories.ApiServiceRepository;
import com.halyk.study.finalboss.repositories.HttpMethodRepository;
import com.halyk.study.finalboss.repositories.UrlRepository;
import com.halyk.study.finalboss.services.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlImpl implements UrlService {

    private final ApiServiceRepository apiServiceRepository;

    private final UrlRepository urlRepository;

    private final HttpMethodRepository httpMethodRepository;


    @Override
    public Response createApiService(Request request) {
        request.setApiServiceName(request.getApiServiceName().trim().toUpperCase());

        Optional<ApiService> apiService = apiServiceRepository.findByName(request.getApiServiceName());

        if (apiService.isPresent()) {
            return ResponseMapper
                    .createResponse("Api Service with the given name already exists.",
                            Status.ERROR, false);
        } else if (request.getApiServiceName().isEmpty()) {
            return ResponseMapper
                    .createResponse("Api Service name is empty.",
                            Status.ERROR, false);
        }

        ApiService entity = ApiService.builder()
                .name(request.getApiServiceName())
                .build();

        apiServiceRepository.save(entity);

        return ResponseMapper
                .createResponse("Api Service was successfully created.",
                        Status.SUCCESS, true);
    }

    @Override
    public Response updateApiService(Request request) {
        request.setApiServiceName(request.getApiServiceName().trim().toUpperCase());

        Optional<ApiService> apiService = apiServiceRepository.findById(request.getApiServiceId());

        if (apiService.isEmpty()) {
            ApiService entity = ApiService.builder()
                    .name(request.getApiServiceName())
                    .build();

            apiServiceRepository.save(entity);

            return ResponseMapper
                    .createResponse("Api Service with the given ID was not found.\n" +
                                    "That is why the new service with the given name was created.",
                            Status.SUCCESS, true);
        } else if (request.getApiServiceName().isEmpty()) {
            return ResponseMapper
                    .createResponse("Api Service name is empty.",
                            Status.ERROR, false);
        }

        apiService.get().setName(request.getApiServiceName().toUpperCase());

        apiServiceRepository.save(apiService.get());

        return ResponseMapper
                .createResponse("Api Service was successfully updated.",
                        Status.SUCCESS, true);
    }

    @Override
    public Response deleteApiService(Request request) {
        Optional<ApiService> apiService = apiServiceRepository.findById(request.getApiServiceId());

        if (apiService.isEmpty()) {
            return ResponseMapper
                    .createResponse("Api Service with the given name was not found",
                            Status.ERROR, false);
        }

        ApiService setService;

        Optional<ApiService> unknown = apiServiceRepository.findByName("Unknown Service");

        if (unknown.isEmpty()) {
            ApiService createUnknown = ApiService.builder()
                    .name("Unknown Service")
                    .build();

            apiServiceRepository.save(createUnknown);

            setService = createUnknown;
        } else {
            setService = unknown.get();
        }

        for (Url url : apiService.get().getUrls()) {
            url.setApiService(setService);

            urlRepository.save(url);
        }

        apiServiceRepository.delete(apiService.get());

        return ResponseMapper
                .createResponse("Api Service was successfully deleted.",
                        Status.SUCCESS, true);
    }

    @Override
    public Map<ApiService, List<Url>> findByUrlsApiService() {
        Map<ApiService, List<Url>> apiServiceUrls = new HashMap<>();

        for (ApiService apiServiceId : apiServiceRepository.findAllApiServiceIds()) {
            apiServiceUrls.put(apiServiceId, urlRepository.findAllByApiServiceId(apiServiceId.getId()));
        }

        return apiServiceUrls;
    }

    @Override
    public Response createUrl(Request request) {
        request.setApiServiceName(request.getApiServiceName().trim().toUpperCase());
        request.setHttpMethodName(request.getHttpMethodName().trim().toUpperCase());
        request.setAddress(request.getAddress().trim());

        Optional<Url> url = urlRepository.findByAddress(request.getAddress());

        Optional<ApiService> apiService = apiServiceRepository.findByName(request.getApiServiceName().toUpperCase());

        if (url.isPresent()) {
            return ResponseMapper
                    .createResponse("Url with the given address already exists.",
                            Status.ERROR, false);
        }

        ApiService entity;

        if (apiService.isEmpty() && !request.getApiServiceName().isEmpty()) {
            ApiService newService = ApiService.builder()
                    .name(request.getApiServiceName())
                    .build();

            apiServiceRepository.save(newService);

            entity = newService;
        } else {
            entity = apiService.orElseGet(this::getUnknown);
        }

        Optional<HttpMethod> optionalHttpMethod = httpMethodRepository.findByName(request.getHttpMethodName());

        HttpMethod setHttpMethod = optionalHttpMethod
                .orElseGet(() -> HttpMethodImpl.getUnknownHttpMethod(httpMethodRepository));

        Url urlEntity = Url.builder()
                .address(request.getAddress())
                .apiService(entity)
                .httpMethod(setHttpMethod)
                .build();

        urlRepository.save(urlEntity);

        return ResponseMapper
                .createResponse("Url was successfully created.",
                        Status.SUCCESS, true);
    }

    @Override
    public Response updateUrl(Request request) {
        request.setAddress(request.getAddress().trim());
        request.setApiServiceName(request.getApiServiceName().trim().toUpperCase());
        request.setHttpMethodName(request.getHttpMethodName().trim().toUpperCase());

        Optional<Url> url = urlRepository.findById(request.getUrlId());

        Optional<ApiService> apiService = apiServiceRepository.findByName(request.getApiServiceName());

        if (url.isEmpty()) {
            return ResponseMapper
                    .createResponse("Url with the given address was not found.",
                            Status.ERROR, false);
        } else if(request.getAddress().isEmpty()) {
            return ResponseMapper
                    .createResponse("Address is empty.",
                            Status.ERROR, false);
        }

        url.get().setAddress(request.getAddress());

        if (apiService.isPresent()) {
            url.get().setApiService(apiService.get());
        } else {
            if (!request.getApiServiceName().isEmpty()) {
                ApiService newService = ApiService.builder()
                        .name(request.getApiServiceName())
                        .build();

                apiServiceRepository.save(newService);
            } else {
                Optional<ApiService> unknown = apiServiceRepository.findByName("Unknown Service");

                if (unknown.isEmpty()) {
                    ApiService createUnknown = ApiService.builder()
                            .name("Unknown Service")
                            .build();

                    apiServiceRepository.save(createUnknown);

                    url.get().setApiService(createUnknown);
                } else {
                    url.get().setApiService(unknown.get());
                }
            }
        }

        if(!request.getNewHttpMethodName().isBlank() &&
                !request.getNewHttpMethodName().equals(url.get().getHttpMethod().getName())) {
            Optional<HttpMethod> optionalHttpMethod =
                    httpMethodRepository.findByName(request.getNewHttpMethodName());

            if(optionalHttpMethod.isEmpty()) {
                HttpMethod method = HttpMethod.builder()
                        .name(request.getNewHttpMethodName())
                        .build();

                httpMethodRepository.save(method);

                url.get().setHttpMethod(method);
            } else {
                url.get().setHttpMethod(optionalHttpMethod.get());
            }
        }

        urlRepository.save(url.get());

        return ResponseMapper
                .createResponse("Url was successfully updated.",
                        Status.SUCCESS, true);
    }

    @Override
    public Response deleteUrl(Request request) {
        Optional<Url> url = urlRepository.findById(request.getUrlId());

        if (url.isEmpty()) {
            return ResponseMapper
                    .createResponse("Url with the given address was not found.",
                            Status.ERROR, false);
        }

        urlRepository.delete(url.get());

        return ResponseMapper
                .createResponse("Url was successfully deleted.",
                        Status.SUCCESS, true);
    }

    private ApiService getUnknown() {
        Optional<ApiService> unknown = apiServiceRepository.findByName("Unknown Service");

        if (unknown.isEmpty()) {
            ApiService createUnknown = ApiService.builder()
                    .name("Unknown Service")
                    .build();

            apiServiceRepository.save(createUnknown);

            return createUnknown;
        }
        return unknown.get();
    }
}
