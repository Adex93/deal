package com.example.deal.dto;

import com.example.deal.enums.EmploymentStatus;
import com.example.deal.enums.Position;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;


@Getter
@Setter
@ToString
public class EmploymentDTO {

    @NotNull
    EmploymentStatus employmentStatus;

    @Pattern(regexp = "^\\d+$", message = "EmployerINN is uncorrected")
    @NotBlank
    @Schema(description = "ИНН", example = "3664240541")
    String employerINN;

    @NotNull
    @Schema(description = "Зарплата", example = "100000")
    BigDecimal salary;

    @NotNull
    @Schema(description = "Должность (WORKER или MID_MANAGER, TOP_MANAGER WORKER или OWNER)", example = "MID_MANAGER")
    Position position;

    @NotNull
    @Schema(description = "Общий стаж работы в месяцах", example = "72")
    Integer workExperienceTotal;

    @NotNull
    @Schema(description = "Стаж работы на последнем рабочем месте в месяцах", example = "24")
    Integer workExperienceCurrent;


}


