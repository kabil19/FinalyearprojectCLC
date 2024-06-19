package com.appli.clcapi.stock.controller;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.service.StockService;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/stock/")
@CrossOrigin("http://localhost:4200")
public class StockController{

    private final StockService stockService;
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody StockDto stockDto){
        return stockService.register(stockDto);
    }
    @DeleteMapping("delete/{stockId}")
    public ResponseEntity<String> delete(@PathVariable Long stockId){
        return stockService.delete(stockId);
    }

    @PutMapping("update")
    public ResponseEntity<String> update(@RequestBody StockDto stockDto){
        return stockService.update(stockDto);
    }

    @GetMapping("getAll")
    public List<StockDto> getAll(){
        return stockService.getAll();
    }

    @GetMapping("select/{existingChar}")
    public List<StockDto> selectStocks(@PathVariable String existingChar){
        return stockService.selectStocks(existingChar);
    }

    @GetMapping("getPaginatedAll")
    public List<StockDto> getPaginatedAll(@PathParam("pageNum") int pageNum){
        return stockService.getPaginatedAll(pageNum, 3);
    }
}