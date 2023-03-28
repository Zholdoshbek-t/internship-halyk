package com.halyk.study.wsdl.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NumberToWordsResponse {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private final String xmlns = "http://www.dataaccess.com/webservicesserver/";

    @JacksonXmlProperty(localName = "NumberToWordsResult")
    private String numberToWordsResult;
}
