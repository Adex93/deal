package com.example.deal.dto;

import com.example.deal.enums.Gender;
import com.example.deal.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ScoringDataDTO {

    @NotNull
    @Min(value = 10000, message = "Amount should be greater than 10000")
    @Schema(description = "Запрашиваемая сумма денежных средств", example = "2000000")
    BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Tern should be greater than 6")
    @Schema(description = "Срок кредита в месяцах", example = "24")
    Integer term;

    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "FirstName should be between 2 and 30 characters")
    @NotBlank(message = "FirstName should not be empty")
    @Schema(description = "Имя", example = "Aleksandr")
    String firstName;

    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "lastName should be between 2 and 30 characters")
    @NotBlank(message = "lastName should not be empty")
    @Schema(description = "Фамилия", example = "Dmitriev")
    String lastName;

    @Pattern(regexp = "^[A-Za-z]+$")
    @Size(min = 2, max = 30, message = "MiddleName should be between 2 and 30 characters or be Null")
    @Schema(description = "Отчество (при наличии)", example = "Sergeevich")
    String middleName;

    @NotNull
    @Schema(description = "Пол (MALE или FEMALE)", example = "MALE")
    Gender gender;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата рождения", example = "1993-07-28")
    LocalDate birthdate;

    @NotBlank
    @Size(min = 4, max = 4, message = "PassportSeries is uncorrected")
    @Schema(description = "Серия паспорта", example = "1234")
    String passportSeries;

    @NotBlank
    @Size(min = 6, max = 6, message = "PassportNumber is uncorrected")
    @Schema(description = "Номер паспорта", example = "123456")
    String passportNumber;

    @NotNull
    @PastOrPresent(message = "PassportIssueDate is uncorrected")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата выдачи паспорта", example = "2015-05-15")
    LocalDate passportIssueDate;

    @NotBlank
    @Schema(description = "Отделение выдачи паспорта", example = "360-018")
    String passportIssueBranch;


    @NotNull
    @Schema(description = "Семейное положение(MARRIED или SINGLE)", example = "SINGLE")
    MaritalStatus maritalStatus;

    @NotNull
    @Min(value = 0, message = "DependentAmount is uncorrected")
    @Schema(description = "Количесво иждивенцев", example = "0")
    Integer dependentAmount;

    @NotNull
    @Valid
    EmploymentDTO employment;

    @Pattern(regexp = "^\\d+$", message = "Account is uncorrected")
    @NotBlank
    @Schema(description = "Номер счета", example = "40702810400000123456")
    String account;

    @NotNull
    @Schema(description = "Покупается ли страховка (true или false)", example = "true")
    Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Зарплатный ли клиент (true или false)", example = "true")
    Boolean isSalaryClient;

}
