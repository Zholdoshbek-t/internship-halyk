package com.halyk.study.pdf;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PdfRequest {

    private String orderId;

    private LocalDate transferDate;

    private String transferDateStr;

    private String payerInfo;

    private String payerRnn;

    private String payerAddress;

    private String payerAccAbsCode;

    private Double amount;

    private LocalDate valueDate;

    private String valueDateStr;

    private String beneficiaryName;

    private String beneficiaryAccCode;

    private String beneficiaryCountry;

    private String beneficiaryBankName;

    private String beneficiaryBankSwift;

    private String commissionNumber;

    private String commissionType;

    private String paymentPurpose;

    private LocalDateTime appSignatureDateTime;

    private String appSignatureDateTimeStr;

    private String otpCode;
}
