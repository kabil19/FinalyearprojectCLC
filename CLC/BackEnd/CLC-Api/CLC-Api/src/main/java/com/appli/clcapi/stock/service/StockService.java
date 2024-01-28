package com.appli.clcapi.stock.service;


import com.appli.clcapi.stock.dto.StockDto;


import java.util.List;

public interface StockService {
    String register(StockDto stockDto);

    String delete(Long stockId);

    String update(StockDto stockDto);

    List<StockDto> selectStocks(String existingChar);

    List<StockDto> getAll();
}
