package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmSalesInvoiceCardDto {
    @JsonProperty("cardRefNo")
    private Long cardRefNo;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("paidDate")
    private LocalDateTime paidDate;

    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;


}
