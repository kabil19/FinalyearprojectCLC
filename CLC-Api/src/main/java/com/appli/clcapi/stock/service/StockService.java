package com.appli.clcapi.stock.service;


import com.appli.clcapi.stock.dto.StockDto;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface StockService {
    ResponseEntity<String> register(StockDto stockDto);

    ResponseEntity<String>  delete(Long stockId);

    ResponseEntity<String>  update(StockDto stockDto);

    List<StockDto> selectStocks(String existingChar);

    List<StockDto> getAll();
     List<StockDto> getPaginatedAll(int pageNum, int pageSize);
}
