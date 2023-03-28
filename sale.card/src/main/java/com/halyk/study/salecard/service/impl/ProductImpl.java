package com.halyk.study.salecard.service.impl;

import com.halyk.study.salecard.dto.*;
import com.halyk.study.salecard.dto.Objects;
import com.halyk.study.salecard.dto.enums.Status;
import com.halyk.study.salecard.entity.Product;
import com.halyk.study.salecard.mappers.ProductMapper;
import com.halyk.study.salecard.mappers.ResponseMapper;
import com.halyk.study.salecard.repository.ProductRepository;
import com.halyk.study.salecard.service.ProductService;
import com.halyk.study.salecard.service.ImageService;
import com.halyk.study.salecard.utils.Translator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ImageService imageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductImpl.class);


    @Override
    public Response getProducts() {
        String uuid = UUID.randomUUID().toString();

        LOGGER.info("REQUEST --> UUID: {} | STATUS: {} | Get Products | BODY: {}",
                uuid, Status.SUCCESS, "getProducts()");

        List<Object> products = productRepository.findAllByOrderByNumberAsc()
                .stream()
                .map(ProductMapper::mapToCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(products);

        Response responseExample =
                ResponseMapper.response(Translator.toLocale("products_returned"),
                        Status.SUCCESS, products.get(0));

        LOGGER.info("RESPONSE -> UUID: {} | STATUS: {} | Get Products | BODY: {}",
                uuid, Status.SUCCESS, responseExample.toProductResponseString());

        return ResponseMapper.response(Translator.toLocale("products_returned"), Status.SUCCESS, objects);
    }

    @Override
    public Response getProductsSortedByName() {
        String uuid = UUID.randomUUID().toString();

        LOGGER.info("REQUEST --> UUID: {} | STATUS: {} | Get Products sorted by name | BODY: {}",
                uuid, Status.SUCCESS, "getProductsSortedByName()");

        List<Object> products = productRepository.findAllByOrderByName()
                .stream()
                .map(ProductMapper::mapToCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(products);

        Response responseExample =
                ResponseMapper.response(Translator.toLocale("products_returned"),
                        Status.SUCCESS, products.get(0));

        LOGGER.info("RESPONSE -> UUID: {} | STATUS: {} | Get Products sorted by name | BODY: {}",
                uuid, Status.SUCCESS, responseExample.toProductResponseString());

        return ResponseMapper.response(Translator.toLocale("products_returned"), Status.SUCCESS, objects);
    }

    @Override
    public Response getProductsSortedByDate() {
        String uuid = UUID.randomUUID().toString();

        LOGGER.info("REQUEST --> UUID: {} | STATUS: {} | Get Products sorted by date | BODY: {}",
                uuid, Status.SUCCESS, "getProductsSortedByDate()");

        List<Object> products = productRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ProductMapper::mapToCardResponse)
                .collect(Collectors.toList());

        Objects objects = new Objects(products);

        Response responseExample =
                ResponseMapper.response(Translator.toLocale("products_returned"),
                        Status.SUCCESS, products.get(0));

        LOGGER.info("RESPONSE -> UUID: {} | STATUS: {} | Get Products sorted by date | BODY: {}",
                uuid, Status.SUCCESS, responseExample.toProductResponseString());

        return ResponseMapper.response(Translator.toLocale("products_returned"), Status.SUCCESS, objects);
    }

    @Override
    public BufferedImage getIcon(Long cardId) {
        Optional<Product> product = productRepository.findById(cardId);

        if (product.isPresent()) {
            try {
                return ImageIO.read(
                        new File(product.get().getIconPath())
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    @Override
    public Response createProductDetails(Request request) {
        Optional<Product> optionalProduct = productRepository.findByName(request.getProductName());

        if (optionalProduct.isPresent()) {
            return ResponseMapper.response(Translator.toLocale("product_already_exists"), Status.ERROR, false);
        }

        if (request.getProductName() == null || request.getProductName().isEmpty()
                || request.getProductDescription() == null || request.getProductDescription().isEmpty()) {
            return ResponseMapper.response(Translator.toLocale("insufficient_product"), Status.ERROR, false);
        }

        int count = (int) productRepository.count();

        Product product = Product.builder()
                .name(request.getProductName())
                .description(request.getProductDescription())
                .iconPath("")
                .color(request.getProductColor().isEmpty() ? "BLACK" : request.getProductColor())
                .number(count + 1)
                .build();

        productRepository.save(product);

        return ResponseMapper.response(Translator.toLocale("product_added"), Status.SUCCESS, true);
    }

    @Override
    public Response updateProductDetails(Request request) {
        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());

        if (optionalProduct.isPresent()) {
            if (request.getProductName() == null || request.getProductName().isEmpty()
                    || request.getProductDescription().isEmpty() || request.getProductId() == null) {
                return ResponseMapper.response(Translator.toLocale("insufficient_product"), Status.ERROR, false);
            }

            Product product = optionalProduct.get();

            product.setName(request.getProductName());
            product.setDescription(request.getProductDescription());
            product.setColor(request.getProductColor());

            productRepository.save(product);

            return ResponseMapper.response(Translator.toLocale("product_updated"), Status.SUCCESS, true);
        }

        return ResponseMapper.response(Translator.toLocale("product_not_found"), Status.ERROR, false);
    }

    @Override
    public Response addProductIcon(Long cardId, MultipartFile multipartFile) {
        Optional<Product> optionalProduct = productRepository.findById(cardId);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            Response response = imageService.addCardIcon(multipartFile, product.getName().toLowerCase());

            if (response.getProductIconPath() == null || response.getProductIconPath().isEmpty()) {
                return response;
            }

            product.setIconPath(response.getProductIconPath());

            productRepository.save(product);

            return ResponseMapper.response(Translator.toLocale("product_icon_added"), Status.SUCCESS, true);
        }

        return ResponseMapper.response(Translator.toLocale("product_not_found"), Status.ERROR, false);
    }

    @Override
    public Response replaceProductIcon(Long cardId, MultipartFile multipartFile) {
        Optional<Product> optionalProduct = productRepository.findById(cardId);

        if (optionalProduct.isPresent()) {
            Response response = imageService.replaceCardIcon(multipartFile, optionalProduct.get());

            if (response.getProductIconPath() == null || response.getProductIconPath().isEmpty()) {
                return response;
            }

            return ResponseMapper.response(Translator.toLocale("product_icon_updated"), Status.SUCCESS, true);
        }

        return ResponseMapper.response(Translator.toLocale("product_not_found"), Status.ERROR, false);
    }

    @Override
    public Response deleteProductIcon(Request request) {
        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());

        if (optionalProduct.isPresent()) {
            Response response = imageService.deleteCardIcon(optionalProduct.get().getIconPath());

            if (response.getStatus() == Status.SUCCESS) {
                optionalProduct.get().setIconPath("");

                productRepository.save(optionalProduct.get());
            }

            return response;
        }

        return ResponseMapper.response(Translator.toLocale("product_not_found"), Status.ERROR, false);
    }

    @Override
    public Response deleteProduct(Request request) {
        Optional<Product> optionalProduct = productRepository.findById(request.getProductId());

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            imageService.deleteCardIcon(product.getIconPath());

            List<Product> products = productRepository.findAllByNumberIsGreaterThan(product.getNumber());

            for (Product entity : products) {
                entity.setNumber(entity.getNumber() - 1);

                productRepository.save(entity);
            }

            productRepository.delete(product);

            return ResponseMapper.response(Translator.toLocale("product_deleted"), Status.SUCCESS, true);
        }

        return ResponseMapper.response(Translator.toLocale("product_not_found"), Status.ERROR, false);
    }

    @Override
    public Response arrangeProducts(Request request) {
        if (request.getOrderNumberProductIdMap().isEmpty()) {
            if (request.getProductId() == null) {
                return ResponseMapper.response(Translator.toLocale("products_no_order"), Status.ERROR, false);
            }
        }

        Map<Integer, Long> map = request.getOrderNumberProductIdMap();

        for (Integer orderNumber : map.keySet()) {
            Optional<Product> optionalProduct = productRepository.findById(map.get(orderNumber));

            if (optionalProduct.isPresent()) {
                optionalProduct.get().setNumber(orderNumber);

                productRepository.save(optionalProduct.get());
            }
        }

        return ResponseMapper.response(Translator.toLocale("products_arranged"), Status.SUCCESS, true);
    }
}
