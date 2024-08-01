package com.appli.clcapi.productCart.dto;

import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCartDto {

    @JsonProperty("proCartId")
    private Long proCartId;

    @JsonProperty("stockOBJ")
    private StockDto stockDto;

    @JsonProperty("quantity")
    private Double quantity;

    @JsonProperty("discount")
    private Double discount;

    @JsonProperty("total")
    private Double total;

    @JsonProperty("netAmount")
    private Double netAmount;

    @JsonProperty("tempInvoiceOBJ")
    private TempInvoiceDto tempInvoiceDto;

//    @JsonProperty("confirmInvoiceOBJ")
//    private ConfirmInvoiceDto confirmInvoiceDto;

    @JsonIgnore
    private boolean deleted = false;

    public ProductCartDto(ProductCartEntity productCartEntity){
        setProCartId(productCartEntity.getProCartId());
        setQuantity(productCartEntity.getQuantity());
        setDiscount(productCartEntity.getDiscount());
        setNetAmount(productCartEntity.getNetAmount());
        setTotal(productCartEntity.getTotal());
        setTempInvoiceDto(new TempInvoiceDto(productCartEntity.getTempInvoiceEntity()));
        setStockDto(new StockDto(productCartEntity.getStockEntity()));
//        setConfirmInvoiceDto(new ConfirmInvoiceDto(productCartEntity.getConfirmInvoiceEntity()));
    }
}
