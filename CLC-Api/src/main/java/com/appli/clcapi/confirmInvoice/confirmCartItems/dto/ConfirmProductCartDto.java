package com.appli.clcapi.confirmInvoice.confirmCartItems.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.stock.dto.StockDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmProductCartDto {

    @JsonProperty("confirmProductCartId")
    private Long confirmProductCartId;

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

//    @JsonProperty("tempInvoiceOBJ")
//    private TempInvoiceDto tempInvoiceDto;

    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;


    public ConfirmProductCartDto(ConfirmProductCartEntity confirmProductCartEntity) {
        this.confirmProductCartId = confirmProductCartEntity.getConfirmProductCartId();
        this.stockDto = new StockDto(confirmProductCartEntity.getStockEntity());
        this.quantity = confirmProductCartEntity.getQuantity();
        this.discount = confirmProductCartEntity.getDiscount();
        this.total = confirmProductCartEntity.getTotal();
        this.netAmount = confirmProductCartEntity.getNetAmount();
//        this.tempInvoiceDto = new TempInvoiceDto(confirmProductCartEntity.getTempInvoiceEntity());
        this.confirmInvoiceDto = new ConfirmInvoiceDto(confirmProductCartEntity.getConfirmSalesInvoiceEntity());
    }


}
