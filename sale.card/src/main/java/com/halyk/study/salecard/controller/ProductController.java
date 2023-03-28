package com.halyk.study.salecard.controller;

import com.halyk.study.salecard.dto.Request;
import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Tag(name = "Products controller", description = "Products endpoints (related to admin side)")
public class ProductController {

    private final ProductService productService;


    @GetMapping(path = "/get/all", produces = "application/xml")
    @Operation(summary = "Get products")
    public ResponseEntity<Response> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping(value = "/get/sorted-by-name", produces = "application/xml")
    @Operation(summary = "Get products sorted by name")
    public ResponseEntity<Response> getProductsSortedByName() {
        return ResponseEntity.ok(productService.getProductsSortedByName());
    }

    @GetMapping(value = "/get/sorted-by-date", produces = "application/xml")
    @Operation(summary = "Get products sorted by date")
    public ResponseEntity<Response> getProductsSortedByDate() {
        return ResponseEntity.ok(productService.getProductsSortedByDate());
    }

    @GetMapping(value = "/get/icon/{card_id}", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Get product icon")
    public BufferedImage getIcon(@PathVariable Long card_id) {
        return productService.getIcon(card_id);
    }

    @PostMapping(value = "/create/details", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Create product")
    public ResponseEntity<Response> createProductDetails(@RequestBody Request request) {
        return ResponseEntity.ok(productService.createProductDetails(request));
    }

    @PostMapping(value = "/update/details", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Update product")
    public ResponseEntity<Response> updateProductDetails(@RequestBody Request request) {
        return ResponseEntity.ok(productService.updateProductDetails(request));
    }

    @PostMapping(value = "/add/icon/{cardId}", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Add product icon")
    public ResponseEntity<Response> addProductIcon(@PathVariable Long cardId,
                                                   @RequestPart MultipartFile icon) {
        return ResponseEntity.ok(productService.addProductIcon(cardId, icon));
    }

    @PostMapping(value = "/update/icon/{cardId}", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Update product icon")
    public ResponseEntity<Response> replaceProductIcon(@PathVariable Long cardId,
                                                       @RequestPart MultipartFile icon) {
        return ResponseEntity.ok(productService.replaceProductIcon(cardId, icon));
    }

    @PostMapping(value = "/delete/icon", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Delete product icon")
    public ResponseEntity<Response> deleteProductIcon(@RequestBody Request request) {
        return ResponseEntity.ok(productService.deleteProductIcon(request));
    }

    @PostMapping(value = "/delete", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Delete product")
    public ResponseEntity<Response> deleteProduct(@RequestBody Request request) {
        return ResponseEntity.ok(productService.deleteProduct(request));
    }

    @PostMapping(value = "/arrange", produces = "application/xml", consumes = "application/xml")
    @Operation(summary = "Arrange products order")
    public ResponseEntity<Response> arrangeProducts(@RequestBody Request request) {
        return ResponseEntity.ok(productService.arrangeProducts(request));
    }
}
