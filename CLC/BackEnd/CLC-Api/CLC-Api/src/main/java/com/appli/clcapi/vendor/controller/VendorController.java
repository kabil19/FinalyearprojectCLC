package com.appli.clcapi.vendor.controller;

import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/vendor/")
@CrossOrigin(origins = "*")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping("register")
    public String register(@RequestBody VendorDto vendorDto){
        return vendorService.register(vendorDto);
    }
    @DeleteMapping("delete/{vendorID}")
    public String delete(@PathVariable Long vendorID){
        return vendorService.delete(vendorID);
    }

    @PutMapping("update")
    public String update(@RequestBody VendorDto vendorDto){
        return vendorService.update(vendorDto);
    }
    @GetMapping("getAll")
    public List<VendorDto> getAll(){
        return vendorService.getAll();
    }

    @GetMapping("select/{existingChar}")
    public List<VendorDto> selectVendors(@PathVariable String existingChar){
        return vendorService.selectVendors(existingChar);
    }

}
