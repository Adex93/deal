package com.example.deal.services;

import com.example.deal.clients.FeignConveyor;
import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.EmploymentDTO;
import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.entity.*;
import com.example.deal.enums.Gender;
import com.example.deal.enums.MaritalStatus;
import com.example.deal.enums.Position;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.myExceptions.ConnectionException;
import com.example.deal.myExceptions.ScoringException;
import com.example.deal.repositoryes.ApplicationRepository;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CalculateServiceTest {

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    FeignConveyor feignConveyor;

    FinishRegistrationRequestDTO finishRegistrationRequestDTO = new FinishRegistrationRequestDTO();

    @BeforeEach
    void setFinishRegistrationRequestDTO() {
        finishRegistrationRequestDTO.setEmploymentDTO(new EmploymentDTO());
        finishRegistrationRequestDTO.setGender(Gender.MALE);
        finishRegistrationRequestDTO.setMaritalStatus(MaritalStatus.SINGLE);
        finishRegistrationRequestDTO.setDependentAmount(0);
        finishRegistrationRequestDTO.setPassportIssueDate(LocalDate.of(2015, 5, 15));
        finishRegistrationRequestDTO.setPassportIssueBranch("360-018");
        finishRegistrationRequestDTO.getEmploymentDTO().setEmployerINN("3664240541");
        finishRegistrationRequestDTO.getEmploymentDTO().setSalary(BigDecimal.valueOf(100000));
        finishRegistrationRequestDTO.getEmploymentDTO().setPosition(Position.MID_MANAGER);
        finishRegistrationRequestDTO.getEmploymentDTO().setWorkExperienceTotal(72);
        finishRegistrationRequestDTO.getEmploymentDTO().setWorkExperienceCurrent(24);
        finishRegistrationRequestDTO.setAccount("40702810400000123456");
    }

    @Test
    void putFinishShouldUpdateAllEntity() {
        CalculateService calculateService = new CalculateService(applicationRepository, feignConveyor);
        CreditDTO creditDTO = new CreditDTO();
        Long id = 10L;

        Credit credit = new Credit();

        Client client = new Client();
        client.setPassport(new Passport());
        client.setEmployment(new Employment());

        Application application = new Application();
        application.setCredit(credit);
        application.setClient(client);
        application.setStatusHistory(new ArrayList<>());

        Mockito.when(feignConveyor.calculating(Mockito.any())).thenReturn(new ResponseEntity<>(creditDTO, HttpStatus.OK));
        Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));

        calculateService.putFinish(finishRegistrationRequestDTO, id);

        Mockito.verify(applicationRepository, times(1)).save(Mockito.any());
        Mockito.verify(applicationRepository, times(1)).findById(Mockito.any());
        Mockito.verify(feignConveyor, times(1)).calculating(Mockito.any());
        assertNotNull(application.getStatusHistory());
        assertNotNull(application.getCredit());
        assertNotNull(application.getClient());
        assertEquals(Status.CC_APPROVED, application.getStatus());
    }

    @Test
    void putFinishBaseDataException() {
        CalculateService calculateService = new CalculateService(applicationRepository, feignConveyor);
        Long id = 10L;

        Credit credit = new Credit();

        Client client = new Client();
        client.setPassport(new Passport());
        client.setEmployment(new Employment());

        Application application = new Application();
        application.setCredit(credit);
        application.setClient(client);
        application.setStatusHistory(new ArrayList<>());
        try {

            Mockito.when(feignConveyor.calculating(Mockito.any())).thenThrow(NoSuchElementException.class);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            calculateService.putFinish(finishRegistrationRequestDTO, id);

        } catch (RuntimeException e) {
            assertEquals(BaseDataException.class, e.getClass());
        }
    }
    @Test
    void putFinishScoringException() {
        CalculateService calculateService = new CalculateService(applicationRepository, feignConveyor);
        Long id = 10L;

        Credit credit = new Credit();

        Client client = new Client();
        client.setPassport(new Passport());
        client.setEmployment(new Employment());

        Application application = new Application();
        application.setCredit(credit);
        application.setClient(client);
        application.setStatusHistory(new ArrayList<>());

        try {

            Mockito.when(feignConveyor.calculating(Mockito.any())).thenThrow(FeignException.class);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            calculateService.putFinish(finishRegistrationRequestDTO, id);

        } catch  (RuntimeException e) {
            assertEquals(ScoringException.class, e.getClass());
        }
    }

    @Test
    void putFinishConnectionException() {

        CalculateService calculateService = new CalculateService(applicationRepository, feignConveyor);
        Long id = 10L;

        Credit credit = new Credit();

        Client client = new Client();
        client.setPassport(new Passport());
        client.setEmployment(new Employment());

        Application application = new Application();
        application.setCredit(credit);
        application.setClient(client);
        application.setStatusHistory(new ArrayList<>());

        try {
            Mockito.when(feignConveyor.calculating(Mockito.any())).thenThrow(RetryableException.class);
            Mockito.when(applicationRepository.findById(id)).thenReturn(Optional.of(application));
            calculateService.putFinish(finishRegistrationRequestDTO, id);
        } catch (RuntimeException e) {
            assertEquals(ConnectionException.class, e.getClass());
        }
    }


}