package com.appli.clcapi.paymentMethod.tempPayMethods.dto;

import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
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
public class TempCashDto {
    @JsonProperty("cashId")
    private Long cashId;

    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("tempInvoiceOBJ")
    private TempInvoiceDto tempInvoiceDto;

}
