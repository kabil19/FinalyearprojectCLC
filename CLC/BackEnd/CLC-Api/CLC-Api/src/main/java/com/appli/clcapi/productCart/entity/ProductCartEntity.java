package com.appli.clcapi.productCart.entity;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productCart_tbl")
@Builder
public class ProductCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long proCartId;
    private Double quantity;
    private Double discount;
    private Double total;
    private Double netAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;

   /* @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;*/

    private boolean deleted = false;

}