package com.example.deal.clients;

import com.example.deal.dto.CreditDTO;
import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.dto.ScoringDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "feignOffers", url = "${custom.deal.clients.url}")
public interface FeignConveyor {

    @PostMapping(value = "/offers")
    ResponseEntity<List<LoanOfferDTO>> addNewOffer(@Valid @RequestBody LoanApplicationRequestDTO loanApplicationRequestDTOBody);

    @PostMapping(value = "/calculation")
    ResponseEntity<CreditDTO> calculating(@Valid @RequestBody ScoringDataDTO scoringDataDTO);

}


