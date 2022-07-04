package com.example.deal.services;

import com.example.deal.clients.FeignConveyor;
import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.entity.Application;
import com.example.deal.entity.Client;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.ConnectionException;
import com.example.deal.myExceptions.ScoringException;
import com.example.deal.repositoryes.ApplicationRepository;
import com.example.deal.repositoryes.ClientRepository;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ApplicationService {

    final
    ClientRepository clientRepository;
    final
    ApplicationRepository applicationRepository;
    final
    FeignConveyor feignConveyor;

    public ApplicationService(ClientRepository clientRepository, ApplicationRepository applicationRepository, FeignConveyor feignConveyor) {
        this.clientRepository = clientRepository;
        this.applicationRepository = applicationRepository;
        this.feignConveyor = feignConveyor;
    }

    public List<LoanOfferDTO> getResponseListLoanDTO(LoanApplicationRequestDTO loanApplicationRequestDTOBody) {

        Client client = new Client(loanApplicationRequestDTOBody);
        Application application = new Application();
        application.setCreationDate(LocalDate.now());
        application.setClient(client);
        List<ApplicationStatusHistoryDTO> applicationStatusHistoryDTOList = new ArrayList<>();
        applicationRepository.save(application);

        try {
            log.info("Произведен POST запрос на /conveyor/offers MC conveyor");
            List<LoanOfferDTO> list = feignConveyor.addNewOffer(loanApplicationRequestDTOBody).getBody();
            for (LoanOfferDTO loan : list
            ) {
                loan.setApplicationId(application.getId());
            }
            log.info("Элементам списка List<LoanOfferDTO> присвоен id " + application.getId());
            application.setStatus(Status.PREAPPROVAL);
            applicationStatusHistoryDTOList.add(new ApplicationStatusHistoryDTO(Status.PREAPPROVAL, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), null));
            application.setStatusHistory(applicationStatusHistoryDTOList);
            applicationRepository.save(application);
            log.info("В базе данных сохранена следующая информация: " + application + ", Client: " + client);
            log.info("Список с экземплярами LoanOfferDTO успешно создан: " + list);
            return list;

        } catch (RetryableException e) {
            log.error("Отсутствует подключение к микросервису Credit Conveyor");
            application.setStatus(Status.CC_DENIED);
            applicationStatusHistoryDTOList.add(new ApplicationStatusHistoryDTO(Status.CC_DENIED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.PREAPPROVAL));
            applicationRepository.save(application);
            throw new ConnectionException("Отсутствует подключение к микросервису Credit Conveyor");
        } catch (FeignException e) {
            log.error("Прескоринг не пройден");
            application.setStatus(Status.CC_DENIED);
            applicationStatusHistoryDTOList.add(new ApplicationStatusHistoryDTO(Status.CC_DENIED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.PREAPPROVAL));
            applicationRepository.save(application);
            throw new ScoringException("Прескоринг не пройден");
        }
    }

}
