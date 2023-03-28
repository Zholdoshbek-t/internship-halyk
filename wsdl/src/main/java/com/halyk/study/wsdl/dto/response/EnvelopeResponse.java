package com.halyk.study.wsdl.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "Envelope")
public class EnvelopeResponse {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:soap")
    private final String xmlns = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(localName = "Body")
    private BodyResponse body;
}
