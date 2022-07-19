package com.example.deal.services;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Credit;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.repositoryes.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class OfferService {

    final
    ApplicationRepository applicationRepository;

    final
    ProducerService producerService;

    public OfferService(ApplicationRepository applicationRepository, ProducerService producerService) {
        this.applicationRepository = applicationRepository;
        this.producerService = producerService;
    }

    public void putOffer(LoanOfferDTO loanOfferDTO) {

        try {
            Application application = applicationRepository.findById(loanOfferDTO.getApplicationId()).get();
            application.setAppliedOffer(loanOfferDTO);
            application.setStatus(Status.APPROVED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.APPROVED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.PREAPPROVAL));

            Credit credit = new Credit();
            credit.setAmount(loanOfferDTO.getRequestedAmount());
            credit.setTerm(loanOfferDTO.getTerm());
            credit.setIsInsuranceEnabled(loanOfferDTO.getIsInsuranceEnabled());
            credit.setIsSalaryClient(loanOfferDTO.getIsSalaryClient());
            application.setCredit(credit);

            applicationRepository.save(application);
            log.info("В базе данных сохранена следующая информация: " + application + ", " + credit);

            producerService.sendFinishRegistration(application.getId());
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + loanOfferDTO.getApplicationId() + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + loanOfferDTO.getApplicationId() + " в базе данных отсутствует");



        }
    }

}
