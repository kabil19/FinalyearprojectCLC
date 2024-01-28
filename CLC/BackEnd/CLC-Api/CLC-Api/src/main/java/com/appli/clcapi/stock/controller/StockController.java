package com.appli.clcapi.stock.controller;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/stock/")
@CrossOrigin(origins = "*")
public class StockController{

    private final StockService stockService;
    @PostMapping("register")
    public String register(@RequestBody StockDto stockDto){
        return stockService.register(stockDto);
    }
    @GetMapping("getAll")
    public List<StockDto> getAll(){
        return stockService.getAll();
    }

    @DeleteMapping("delete/{stockId}")
    public String delete(@PathVariable Long stockId){
        return stockService.delete(stockId);
    }

    @PutMapping("update")
    public String update(@RequestBody StockDto stockDto){
        return stockService.update(stockDto);
    }

    @GetMapping("select/{existingChar}")
    public List<StockDto> selectStocks(@PathVariable String existingChar){
        return stockService.selectStocks(existingChar);
    }
}