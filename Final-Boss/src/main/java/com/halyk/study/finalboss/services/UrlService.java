package com.halyk.study.finalboss.services;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;
import com.halyk.study.finalboss.entities.ApiService;
import com.halyk.study.finalboss.entities.Url;

import java.util.List;
import java.util.Map;

public interface UrlService {

    Response createApiService(Request request);

    Response updateApiService(Request request);

    Response deleteApiService(Request request);

    Map<ApiService, List<Url>> findByUrlsApiService();

    Response createUrl(Request request);

    Response updateUrl(Request request);

    Response deleteUrl(Request request);
}
