package com.appli.clcapi.confirmInvoice.serviceImple;
import com.appli.clcapi.common.constants.ConfirmInvoiceConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ConfirmInvoiceServiceImple implements ConfirmInvoiceService {

    private final TempInvoiceRepo tempInvoiceRepo;
    private final ConfirmInvoiceRepo confirmInvoiceRepo;
    private final ConfirmProductCartService confirmProductCartService;
    @Override
    @Transactional
    public NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Optional<TempInvoiceEntity> selectedTempInvoice = tempInvoiceRepo.findById(invoiceId);
            TempInvoiceEntity tempInvoiceEntity = selectedTempInvoice.get();
            CustomerEntity aCust = tempInvoiceEntity.getCustomer();
            CustomerDto aCustDto = new CustomerDto(aCust);
            createNewConfirmInvoiceData(tempInvoiceEntity, aCustDto);

            //ConfirmCartImple execute
            Boolean isCartItemsConfirmed = confirmProductCartService.confirmTheCartItems(invoiceId);
            if(isCartItemsConfirmed){
                tempInvoiceRepo.deleteById(invoiceId);
                tempInvoiceRepo.flush();
                response.setStatus(HttpStatus.ACCEPTED);
                response.setSuccessMessage(ConfirmInvoiceConsonants.INVOICE_HAS_BEEN_CONFIRMED);
                response.setResult(null);
            }
        }catch (Exception e){
            response.setErrors(Arrays.asList("Couldn't Confirm the selected invoice"));
            response.setStatus(HttpStatus.BAD_GATEWAY);
        }
        return response;
    }

    private void createNewConfirmInvoiceData(TempInvoiceEntity tempInvoiceEntity, CustomerDto aCustDto) {
        ConfirmInvoiceEntity anInvoice = ConfirmInvoiceEntity.builder()
                .confirmInvoiceId(tempInvoiceEntity.getTempInvoiceId())
                .invoiceNumber(tempInvoiceEntity.getTempInvoiceNumber())
                .date(new Date())
                .paidAmount(tempInvoiceEntity.getPaidAmount())
                .netAmount(tempInvoiceEntity.getNetAmount())
                .customer(new CustomerEntity(aCustDto))
                .build();
        confirmInvoiceRepo.save(anInvoice);
    }


}
