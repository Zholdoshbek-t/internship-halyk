package com.halyk.study.finalboss.services;

import com.halyk.study.finalboss.dtos.Request;
import com.halyk.study.finalboss.dtos.Response;

public interface HttpMethodService {

    Response getHttpMethods();

    Response createHttpMethod(Request request);

    Response updateHttpMethod(Request request);

    Response deleteHttpMethod(Request request);
}