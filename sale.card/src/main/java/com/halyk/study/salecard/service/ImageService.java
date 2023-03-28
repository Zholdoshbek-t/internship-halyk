package com.halyk.study.salecard.service;

import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.entity.Product;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    Response addCardIcon(MultipartFile multipartFile, String cardName);

    Response replaceCardIcon(MultipartFile multipartFile, Product product);

    Response deleteCardIcon(String iconPath);
}
