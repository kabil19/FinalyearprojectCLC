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
        public ResponseEntity<?> register(@RequestBody TempInvoiceDto tempInvoiceDto){
            tempInvoiceService.register(tempInvoiceDto);
            return new ResponseEntity<>("Invoice has been created", HttpStatus.CREATED);
        }

        @DeleteMapping("delete/{tempInvoiceId}")
        public ResponseEntity<?> delete(@PathVariable Long tempInvoiceId){
            tempInvoiceService.delete(tempInvoiceId);
            return new ResponseEntity<>("Selected invoice has been wiped out", HttpStatus.OK);
        }

        @PutMapping("update")
        public ResponseEntity<?> update(@RequestBody  TempInvoiceDto tempInvoiceDto){
            tempInvoiceService.update(tempInvoiceDto);
            return new ResponseEntity<>("Selected invoice has been updated", HttpStatus.OK);
        }

        @GetMapping("getAll")
        public List<TempInvoiceDto> getAll(){
            return tempInvoiceService.getAll();
        }

        @GetMapping("select/{existingChar}")
        public ArrayList<TempInvoiceDto> select(@PathVariable String existingChar){
            return tempInvoiceService.select(existingChar);
        }



}
