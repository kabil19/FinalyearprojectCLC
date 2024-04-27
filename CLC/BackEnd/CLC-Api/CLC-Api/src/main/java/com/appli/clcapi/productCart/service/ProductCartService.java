package com.appli.clcapi.productCart.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;

import java.util.List;


public interface ProductCartService {
     NonPaginatedResponse addToCart(ProductCartDto productCartDto) ;
     NonPaginatedResponse delete(Long proCartId) ;
     NonPaginatedResponse getAll(Long invoiceId);
}
