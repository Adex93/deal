package com.example.deal.entity;

import com.example.deal.dto.ApplicationStatusHistoryDTO;
import com.example.deal.dto.LoanOfferDTO;
import com.example.deal.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Client client;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Credit credit;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Status status;

    LocalDate creationDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    LoanOfferDTO appliedOffer;

    LocalDate signDate;

    Integer sesCode;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    List<ApplicationStatusHistoryDTO> statusHistory;

}
