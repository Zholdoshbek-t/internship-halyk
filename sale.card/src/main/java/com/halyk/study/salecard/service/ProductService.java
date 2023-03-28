package com.halyk.study.salecard.service;

import com.halyk.study.salecard.dto.Request;
import com.halyk.study.salecard.dto.Response;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface ProductService {

    Response getProducts();

    Response getProductsSortedByName();

    Response getProductsSortedByDate();

    BufferedImage getIcon(Long cardId);

    Response createProductDetails(Request request);

    Response updateProductDetails(Request request);

    Response addProductIcon(Long cardId, MultipartFile multipartFile);

    Response replaceProductIcon(Long cardId, MultipartFile multipartFile);

    Response deleteProductIcon(Request request);

    Response deleteProduct(Request request);

    Response arrangeProducts(Request request);
}
