package com.appli.clcapi.stock.serviceImple;

import com.appli.clcapi.category.entity.CategoryEntity;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import com.appli.clcapi.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {

    private final StockRepo stockRepo;

    @Override
    public ResponseEntity<String> register(StockDto stockDto) {
        LocalDateTime now = LocalDateTime.now();
        try{
            if(stockDto.getSellingPrice() <stockDto.getPurchasePrice()){
                return new ResponseEntity<>("Purchase price can't be greater than Selling Price!",HttpStatus.BAD_REQUEST);
            }
            StockEntity aStock = StockEntity.builder()
                    .stockId(stockDto.getStockId())
                    .categoryEntity(new CategoryEntity(stockDto.getCategoryOBJ().getCategoryId()))
                    .itemName(stockDto.getItemName())
                    .arrivalDate(now)
                    .quantity(stockDto.getQuantity())
                    .materialColour(stockDto.getMaterialColour())
                    .remarks(stockDto.getRemarks())
                    .deleted(false)
                    .purchasePrice(stockDto.getPurchasePrice())
                    .sellingPrice(stockDto.getSellingPrice())
                    .reorderQty(stockDto.getReorderQty())
                    .build();
            stockRepo.save(aStock);
            return new ResponseEntity<>("Stock is Inserted!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long stockId) {
        try {
            StockEntity aStock = stockRepo.getReferenceById(stockId);
            if (aStock.getTempPurchaseProductCartEntity().isEmpty() && aStock.getProductCartEntity().isEmpty()) {
                aStock.setDeleted(true);
                stockRepo.save(aStock);
                return new ResponseEntity<>("Stock is deleted!", HttpStatus.OK);
            }
            return new ResponseEntity<>("Stock as some other references, so can't be deleted!", HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> update(StockDto stockDto) {
        try{
            Optional<StockEntity> aStock = stockRepo.findById(stockDto.getStockId());
            if(stockDto.getSellingPrice() <stockDto.getPurchasePrice()){
                return new ResponseEntity<>("Purchase price can't be greater than Selling Price!",HttpStatus.BAD_REQUEST);
            }
            if(aStock.isPresent()){
                StockEntity updatedStock;
                updatedStock = aStock.get();
                updatedStock.setCategoryEntity(new CategoryEntity(stockDto.getCategoryOBJ().getCategoryId()));
                updatedStock.setItemName(stockDto.getItemName());
                updatedStock.setQuantity(stockDto.getQuantity());
                updatedStock.setRemarks(stockDto.getRemarks());
                updatedStock.setArrivalDate(stockDto.getArrivalDate());
                updatedStock.setMaterialColour(stockDto.getMaterialColour());
                updatedStock.setPurchasePrice(stockDto.getPurchasePrice());
                updatedStock.setSellingPrice(stockDto.getSellingPrice());
                updatedStock.setReorderQty(stockDto.getReorderQty());
                stockRepo.save(updatedStock);
                return new ResponseEntity<>("Stock is updated!",HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }


    @Override
    public List<StockDto> selectStocks(String existingChar) {
       List<StockEntity> stockEntities = stockRepo.
               findAllByMaterialColourContainingIgnoreCaseOrItemNameContainingIgnoreCaseOrRemarksContainingIgnoreCase
               (existingChar,existingChar,existingChar);
       List<StockDto> stockDtoList = new ArrayList<>();
       for (StockEntity aStock : stockEntities){
           if(!aStock.getCategoryEntity().isDeleted()){
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
    public List<StockDto> getPaginatedAll(int pageNum, int pageSize) {
        Pageable sortedByName =   PageRequest.of(pageNum, 3, Sort.by("itemName"));
//        List<StockEntity> stockList = stockRepo.findAllByDeletedEquals(false);
        Page<StockEntity> stockList = stockRepo.findAll(sortedByName);
        List<StockDto> stockDtoListForView = new ArrayList<>();
        for(StockEntity aStock : stockList) {
            StockDto stockDto = new StockDto(aStock);
            stockDtoListForView.add(stockDto);
        }
        return stockDtoListForView;
    }
}
