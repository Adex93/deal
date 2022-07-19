package com.example.deal.services;

import com.example.deal.entity.Application;
import com.example.deal.myExceptions.BaseDataException;
import com.example.deal.repositoryes.ApplicationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class AdminService {

    final
    ApplicationRepository applicationRepository;

    public AdminService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Application getApplicationById(Long id) {
        try {
            log.info("Из базы данных извлечена заявка с  id: " + id);
            return applicationRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            log.error("Заявка с applicationId = " + id + " в базе данных отсутствует");
            throw new BaseDataException("Заявка с applicationId = " + id + " в базе данных отсутствует");
        }
    }

    public List<Application> getAllApplications() {

        List<Application> list = new ArrayList<>();
        Iterator<Application> iterator = applicationRepository.findAll().iterator();
        iterator.forEachRemaining(list::add);
        log.info("Из базы данных извлечены все заявки");
        return list;
    }

}
