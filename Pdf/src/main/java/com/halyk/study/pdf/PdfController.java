package com.halyk.study.pdf;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class PdfController {

    private final PdfGenerator pdfGenerator;

    @GetMapping(value = "/{orderId}", produces = "application/pdf")
    public ResponseEntity<Resource> word(@PathVariable String orderId) {
        return pdfGenerator.generatePdfFromHtml(orderId);
    }
}
