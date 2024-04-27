package com.appli.clcapi.productCart.entity;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productCart_tbl")
@Builder
public class ProductCartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long proCartId;
    private Long quantity;
    private Long discount;
    private Long total;
    private Long netAmount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;
    private boolean deleted = false;

}