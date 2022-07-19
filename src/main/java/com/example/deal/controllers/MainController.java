package com.example.deal.controllers;

import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.services.ApplicationService;
import com.example.deal.services.CalculateService;
import com.example.deal.services.OfferService;
import com.example.deal.services.ProducerService;
import io.swagger.v3.oas.annotations.Operation;
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

    final
    ProducerService producerService;

    public MainController(ApplicationService applicationService, OfferService offerService, CalculateService calculateService, ProducerService producerService) throws RuntimeException {
        this.applicationService = applicationService;
        this.offerService = offerService;
        this.calculateService = calculateService;
        this.producerService = producerService;
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Create 4 LoanOfferDTO")
    @PostMapping("/deal/application")
    public ResponseEntity<List<LoanOfferDTO>> addNewApplication(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTO) {
        log.info("Произвёлся вызов /deal/application со следующими входными данными: " + loanApplicationRequestDTO);
        log.info("Вызвана функция addDBandResponseListLoanDTO класса ApplicationService для создания и сохранения в базе данных сущностей Application и Client и формирования списка с кредитными предложениями LoanOffersDTO");
        return new ResponseEntity<>(applicationService.getResponseListLoanDTO(loanApplicationRequestDTO), HttpStatus.OK);
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Put LoanOfferDTO")
    @PutMapping("/deal/offer")
    public void addClientOffer(@Valid @RequestBody LoanOfferDTO loanOfferDTO) {
        log.info("Произвёлся вызов /deal/offer со следующими входными данными: " + loanOfferDTO);
        log.info("Вызвана функция putOffer класса OfferService для обновления в базе данных сущностей Application, Client и Credit на основании выбранного кредитного предложения LoanOffersDTO");
        offerService.putOffer(loanOfferDTO);
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Finish Registration")
    @PutMapping("/deal/calculate/{applicationId}")
    public void addFinishDTO(@PathVariable(name = "applicationId") Long applicationId, @Valid @RequestBody FinishRegistrationRequestDTO finishRegistrationRequestDTO) {
        log.info("Произвёлся вызов /deal/calculate/{applicationId} со следующими входными данными: " + finishRegistrationRequestDTO + ", applicationId: " + applicationId);
        log.info("Вызвана функция putFinish класса CalculateService для завершения регистрации и полного подсчёта кредита");
        calculateService.putFinish(finishRegistrationRequestDTO, applicationId);
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Create documents")
    @PostMapping("/deal/document/{applicationId}/send")
    public void createDocuments(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("Произвёлся вызов /deal/document/{applicationId}/send с applicationId: " + applicationId);
        log.info("Вызвана функция sendDocuments класса ProducerService для осуществления запроса на отправку документов");
        producerService.sendDocuments(applicationId);
    }

    @Tag(name = "The deal API(Admin)")
    @Operation(summary = "Update status Application (for dossier)")
    @PutMapping("/deal/admin/application/{applicationId}/status")
    public void updateStatus(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("Произвёлся вызов /deal/admin/application/{applicationId}/status с applicationId: " + applicationId);
        log.info("Вызвана функция updateStatus класса ProducerService для обновления статуса заявки");
        producerService.updateStatus(applicationId);
    }

    @Tag(name = "The deal API(Admin)")
    @Operation(summary = "Get Application by Id")
    @GetMapping("/deal/admin/application/{applicationId}")
    public ResponseEntity<Application> getApplication(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("Произвёлся вызов /deal/admin/application/{applicationId}/getApplication с applicationId: " + applicationId);
        log.info("Вызвана функция getApplication класса ProducerService для обновления статуса заявки");
        return new ResponseEntity<>(producerService.getApplication(applicationId), HttpStatus.OK);
    }


    @Tag(name = "The deal API")
    @Operation(summary = "Send SES-code")
    @PostMapping("/deal/document/{applicationId}/sign")
    public void createSES(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("Произвёлся вызов /deal/document/{applicationId}/sign с applicationId: " + applicationId);
        log.info("Вызвана функция generateSES класса ProducerService для осуществления запроса на подписание документов");
        producerService.generateSES(applicationId);
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Enter SES-code")
    @PostMapping("/deal/document/{applicationId}/code")
    public void enterSES(@PathVariable(name = "applicationId") Long applicationId, @RequestBody Integer code) {
        log.info("Произвёлся вызов /deal/document/{applicationId}/code с applicationId: " + applicationId + ", sesCode: " + code);
        log.info("Вызвана функция enterSES класса ProducerService для осуществления подписания документов");
        producerService.enterSES(applicationId, code);
    }

    @Tag(name = "The deal API")
    @Operation(summary = "Denied a credit")
    @PostMapping("/deal/document/{applicationId}/denied")
    public void deniedCredit(@PathVariable(name = "applicationId") Long applicationId) {
        log.info("Произвёлся вызов /deal/document/{applicationId}/denied с applicationId: " + applicationId);
        log.info("Вызвана функция clientDenied класса ProducerService для осуществления отказа клиента от кредита");
        producerService.clientDenied(applicationId);
    }
}
