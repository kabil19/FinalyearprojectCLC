package com.appli.clcapi.tempInvoice.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.customer.repository.CustomerRepo;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.productCart.repository.ProductCartRepo;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import com.appli.clcapi.tempInvoice.service.TempInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TempInvoiceServiceImpl implements TempInvoiceService {

    private final TempInvoiceRepo tempInvoiceRepo;
    private final ProductCartRepo tempProductCartRepo;
    private final StockRepo stockRepo;
    private final CustomerRepo customerRepo;

    @Override
    public NonPaginatedResponse createTempSalesInvoice(TempInvoiceDto tempInvoiceDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<CustomerEntity> customerEntity = customerRepo.findById(tempInvoiceDto.getCustomerEntity().getCustId());
            if (customerEntity.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("The Selected Customer isn't exist!"));
                return response;
            }
            var anInvoice = TempInvoiceEntity.builder()
                    .tempInvoiceId(tempInvoiceDto.getTempInvoiceId())
                    .date(LocalDateTime.now())
                    .netAmount(tempInvoiceDto.getNetAmount())
                    .paidAmount(tempInvoiceDto.getPaidAmount())
                    .finalized(false)
                    .isComplete(false)
                    .tempInvoiceNumber(tempInvoiceDto.getTempInvoiceNumber())
                    .customer(new CustomerEntity(tempInvoiceDto.getCustomerEntity()))
                    .build();
            tempInvoiceRepo.save(anInvoice);
            response.setSuccessMessage("Sales Invoice is created!");
            response.setStatus(HttpStatus.OK);
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't create the Sales Invoice!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse deleteTempSalesInvoice(Long tempInvoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Optional<TempInvoiceEntity> aTempInvoice = tempInvoiceRepo.findById(tempInvoiceId);
            if (aTempInvoice.isPresent()) {
                List<ProductCartEntity> cartItems = tempProductCartRepo.findByTempInvoiceEntity_TempInvoiceId(aTempInvoice.get().getTempInvoiceId());
                if (!aTempInvoice.get().getFinalized()) {
                    alterQTYinStockEntity(cartItems);
                    tempInvoiceRepo.deleteById(tempInvoiceId);
                    response.setSuccessMessage("Temp Sales Invoice is Successfully deleted!");
                    response.setStatus(HttpStatus.OK);
                }
            }
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't Delete the selected Temp Sales Invoice!"));
        }
        return response;

    }

    private void alterQTYinStockEntity(List<ProductCartEntity> cartItems) {
        for (ProductCartEntity anItemInCart : cartItems) {

            StockEntity stockEntity = stockRepo.findById(anItemInCart.getStockEntity().getStockId()).get();
            stockEntity.setQuantity(stockEntity.getQuantity() + anItemInCart.getQuantity());
            stockRepo.save(stockEntity);
        }
    }

    @Override
    public ResponseEntity<String> update(TempInvoiceDto tempInvoiceDto) {
        try {

            Optional<TempInvoiceEntity> aTempInvoice = tempInvoiceRepo.findById(tempInvoiceDto.getTempInvoiceId());
            if (!aTempInvoice.get().getFinalized()) {
                TempInvoiceEntity updatedTempInvoice;
                updatedTempInvoice = aTempInvoice.get();
                updatedTempInvoice.setTempInvoiceId(tempInvoiceDto.getTempInvoiceId());
                updatedTempInvoice.setDate(tempInvoiceDto.getDate());
                updatedTempInvoice.setNetAmount(tempInvoiceDto.getNetAmount());
                updatedTempInvoice.setPaidAmount(tempInvoiceDto.getPaidAmount());
                updatedTempInvoice.setTempInvoiceNumber(tempInvoiceDto.getTempInvoiceNumber());
                updatedTempInvoice.setCustomer(new CustomerEntity(tempInvoiceDto.getCustomerEntity()));
                if (tempInvoiceDto.getFinalized() != null) {
                    updatedTempInvoice.setFinalized(tempInvoiceDto.getFinalized());
                } else {
                    updatedTempInvoice.setFinalized(false);
                }
                tempInvoiceRepo.save(updatedTempInvoice);
                return new ResponseEntity<>("Invoice has been updated", HttpStatus.OK);
            }
            return new ResponseEntity<>("Finalized Invoice can't be updated", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<?>> getAll() {
        List<TempInvoiceEntity> existingTempInvoices = tempInvoiceRepo.findAll();
        List<TempInvoiceDto> listOfTempInvoiceDto = new ArrayList<>();
        for (TempInvoiceEntity aTempInvoice : existingTempInvoices) {
            TempInvoiceDto tempInvoiceDto = new TempInvoiceDto(aTempInvoice);
            listOfTempInvoiceDto.add(tempInvoiceDto);
        }
        return new ResponseEntity<>(listOfTempInvoiceDto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ArrayList<?>> select(String existingChar) {
        Iterable<TempInvoiceEntity> searchedListOfTempInvoices;
        searchedListOfTempInvoices = tempInvoiceRepo.findByCustomerCustNameContainingIgnoreCase(existingChar);
        ArrayList<TempInvoiceDto> listOfDtoForView = new ArrayList<>();
        for (TempInvoiceEntity anInvoice : searchedListOfTempInvoices) {
            TempInvoiceDto anInvoiceDto = new TempInvoiceDto(anInvoice);
            listOfDtoForView.add(anInvoiceDto);
        }
        return new ResponseEntity<>(listOfDtoForView, HttpStatus.OK);
    }

    @Override
    public NonPaginatedResponse getTempInvoiceById(Long id) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempInvoiceEntity> invoiceEntity = tempInvoiceRepo.findById(id);
            TempInvoiceDto tempInvoiceDto = new TempInvoiceDto(invoiceEntity.get());
            response.setResult(tempInvoiceDto);
            response.setSuccessMessage("Temp Invoice is retrieved!");
        } catch (Exception e) {
            response.setErrors(List.of("Couldn't retrieve!"));
        }
        return response;
    }


}
