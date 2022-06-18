package com.example.deal.services;

import com.example.deal.controllers.FeignConveyor;
import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.FinishRegistrationRequestDTO;
import com.example.deal.dto.ScoringDataDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.entity.Credit;
import com.example.deal.enums.CreditStatus;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.myExceptions.ConnectionException;
import com.example.deal.myExceptions.ScoringException;
import com.example.deal.repositoryes.ApplicationRepository;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class CalculateService {

    final
    ApplicationRepository applicationRepository;

    final
    FeignConveyor feignConveyor;

    public CalculateService(ApplicationRepository applicationRepository, FeignConveyor feignConveyor) {
        this.applicationRepository = applicationRepository;
        this.feignConveyor = feignConveyor;
    }

    public void putFinish(FinishRegistrationRequestDTO finishRegistrationRequestDTO, Long id) {

        Application application = new Application();
        try {
            application = applicationRepository.findById(id).get();
            ScoringDataDTO scoringDataDTO = new ScoringDataDTO();
            Client client = application.getClient();
            Credit credit = application.getCredit();

            scoringDataDTO.setAmount(credit.getAmount());
            scoringDataDTO.setTerm(credit.getTerm());
            scoringDataDTO.setFirstName(client.getFirstName());
            scoringDataDTO.setLastName(client.getLastName());
            scoringDataDTO.setMiddleName(client.getMiddleName());
            scoringDataDTO.setGender(finishRegistrationRequestDTO.getGender());
            scoringDataDTO.setBirthdate(client.getBirthDate());
            scoringDataDTO.setPassportSeries(client.getPassport().getSeries());
            scoringDataDTO.setPassportNumber(client.getPassport().getNumber());
            scoringDataDTO.setPassportIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());
            scoringDataDTO.setPassportIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
            scoringDataDTO.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
            scoringDataDTO.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
            scoringDataDTO.setEmployment(finishRegistrationRequestDTO.getEmploymentDTO());
            scoringDataDTO.setAccount(finishRegistrationRequestDTO.getAccount());
            scoringDataDTO.setIsInsuranceEnabled(credit.getIsInsuranceEnabled());
            scoringDataDTO.setIsSalaryClient(credit.getIsSalaryClient());

            client.setAccount(finishRegistrationRequestDTO.getAccount());
            client.setDependentAmount(finishRegistrationRequestDTO.getDependentAmount());
            client.getEmployment().setEmploymentStatus(finishRegistrationRequestDTO.getEmploymentDTO().getEmploymentStatus());
            client.getEmployment().setEmployer(finishRegistrationRequestDTO.getEmploymentDTO().getEmployerINN());
            client.getEmployment().setSalary(finishRegistrationRequestDTO.getEmploymentDTO().getSalary());
            client.getEmployment().setPosition(finishRegistrationRequestDTO.getEmploymentDTO().getPosition());
            client.getEmployment().setWorkExperienceTotal(finishRegistrationRequestDTO.getEmploymentDTO().getWorkExperienceTotal());
            client.getEmployment().setWorkExperienceCurrent(finishRegistrationRequestDTO.getEmploymentDTO().getWorkExperienceCurrent());
            client.setGender(finishRegistrationRequestDTO.getGender());
            client.setMaritalStatus(finishRegistrationRequestDTO.getMaritalStatus());
            client.getPassport().setIssueBranch(finishRegistrationRequestDTO.getPassportIssueBranch());
            client.getPassport().setIssueDate(finishRegistrationRequestDTO.getPassportIssueDate());

            log.info("Произведен POST запрос на /conveyor/calculation MC conveyor");
            CreditDTO creditDTO = feignConveyor.calculating(scoringDataDTO).getBody();

            credit.setAmount(creditDTO.getAmount());
            credit.setMonthlyPayment(creditDTO.getMonthlyPayment());
            credit.setRate(creditDTO.getRate());
            credit.setPsk(creditDTO.getPsk());
            credit.setPaymentSchedule(creditDTO.getPaymentSchedule());
            credit.setCreditStatus(CreditStatus.CALCULATED);

            application.setStatus(Status.CC_APPROVED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.CC_APPROVED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED));
            application.setCredit(credit);
            application.setClient(client);
            applicationRepository.save(application);

            log.info("В базе данных сохранена следующая информация: Application: " + application + ", Client: " + client + ", Credit: " + credit);
        }
        catch (RetryableException e) {
            log.error("Отсутствует подключение к микросервису Credit Conveyor");
            application.setStatus(Status.CC_DENIED);
            application.setStatus(Status.CC_DENIED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.CC_DENIED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED));
            applicationRepository.save(application);
            throw new ConnectionException("Отсутствует подключение к микросервису Credit Conveyor");
        }
        catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }
        catch (FeignException e) {
            log.error("Cкорринг не пройден");
            application.setStatus(Status.CC_DENIED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.CC_DENIED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.APPROVED));
            applicationRepository.save(application);
            throw new ScoringException("Cкоринг не пройден");
        }
    }

}