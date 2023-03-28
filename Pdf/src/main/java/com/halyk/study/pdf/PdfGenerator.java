package com.halyk.study.pdf;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class PdfGenerator {

    private final Environment env;

    private final TemplateEngine templateEngine;

    private final ITextRenderer renderer;
    @Qualifier("local")
    @NonNull
    private final DateTimeFormatter localDateFormat;
    @Qualifier("full")
    @NonNull
    private final DateTimeFormatter fullDateFormat;
    @Qualifier("save")
    @NonNull
    private final DateTimeFormatter savePdfDateFormat;


    public ResponseEntity<Resource> generatePdfFromHtml(String orderId) {
        try {
            File dir = new File(env.getProperty("pdf.downloads.path"));
            for (File file : dir.listFiles()) {
                if (file.getName().contains(orderId)) {
                    return ResponseEntity.ok(
                            new InputStreamResource(
                                    new ByteArrayInputStream(Files.readAllBytes(Paths.get(file.getAbsolutePath())))
                            )
                    );
                }
            }

            PdfRequest request = request(orderId);

            request.setTransferDateStr(localDateFormat.format(request.getTransferDate()));
            request.setValueDateStr(localDateFormat.format(request.getValueDate()));
            request.setAppSignatureDateTimeStr(fullDateFormat.format(request.getAppSignatureDateTime()));

            Map<String, Object> data = new HashMap<>();
            data.put("request", request);

            Context context = new Context();
            context.setVariables(data);

            String htmlContent = templateEngine
                    .process(Objects.requireNonNull(env.getProperty("pdf.template.name")), context);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            String filename = "pdf_" + request.getOrderId() + "_" + savePdfDateFormat.format(LocalDate.now());

            String outputFolder = env.getProperty("pdf.downloads.path") + File.separator
                    + filename + ".pdf";

            FileOutputStream outputStream = new FileOutputStream(outputFolder);

            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(bos);
            renderer.createPDF(outputStream);

            outputStream.close();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition",
                    String.format("attachment; filename=%s", filename));

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(new InputStreamResource(new ByteArrayInputStream(bos.toByteArray())));
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    public PdfRequest request(String orderId) {
        return PdfRequest.builder()
                .orderId(orderId)
                .transferDate(LocalDate.now())
                .payerInfo("b")
                .payerRnn("c")
                .payerAddress("d")
                .payerAccAbsCode("e")
                .amount(50000.4123)
                .valueDate(LocalDate.now())
                .beneficiaryName("f")
                .beneficiaryAccCode("Kal3Ec1")
                .beneficiaryCountry("Кыргызстан")
                .beneficiaryBankName("Халык Банк")
                .beneficiaryBankSwift("Халык Банк")
                .commissionNumber("12.2")
                .commissionType("Трансфер")
                .paymentPurpose("Оплата за контракт")
                .appSignatureDateTime(LocalDateTime.now())
                .otpCode("ASDASD")
                .build();
    }

}
