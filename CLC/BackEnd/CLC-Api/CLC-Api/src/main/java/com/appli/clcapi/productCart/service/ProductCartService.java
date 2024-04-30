package com.appli.clcapi.productCart.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;



public interface ProductCartService {
     NonPaginatedResponse addToCart(ProductCartDto productCartDto) ;
     NonPaginatedResponse delete(Long proCartId) ;
     NonPaginatedResponse getAll(Long invoiceId);
     NonPaginatedResponse select(String exitingChar);
     NonPaginatedResponse update(ProductCartDto productCartDto);
}
