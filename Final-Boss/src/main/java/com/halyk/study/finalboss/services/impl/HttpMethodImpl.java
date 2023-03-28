package com.halyk.study.finalboss.services.impl;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.dtos.enums.Status;
import com.halyk.study.finalboss.entities.HttpMethod;
import com.halyk.study.finalboss.entities.Url;
import com.halyk.study.finalboss.repositories.HttpMethodRepository;
import com.halyk.study.finalboss.repositories.UrlRepository;
import com.halyk.study.finalboss.services.HttpMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HttpMethodImpl implements HttpMethodService {

    private final HttpMethodRepository httpMethodRepository;

    private final UrlRepository urlRepository;


    @Override
    public Response getHttpMethods() {
        return Response.builder()
                .message("Http Methods returned.")
                .status(Status.SUCCESS)
                .data(
                        httpMethodRepository.findAll().stream()
                                .map(m -> Response.builder()
                                        .httpMethodName(m.getName())
                                        .build())
                )
                .build();
    }

    @Override
    public Response createHttpMethod(Request request) {
        request.setHttpMethodName(request.getHttpMethodName().trim().toUpperCase());

        Optional<HttpMethod> optional = httpMethodRepository.findByName(request.getHttpMethodName());

        if(optional.isPresent()) {
            return Response.builder()
                    .status(Status.ERROR)
                    .message("HTTP METHOD with given name already exists.")
                    .build();
        } else if(request.getHttpMethodName().isBlank()) {
            return Response.builder()
                    .status(Status.ERROR)
                    .message("HTTP METHOD name was not found in Request.")
                    .build();
        }

        HttpMethod hm = HttpMethod.builder()
                .name(request.getHttpMethodName())
                .build();

        httpMethodRepository.save(hm);

        return Response.builder()
                .message("Http Method was saved.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    @Override
    public Response updateHttpMethod(Request request) {
        request.setHttpMethodName(request.getHttpMethodName().trim().toUpperCase());

        Optional<HttpMethod> optional = httpMethodRepository.findByName(request.getHttpMethodName());

        if(optional.isEmpty()) {
            return Response.builder()
                    .status(Status.ERROR)
                    .message("HTTP METHOD was not found.")
                    .build();
        } else if(request.getNewHttpMethodName().isBlank()) {
            return Response.builder()
                    .status(Status.ERROR)
                    .message("HTTP METHOD new name was not found in Request.")
                    .build();
        }

        optional.get().setName(request.getNewHttpMethodName());

        httpMethodRepository.save(optional.get());

        return Response.builder()
                .message("Http Method was updated.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    @Override
    public Response deleteHttpMethod(Request request) {
        request.setHttpMethodName(request.getHttpMethodName().trim().toUpperCase());

        Optional<HttpMethod> optional = httpMethodRepository.findByName(request.getHttpMethodName());

        if(optional.isEmpty()) {
            return Response.builder()
                    .status(Status.ERROR)
                    .message("HTTP METHOD was not found.")
                    .build();
        }

        HttpMethod unknown = getUnknownHttpMethod(httpMethodRepository);

        for(Url url: optional.get().getUrls()) {
            url.setHttpMethod(unknown);

            urlRepository.save(url);
        }

        httpMethodRepository.delete(optional.get());

        return Response.builder()
                .message("Http Method was deleted.")
                .status(Status.SUCCESS)
                .data(true)
                .build();
    }

    static HttpMethod getUnknownHttpMethod(HttpMethodRepository httpMethodRepository) {
        HttpMethod setHttpMethod;

        Optional<HttpMethod> unknownOptional = httpMethodRepository.findByName("UNKNOWN");

        if(unknownOptional.isEmpty()) {
            HttpMethod addUnknown = HttpMethod.builder()
                    .name("UNKNOWN")
                    .build();

            httpMethodRepository.save(addUnknown);

            setHttpMethod = addUnknown;
        } else {
            setHttpMethod = unknownOptional.get();
        }
        return setHttpMethod;
    }
}
