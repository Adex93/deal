package com.example.deal.entity;

import com.example.deal.dto.PaymentScheduleElement;
import com.example.deal.enums.CreditStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    BigDecimal amount;

    Integer term;

    BigDecimal monthlyPayment;

    BigDecimal rate;

    BigDecimal psk;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    List<PaymentScheduleElement> paymentSchedule;

    Boolean isInsuranceEnabled;

    Boolean isSalaryClient;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    CreditStatus creditStatus;


}
