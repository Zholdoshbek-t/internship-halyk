package com.halyk.study.salecard.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "request")
public class Request {

    @JacksonXmlProperty(localName = "login")
    private String login;

    @JacksonXmlProperty(localName = "password")
    private String password;

    @JacksonXmlProperty(localName = "access_token")
    private String accessToken;

    @JacksonXmlProperty(localName = "product_id")
    private Long productId;

    @JacksonXmlProperty(localName = "product_name")
    private String productName;

    @JacksonXmlProperty(localName = "product_description")
    private String productDescription;

    @JacksonXmlProperty(localName = "product_color")
    private String productColor;

    @JacksonXmlProperty(localName = "product_icon_endpoint")
    private String productIconEndpoint;

    @JacksonXmlProperty(localName = "order_number_product_id_map")
    private Map<Integer, Long> orderNumberProductIdMap;

    @JacksonXmlProperty(localName = "user_id")
    private Long userId;

    @JacksonXmlProperty(localName = "barcode")
    private String barcode;

    @JacksonXmlProperty(localName = "user_card_id")
    private Long userCardId;
}
