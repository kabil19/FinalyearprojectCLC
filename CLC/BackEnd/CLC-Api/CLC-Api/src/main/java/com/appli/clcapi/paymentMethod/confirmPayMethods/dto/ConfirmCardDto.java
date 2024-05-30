package com.appli.clcapi.paymentMethod.confirmPayMethods.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmCardDto {
    @JsonProperty("cardRefNo")
    private Long cardRefNo;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("paidDate")
    private Date paidDate;

    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;


}
