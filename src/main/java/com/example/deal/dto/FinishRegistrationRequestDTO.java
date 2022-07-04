package com.example.deal.dto;

import com.example.deal.enums.Gender;
import com.example.deal.enums.MaritalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class FinishRegistrationRequestDTO {

    @NotNull
    @Schema(description = "Пол (MALE, FEMALE,или NON_BINARY)", example = "MALE")
    Gender gender;

    @NotNull
    @Schema(description = "Семейное положение (SINGLE, MARRIED, DIVORCED или WIDOW_WIDOWER)", example = "SINGLE")
    MaritalStatus maritalStatus;

    @NotNull
    @Min(value = 0, message = "DependentAmount is uncorrected")
    @Schema(description = "Количесво иждивенцев", example = "0")
    Integer dependentAmount;

    @NotNull
    @PastOrPresent(message = "PassportIssueDate is uncorrected")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "Дата выдачи паспорта", example = "2015-05-15")
    LocalDate passportIssueDate;

    @NotBlank
    @Schema(description = "Отделение выдачи паспорта", example = "360-018")
    String passportIssueBranch;

    @NotNull
    EmploymentDTO employmentDTO;

    @Pattern(regexp = "^\\d+$", message = "Account is uncorrected")
    @NotBlank
    @Schema(description = "Номер счета", example = "40702810400000123456")
    String account;
}
