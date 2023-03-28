package com.halyk.study.salecard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.halyk.study.salecard.dto.enums.Status;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JacksonXmlRootElement(localName = "response")
public class Response {

    @JacksonXmlProperty(localName = "message")
    private String message;

    @JacksonXmlProperty(localName = "status")
    private Status status;

    @JacksonXmlProperty(localName = "single_data")
    private Object data;

    @JacksonXmlProperty(localName = "data_list")
    private Objects dataList;

    @JacksonXmlProperty(localName = "product_id")
    private Long productId;

    @JacksonXmlProperty(localName = "product_name")
    private String productName;

    @JacksonXmlProperty(localName = "product_description")
    private String productDescription;

    @JacksonXmlProperty(localName = "product_color")
    private String productColor;

    @JacksonXmlProperty(localName = "product_icon_path")
    private String productIconPath;

    @JacksonXmlProperty(localName = "icon_endpoint")
    private String iconEndpoint;

    @JacksonXmlProperty(localName = "order_number")
    private Integer orderNumber;

    @JacksonXmlProperty(localName = "user_card_id")
    private Long userCardId;

    @JacksonXmlProperty(localName = "user_card_barcode_path")
    private String userCardBarcodePath;

    public String toProductString() {
        return
                "{productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productColor='" + productColor + '\'' +
                ", iconEndpoint='" + iconEndpoint + '\'' +
                ", orderNumber=" + orderNumber + "}";
    }

    public String toProductResponseString() {
        Response product = (Response) data;
        return "Response{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + product.toProductString();
    }

    @Override
    public String toString() {
        return "Response{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                ", dataList=" + dataList +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productDescription='" + productDescription + '\'' +
                ", productColor='" + productColor + '\'' +
                ", productIconPath='" + productIconPath + '\'' +
                ", iconEndpoint='" + iconEndpoint + '\'' +
                ", orderNumber=" + orderNumber +
                ", userCardId=" + userCardId +
                ", userCardBarcodePath='" + userCardBarcodePath + '\'' +
                '}';
    }
}
