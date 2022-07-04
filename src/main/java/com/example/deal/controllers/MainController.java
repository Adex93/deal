package com.example.deal.controllers;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.CalculateService;
import com.example.deal.services.OfferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
public class MainController {

    final
    ApplicationService applicationService;

    final
    OfferService offerService;

    final
    CalculateService calculateService;

    public MainController(ApplicationService applicationService, OfferService offerService, CalculateService calculateService) throws RuntimeException {
        this.applicationService = applicationService;
        this.offerService = offerService;
        this.calculateService = calculateService;
    }

    @Tag(name = "The application API", description = "Create 4 LoanOfferDTO")
    @PostMapping("/deal/application")
    public ResponseEntity<List<LoanOfferDTO>> addNewApplication(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {

        log.info("Произвёлся вызов application API со следующими входными данными: " + loanApplicationRequestDTO);
        log.info("Вызвана функция addDBandResponseListLoanDTO класса ApplicationService для создания и сохранения в базе данных сущностей Application и Client и формирования списка с кредитными предложениями LoanOffersDTO");
        return new ResponseEntity<>(applicationService.getResponseListLoanDTO(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @Tag(name = "The offer API", description = "Put LoanOfferDTO")
    @PutMapping("/deal/offer")
    public void addClientOffer(@Valid @RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("Произвёлся вызов offer API со следующими входными данными: " + loanOfferDTO);
        log.info("Вызвана функция putOffer класса OfferService для обновления в базе данных сущностей Application, Client и Credit на основании выбранного кредитного предложения LoanOffersDTO");
        offerService.putOffer(loanOfferDTO);
    }

    @Tag(name = "The calculate API", description = "Finish Registration")
    @PutMapping("/deal/calculate/{applicationId}")
    public void addFinishDTO(@PathVariable(name = "applicationId") Long applicationId, @Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("Произвёлся вызов calculate API со следующими входными данными: " + finishRegistrationRequestDTO + ", applicationId: " + applicationId);
        log.info("Вызвана функция putFinish класса CalculateService для завершения регистрации и полного подсчёта кредита");
        calculateService.putFinish(finishRegistrationRequestDTO, applicationId);
    }
}
