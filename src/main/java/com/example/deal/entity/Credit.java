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
@SequenceGenerator(name = "credit_id_seq", initialValue = 1, allocationSize = 1)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_id_seq")
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
