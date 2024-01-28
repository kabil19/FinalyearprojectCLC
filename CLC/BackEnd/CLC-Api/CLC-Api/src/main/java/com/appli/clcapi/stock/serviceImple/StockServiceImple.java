package com.appli.clcapi.stock.serviceImple;

import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import com.appli.clcapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImple implements StockService {

    private final StockRepo stockRepo;
    @Override
    public String register(StockDto stockDto) {
        try{
            StockEntity aStock = StockEntity.builder()
                    .stockId(stockDto.getStockId())
                    .materialName(stockDto.getMaterialName())
                    .materialType(stockDto.getMaterialType())
                    .arrivalDate(stockDto.getArrivalDate())
                    .quantity(stockDto.getQuantity())
                    .materialColour(stockDto.getMaterialColour())
                    .remarks(stockDto.getRemarks())
                    .purchasePrice(stockDto.getPurchasePrice())
                    .sellingPrice(stockDto.getSellingPrice())
                    .reorderQty(stockDto.getReorderQty())
                    .build();
            stockRepo.save(aStock);
            return "Stock has been Inserted";
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Could add the stock");
        }
    }

    @Override
    public String delete(Long stockId) {
        StockEntity aStock = stockRepo.getReferenceById(stockId);
        aStock.setDeleted(true);
        stockRepo.save(aStock);
        return "Stock has been deleted";
    }

    @Override
    public String update(StockDto stockDto) {
        try{
            Optional<StockEntity> aStock = stockRepo.findById(stockDto.getStockId());
            if(aStock.isPresent()){
                StockEntity existingStock = aStock.get();
                existingStock.setMaterialName(stockDto.getMaterialName());
                existingStock.setMaterialType(stockDto.getMaterialType());
                existingStock.setQuantity(stockDto.getQuantity());
                existingStock.setRemarks(stockDto.getRemarks());
                existingStock.setArrivalDate(stockDto.getArrivalDate());
                existingStock.setMaterialColour(stockDto.getMaterialColour());
                existingStock.setPurchasePrice(stockDto.getPurchasePrice());
                existingStock.setSellingPrice(stockDto.getSellingPrice());
                existingStock.setReorderQty(stockDto.getReorderQty());
            }
            return "Selected StockHas been Updated";
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Couldn't update the selected stock");
        }

    }

    @Override
    public List<StockDto> selectStocks(String existingChar) {
       List<StockEntity> stockEntities = stockRepo.findAllByMaterialColourContainingIgnoreCaseOrMaterialNameContainingIgnoreCaseOrMaterialTypeContainingIgnoreCaseOrRemarksContainingIgnoreCase
               (existingChar,existingChar,existingChar,existingChar);
       List<StockDto> stockDtoList = new ArrayList<>();
       for (StockEntity aStock : stockEntities){
           if(!aStock.isDeleted()){
               StockDto aNewStockDto = new StockDto(aStock);
               stockDtoList.add(aNewStockDto);
           }
       }
       return stockDtoList;
    }

    @Override
    public List<StockDto> getAll() {
        List<StockEntity> stockList = stockRepo.findAllByDeletedEquals(false);
        List<StockDto> stockDtoListForView = new ArrayList<>();
        for(StockEntity aStock : stockList) {
                StockDto stockDto = new StockDto(aStock);
                stockDtoListForView.add(stockDto);
        }
        return stockDtoListForView;
    }
}
