package com.appli.clcapi.tempInvoice.entity;




import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Builder
@Entity(name = "tempInvoice")
@AllArgsConstructor
@NoArgsConstructor
public class TempInvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tempInvoiceId;

    private Long tempInvoiceNumber;

    private Date date;

    private Long netAmount;

    private Boolean finalized = false;

    @ManyToOne
    @JoinColumn(name="custId", nullable = false)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "tempInvoiceEntity" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCartEntity> productCartEntity;

    public TempInvoiceEntity(TempInvoiceEntity tempInvoiceEntity){
        super();
        this.setTempInvoiceNumber(tempInvoiceEntity.getTempInvoiceNumber());
        this.setTempInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        this.setDate(tempInvoiceEntity.getDate());
        this.setNetAmount(tempInvoiceEntity.getNetAmount());
        this.setCustomer(new CustomerEntity());
    }

    public TempInvoiceEntity(TempInvoiceDto tempInvoiceDto){
        super();
        this.setTempInvoiceNumber(tempInvoiceDto.getTempInvoiceNumber());
        this.setTempInvoiceId(tempInvoiceDto.getTempInvoiceId());
        this.setDate(tempInvoiceDto.getDate());
        this.setNetAmount(tempInvoiceDto.getNetAmount());
        this.setCustomer(new CustomerEntity(tempInvoiceDto.getCustomerOBJ()));
    }

    public TempInvoiceEntity(Long tempInvoiceId){
        this.setTempInvoiceId(tempInvoiceId);
    }

}
