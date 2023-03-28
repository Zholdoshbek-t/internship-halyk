package com.halyk.study.wsdl.dto.request;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JacksonXmlRootElement(localName = "soap:Envelope")
public class EnvelopeRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "xmlns:soap")
    private final String xmlns = "http://schemas.xmlsoap.org/soap/envelope/";

    @JacksonXmlProperty(localName = "soap:Body")
    private BodyRequest bodyRequest;
}
