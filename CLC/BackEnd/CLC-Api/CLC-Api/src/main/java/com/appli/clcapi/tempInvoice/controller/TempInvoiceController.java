package com.appli.clcapi.tempInvoice.controller;


import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.service.TempInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tempInvoice/")
@CrossOrigin(origins = "*")
public class TempInvoiceController {

        private final TempInvoiceService tempInvoiceService;
        @PostMapping("register")
        public ResponseEntity<String> register(@RequestBody TempInvoiceDto tempInvoiceDto){
            return tempInvoiceService.register(tempInvoiceDto);
        }

        @DeleteMapping("delete/{tempInvoiceId}")
        public ResponseEntity<String> delete(@PathVariable Long tempInvoiceId){
            return tempInvoiceService.delete(tempInvoiceId);
        }

        @PutMapping("update")
        public ResponseEntity<String> update(@RequestBody  TempInvoiceDto tempInvoiceDto){
            return tempInvoiceService.update(tempInvoiceDto);
        }

        @GetMapping("getAll")
        public ResponseEntity<List<?>> getAll(){
            return tempInvoiceService.getAll();
        }

        @GetMapping("select/{existingChar}")
        public ResponseEntity<ArrayList<?>> select(@PathVariable String existingChar){
            return tempInvoiceService.select(existingChar);
        }

}
