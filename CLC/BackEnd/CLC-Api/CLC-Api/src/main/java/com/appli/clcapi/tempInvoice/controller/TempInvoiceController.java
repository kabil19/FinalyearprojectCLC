package com.appli.clcapi.tempInvoice.controller;


import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.service.TempInvoiceService;
import lombok.RequiredArgsConstructor;
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
        public ResponseEntity<String> register(@RequestBody TempInvoiceDto tempInvoiceDto)throws Exception{
            return tempInvoiceService.register(tempInvoiceDto);
        }

        @DeleteMapping("delete/{tempInvoiceId}")
        public ResponseEntity<String> delete(@PathVariable Long tempInvoiceId)throws Exception{
            return tempInvoiceService.delete(tempInvoiceId);
        }

        @PutMapping("update")
        public ResponseEntity<String> update(@RequestBody  TempInvoiceDto tempInvoiceDto)throws Exception{
            return tempInvoiceService.update(tempInvoiceDto);
        }

        @GetMapping("getAll")
        public ResponseEntity<List<?>> getAll()throws Exception{
            return tempInvoiceService.getAll();
        }

        @GetMapping("select/{existingChar}")
        public ResponseEntity<ArrayList<?>> select(@PathVariable String existingChar)throws Exception{
            return tempInvoiceService.select(existingChar);
        }

}
