package com.halyk.study.salecard.mappers;

import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public static Response mapToCardResponse(Product product) {
        Response response = new Response();

        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setProductDescription(product.getDescription());
        response.setIconEndpoint(product.getIconPath().isEmpty() ? "" :
                "http://localhost:8080/api/products/get/icon/" + product.getId());
        response.setProductColor(product.getColor());
        response.setOrderNumber(product.getNumber());

        return response;
    }
}
