package com.example.deal.services;

import com.example.deal.clients.FeignConveyor;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.myExceptions.ConnectionException;
import com.example.deal.myExceptions.ScoringException;
import com.example.deal.repositoryes.ApplicationRepository;
import com.example.deal.repositoryes.ClientRepository;
import feign.FeignException;
import feign.RetryableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    ClientRepository clientRepository;

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    FeignConveyor feignConveyor;

    LoanApplicationRequestDTO loanApplicationRequestDTO = new LoanApplicationRequestDTO();

    @BeforeEach
    void setLoanApplicationRequestDTO() {
        loanApplicationRequestDTO.setTerm(24);
        loanApplicationRequestDTO.setAmount(BigDecimal.valueOf(2000000));
        loanApplicationRequestDTO.setFirstName("Aleksandr");
        loanApplicationRequestDTO.setLastName("Dmitriev");
        loanApplicationRequestDTO.setMiddleName("Sergeevich");
        loanApplicationRequestDTO.setEmail("dmitriev_alexandr93@mail.ru");
        loanApplicationRequestDTO.setBirthdate(LocalDate.of(1993, 7, 28));
        loanApplicationRequestDTO.setPassportSeries("1234");
        loanApplicationRequestDTO.setPassportNumber("123456");
    }

    @Test
    void getResponseListLoanDTO() {

        ApplicationService applicationService = new ApplicationService(clientRepository, applicationRepository, feignConveyor);

        List<LoanOfferDTO> mocklist = new ArrayList<>();
        mocklist.add(new LoanOfferDTO());
        mocklist.add(new LoanOfferDTO());
        mocklist.add(new LoanOfferDTO());
        mocklist.add(new LoanOfferDTO());

        Mockito.when(feignConveyor.addNewOffer(loanApplicationRequestDTO)).thenReturn(new ResponseEntity<>(mocklist, HttpStatus.OK));

        List<LoanOfferDTO> resultTest = applicationService.getResponseListLoanDTO(loanApplicationRequestDTO);
        assertEquals(4, resultTest.size());
        Mockito.verify(feignConveyor, times(1)).addNewOffer(Mockito.any());
        Mockito.verify(applicationRepository, times(2)).save(Mockito.any());
        Mockito.verify(clientRepository, times(1)).save(Mockito.any());
    }

    @Test
    void getResponseListLoanDTOScoringEException() {

        ApplicationService applicationService = new ApplicationService(clientRepository, applicationRepository, feignConveyor);

        try {
            Mockito.when(feignConveyor.addNewOffer(loanApplicationRequestDTO)).thenThrow(FeignException.class);
            Mockito.verify(applicationService.getResponseListLoanDTO(loanApplicationRequestDTO));
        } catch (RuntimeException e) {
            assertEquals(ScoringException.class, e.getClass());
        }
    }

    @Test
    void getResponseListLoanDTOConnectionException() {

        ApplicationService applicationService = new ApplicationService(clientRepository, applicationRepository, feignConveyor);

        try {
            Mockito.when(feignConveyor.addNewOffer(loanApplicationRequestDTO)).thenThrow(RetryableException.class);
            Mockito.verify(applicationService.getResponseListLoanDTO(loanApplicationRequestDTO));
        } catch (RuntimeException e) {
            assertEquals(ConnectionException.class, e.getClass());
        }
    }

}