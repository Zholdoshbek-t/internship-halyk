package com.halyk.study.salecard.controller;

import com.halyk.study.salecard.dto.Request;
import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.service.UserCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.image.BufferedImage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users-cards")
@Tag(name = "Users Cards controller", description = "User Card endpoints (related to client side)")
public class UserCardController {

    private final UserCardService userCardService;


    @GetMapping(value = "/get/all", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Get user cards")
    public ResponseEntity<Response> getUserCards(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.getUserCards(request));
    }

    @GetMapping(value = "/get/sorted-by-name", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Get user cards sorted by name")
    public ResponseEntity<Response> getUserCardsSortedByName(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.getUserCardsSortedByName(request));
    }

    @GetMapping(value = "/get/sorted-by-date", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Get user cards sorted by date")
    public ResponseEntity<Response> getUserCardsSortedByDate(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.getUserCardsSortedByDate(request));
    }

    @GetMapping(value = "/get/barcode", produces = "image/png")
    @Operation(summary = "Get user card barcode")
    public BufferedImage getBarcode(@RequestBody Request request) {
        return userCardService.getBarcode(request);
    }

    @PostMapping(value = "/create", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Create user card")
    public ResponseEntity<Response> createUserCard(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.createUserCard(request));
    }

    @PostMapping(value = "/update", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Update user card")
    public ResponseEntity<Response> updateUserCard(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.updateUserCard(request));
    }

    @PostMapping(value = "/delete", produces = "image/png", consumes = "application/xml")
    @Operation(summary = "Delete user card")
    public ResponseEntity<Response> deleteUserCard(@RequestBody Request request) {
        return ResponseEntity.ok(userCardService.deleteUserCard(request));
    }
}
