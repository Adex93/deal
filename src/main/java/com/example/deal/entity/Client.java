package com.example.deal.entity;

import com.example.deal.dto.LoanApplicationRequestDTO;
import com.example.deal.enums.Gender;
import com.example.deal.enums.MaritalStatus;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String lastName;

    String firstName;

    String middleName;

    LocalDate birthDate;

    String email;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Gender gender;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    MaritalStatus maritalStatus;

    Integer dependentAmount;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Passport passport;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    Employment employment;

    String account;

    public Client() {
    }

    public Client(LoanApplicationRequestDTO loanApplicationRequestDTO) {

        Passport passport = new Passport();
        passport.setSeries(loanApplicationRequestDTO.getPassportSeries());
        passport.setNumber(loanApplicationRequestDTO.getPassportNumber());

        this.employment = new Employment();

        this.lastName = loanApplicationRequestDTO.getLastName();
        this.firstName = loanApplicationRequestDTO.getFirstName();
        this.middleName = loanApplicationRequestDTO.getMiddleName();
        this.birthDate = loanApplicationRequestDTO.getBirthdate();
        this.email = loanApplicationRequestDTO.getEmail();
        this.passport = passport;
    }
}
