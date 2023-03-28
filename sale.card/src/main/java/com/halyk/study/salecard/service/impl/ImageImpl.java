package com.halyk.study.salecard.service.impl;

import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.dto.enums.Status;
import com.halyk.study.salecard.entity.Product;
import com.halyk.study.salecard.mappers.ResponseMapper;
import com.halyk.study.salecard.service.ImageService;
import com.halyk.study.salecard.utils.Translator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageImpl implements ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageImpl.class);


    @Override
    public Response addCardIcon(MultipartFile multipartFile, String cardName) {
        if (multipartFile.isEmpty()) {
            return ResponseMapper.response(Translator.toLocale("file_empty"), Status.ERROR, false);
        } else if(cardName.isEmpty()) {
            return ResponseMapper.response(Translator.toLocale("product_no_name"), Status.ERROR, false);
        }

        String iconsPath = "/home/tilek/Projects/sale.card/src/main/resources/icons/";

        File iconFile = new File(iconsPath + cardName + ".png");

        return getResponse(multipartFile, iconFile);
    }

    @Override
    public Response replaceCardIcon(MultipartFile multipartFile, Product product) {
        if (multipartFile.isEmpty()) {
            return ResponseMapper.response(Translator.toLocale("file_empty"), Status.ERROR, false);
        } else if(product.getIconPath().isEmpty()) {
            return addCardIcon(multipartFile, product.getName());
        }

        File iconFile = new File(product.getIconPath());

        return getResponse(multipartFile, iconFile);
    }

    private Response getResponse(MultipartFile multipartFile, File iconFile) {
        try {
            multipartFile.transferTo(iconFile);

            ImageIO.write(ImageIO.read(iconFile), "png", iconFile);

            return Response.builder()
                    .productIconPath(iconFile.getAbsolutePath())
                    .build();
        } catch (IOException e) {
            return ResponseMapper.response(Translator.toLocale("file_problems"), Status.ERROR, false);
        }
    }

    @Override
    public Response deleteCardIcon(String iconPath) {
        if(iconPath.isEmpty()) {
            return ResponseMapper.response(Translator.toLocale("product_no_icon_path"), Status.ERROR, false);
        }

        File iconFile = new File(iconPath);

        boolean isDeleted = iconFile.delete();

        if(isDeleted) {
            return ResponseMapper.response(Translator.toLocale("product_icon_deleted"), Status.SUCCESS, true);
        }

        return ResponseMapper.response(Translator.toLocale("system_problems_icon_removal"), Status.ERROR, false);
    }

}
