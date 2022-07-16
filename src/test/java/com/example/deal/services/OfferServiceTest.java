package com.example.deal.services;

import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.repositoryes.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OfferServiceTest {

    @Mock
    ProducerService producerService;

    @Mock
    ApplicationRepository applicationRepository;

    LoanOfferDTO loanOfferDTO = new LoanOfferDTO();

    @BeforeEach
    void setLoanOfferDTO() {
        loanOfferDTO.setApplicationId(10L);
        loanOfferDTO.setRequestedAmount(BigDecimal.valueOf(2000000));
        loanOfferDTO.setTotalAmount(BigDecimal.valueOf(2060000));
        loanOfferDTO.setTerm(24);
        loanOfferDTO.setMonthlyPayment(BigDecimal.valueOf(100864.01));
        loanOfferDTO.setIsInsuranceEnabled(true);
        loanOfferDTO.setIsSalaryClient(true);
    }

    @Test
    void putOfferShouldUpdateApplication() {
        OfferService offerService = new OfferService(applicationRepository, producerService);

        Application application = new Application();
        application.setStatusHistory(new ArrayList<>());

        Mockito.when(applicationRepository.findById(loanOfferDTO.getApplicationId())).thenReturn(Optional.of(application));
        offerService.putOffer(loanOfferDTO);

        Mockito.verify(applicationRepository, times(1)).save(Mockito.any());
        Mockito.verify(applicationRepository, times(1)).findById(Mockito.any());
        assertNotNull(application.getStatusHistory());
        assertNotNull(application.getCredit());
        assertEquals(Status.APPROVED, application.getStatus());
    }

    @Test
    void putOfferBaseDataException() {
        OfferService offerService = new OfferService(applicationRepository, producerService);

        try {
            Mockito.when(applicationRepository.findById(loanOfferDTO.getApplicationId())).thenThrow(NoSuchElementException.class);

            offerService.putOffer(loanOfferDTO);
        } catch (RuntimeException e) {
            assertEquals(BaseDataException.class, e.getClass());
        }
    }

}