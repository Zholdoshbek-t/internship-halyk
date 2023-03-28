package com.halyk.study.wsdl.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NumberToWordsRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns")
    private final String xmlns = "http://www.dataaccess.com/webservicesserver/";

    @JacksonXmlProperty(localName = "ubiNum")
    private Integer ubiNum;
}
