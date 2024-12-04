package com.appli.clcapi.invoiceReturn.dto;
import com.appli.clcapi.invoiceReturn.entity.SalesInvoiceCartReturnEntity;
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
public class SalesInvoiceCartReturnDto {

    @JsonProperty("confirmProductCartId")
    private Long salesRetProductCartId;

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

    @JsonProperty("confirmInvoiceOBJ")
    private SalesReturnInvoiceDto salesReturnInvoiceDto;



    public SalesInvoiceCartReturnDto(SalesInvoiceCartReturnEntity salesInvoiceCartReturnEntity) {
        this.salesRetProductCartId = salesInvoiceCartReturnEntity.getSalesRetProductCartId();
        this.stockDto = new StockDto(salesInvoiceCartReturnEntity.getStockEntity());
        this.quantity = salesInvoiceCartReturnEntity.getQuantity();
        this.discount = salesInvoiceCartReturnEntity.getDiscount();
        this.total = salesInvoiceCartReturnEntity.getTotal();
        this.netAmount = salesInvoiceCartReturnEntity.getNetAmount();
        this.salesReturnInvoiceDto = new SalesReturnInvoiceDto(salesInvoiceCartReturnEntity.getSalesReturnInvoice());
    }


}