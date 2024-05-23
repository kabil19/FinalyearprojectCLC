package com.appli.clcapi.productCart.controller;


import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;
import com.appli.clcapi.productCart.service.ProductCartService;

import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/productCart/")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductCartController {



    private final ProductCartService productCartService;
    @PostMapping("register")
    public NonPaginatedResponse register(@RequestBody ProductCartDto productCartDto)
    {
        return productCartService.addToCart(productCartDto);
    }

    @DeleteMapping("delete/{proCartId}")
    public NonPaginatedResponse delete(@PathVariable Long proCartId){
        return productCartService.delete(proCartId);
    }

    @PutMapping("update")
    public NonPaginatedResponse update(@RequestBody ProductCartDto productCartDto){
        return productCartService.update(productCartDto);
    }

    @GetMapping("getAll/{invoiceId}")
    public NonPaginatedResponse getAll(@PathVariable Long invoiceId){
        return productCartService.getAll(invoiceId);
    }



    @GetMapping("select/{invoiceId}/{existingChar}")
    public NonPaginatedResponse select(@PathVariable Long invoiceId, @PathVariable String existingChar){
        return productCartService.select(invoiceId, existingChar);
    }

}