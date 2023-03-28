package com.halyk.study.wsdl.service;

import com.halyk.study.wsdl.dto.RestResponse;
import com.halyk.study.wsdl.dto.response.NumberToWordsResponse;

public interface EnvelopeService {

    RestResponse getNumberToWordsResponse(Integer num);
}
