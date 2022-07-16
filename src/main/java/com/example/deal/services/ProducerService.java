package com.example.deal.services;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.EmailMessage;
import com.example.deal.entity.Application;
import com.example.deal.entity.Theme;
import com.example.deal.enums.CreditStatus;
import com.example.deal.enums.Status;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.myExceptions.SesCodeException;
import com.example.deal.repositoryes.ApplicationRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Objects;

@Slf4j
@Service
@Getter
@Setter
public class ProducerService {

    final
    ApplicationRepository applicationRepository;

    final
    KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, EmailMessage> kafkaTemplate, ApplicationRepository applicationRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationRepository = applicationRepository;
    }


    public void sendFinishRegistration(Long id) {
        try {
            Application application = applicationRepository.findById(id).get();
            EmailMessage message = new EmailMessage();
            message.setAddress(application.getClient().getEmail());
            message.setTheme(Theme.FINISH_REGISTRATION);
            message.setApplicationId(application.getId());

            log.info("В топик finish-registration отправлено сообщение: " + message);
            kafkaTemplate.send("finish-registration", message);
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }

    }

    public void sendCreateDocuments(Long id) {
        try {
            Application application = applicationRepository.findById(id).get();
            EmailMessage message = new EmailMessage();
            message.setAddress(application.getClient().getEmail());
            message.setTheme(Theme.CREATE_DOCUMENTS);
            message.setApplicationId(application.getId());

            log.info("В топик create-documents отправлено сообщение: " + message);
            kafkaTemplate.send("create-documents", message);
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }

    }

    public void sendDocuments(Long id) {
        try {
            Application application = applicationRepository.findById(id).get();
            EmailMessage message = new EmailMessage();
            message.setAddress(application.getClient().getEmail());
            message.setTheme(Theme.SEND_DOCUMENTS);
            message.setApplicationId(id);

            log.info("В топик send-documents отправлено сообщение: " + message);
            kafkaTemplate.send("send-documents", message);
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }

    }

    public void updateStatus(Long id) {
        Application application = applicationRepository.findById(id).get();
        application.setStatus(Status.DOCUMENT_CREATED);
        application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.DOCUMENT_CREATED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.CC_APPROVED));
        applicationRepository.save(application);
        log.info("Обновлен статус заявки с  id: " + id);
    }

    public Application getApplication(Long id) {
        log.info("Из базы данных извлечена заявка с  id: " + id);
        return applicationRepository.findById(id).get();
    }

    public void generateSES(Long id) {
        try {
            Application application = applicationRepository.findById(id).get();

            int min = 1000;
            int max = 9999;
            max -= min;
            Integer code = (int) (Math.random() * ++max) + min;
            application.setSesCode(code);
            applicationRepository.save(application);
            log.info("Сгенирован Ses код заявки " + id + " - " + code);

            EmailMessage message = new EmailMessage();
            message.setAddress(application.getClient().getEmail());
            message.setTheme(Theme.SEND_SES);
            message.setApplicationId(id);

            log.info("В топик send-ses отправлено сообщение: " + message);
            kafkaTemplate.send("send-ses", message);
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }

    }

    public void enterSES(Long id, Integer code) {
        try {
            Application application = applicationRepository.findById(id).get();
            if (Objects.equals(code, application.getSesCode())) {

                application.setStatus(Status.DOCUMENT_SIGNED);
                application.setSignDate(LocalDate.now());
                application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.DOCUMENT_SIGNED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.DOCUMENT_CREATED));
                applicationRepository.save(application);

                EmailMessage message = new EmailMessage();
                message.setAddress(application.getClient().getEmail());
                message.setTheme(Theme.CREDIT_ISSUED);
                message.setApplicationId(id);

                log.info("В топик credit-issued отправлено сообщение: " + message);
                kafkaTemplate.send("credit-issued", message);

                application.setStatus(Status.CREDIT_ISSUED);
                application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.CREDIT_ISSUED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), Status.DOCUMENT_SIGNED));
                application.getCredit().setCreditStatus(CreditStatus.ISSUED);
                applicationRepository.save(application);
            } else throw new SesCodeException("Введен неверный код! Попробуйте еще раз");
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }

    }

    public void clientDenied(Long id) {
        try {
            Application application = applicationRepository.findById(id).get();
            application.setStatus(Status.CLIENT_DENIED);
            application.getStatusHistory().add(new ApplicationStatusHistoryDTO(Status.CREDIT_ISSUED, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS), application.getStatus()));
            applicationRepository.save(application);

            EmailMessage message = new EmailMessage();
            message.setAddress(application.getClient().getEmail());
            message.setTheme(Theme.APPLICATION_DENIED);
            message.setApplicationId(id);

            log.info("В топик application-denied отправлено сообщение: " + message);
            kafkaTemplate.send("application-denied", message);
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }
    }
}
