package com.appli.clcapi.vendor.dto;

import com.appli.clcapi.vendor.entity.VendorEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VendorDto {

    @JsonProperty("vendorId")
    private Long vendorId;

    @JsonProperty("vendorName")
    private String vendorName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("contact")
    private  Integer contact;

    @JsonProperty(value = "deleted")
    @JsonIgnore
    private boolean deleted;

    public VendorDto(VendorEntity vendorEntity){
       this.vendorId = (vendorEntity.getVendorId());
       this.vendorName = vendorEntity.getVendorName();
       this.address = vendorEntity.getAddress();
       this.email = vendorEntity.getEmail();
       this.contact = vendorEntity.getContact();
       this.deleted = vendorEntity.isDeleted();
    }
}
