package com.appli.clcapi.notification.serviceImpl;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.notification.service.NotificationService;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.dto.ConfirmSalesInvoiceChequeDto;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmSalesInvoiceChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmSalesPayChequeRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.dto.PurchasePayChequeDto;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayChequeRepo;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final PurchasePayChequeRepo purchasePayChequeRepo;
    private final ConfirmSalesPayChequeRepo salesPayChequeRepo;
    private final StockRepo stockRepo;

    @Override
    public NonPaginatedResponse fetchAllNotifications() {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<PurchasePayChequeEntity> purchaseChequeList = purchasePayChequeRepo.findAll();
            List<ConfirmSalesInvoiceChequeEntity> salesInvoiceChequeList = salesPayChequeRepo.findAll();
            List<StockEntity> stockList = stockRepo.findAllByDeletedEquals(false);
            LocalDateTime today = LocalDateTime.now();

            int alartThreshold = 5;

            List<PurchasePayChequeDto> purchasePayChequeDtos = purchaseChequeList.stream()
                    .filter(
                            aCheque -> {
                                long daysRemaining = aCheque.getChequeDueDate().toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay();
                                return daysRemaining <= alartThreshold && daysRemaining >= 0;
                            }
                    ).map(PurchasePayChequeDto::new)
                    .toList();
            List<ConfirmSalesInvoiceChequeDto> salesInvoiceChequeDtos = salesInvoiceChequeList.stream()
                    .filter(
                            aCheque -> {
                                long daysRemaining = aCheque.getChequeDueDate().toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay();
                                return daysRemaining <= alartThreshold && daysRemaining >= 0;
                            }
                    ).map(ConfirmSalesInvoiceChequeDto::new)
                    .toList();

            List<StockDto> productDtos = stockList.stream()
                    .filter(
                            aProduct -> aProduct.getQuantity() <= aProduct.getReorderQty())
                    .map(StockDto::new)
                    .toList();
            HashMap<String, Object> notificationResponse = new HashMap<>();
            notificationResponse.put("productsLowerThanReorderLevel",productDtos);
            notificationResponse.put("salesChequeDues", salesInvoiceChequeDtos);
            notificationResponse.put("purchaseChequeDues", purchasePayChequeDtos);


            response.setResult(notificationResponse);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Data Fetched to notify!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Error Fetching Notifications!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
