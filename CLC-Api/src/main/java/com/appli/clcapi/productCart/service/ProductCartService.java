package com.appli.clcapi.productCart.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;



public interface ProductCartService {
     NonPaginatedResponse addProductsToCart(ProductCartDto productCartDto) ;
     NonPaginatedResponse getAllTempProCartItemsByInvoiceId(Long invoiceId);
     NonPaginatedResponse deleteProductFromTheCart(Long proCartId) ;
     NonPaginatedResponse addMainDiscount(Long proCartId, Double mainDiscount) ;
     NonPaginatedResponse select(Long invoiceId, String exitingChar);
     NonPaginatedResponse update(ProductCartDto productCartDto);

}
