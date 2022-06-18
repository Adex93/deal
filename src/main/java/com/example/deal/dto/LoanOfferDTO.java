package com.example.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class LoanOfferDTO {

    @NotNull
    @Schema(description = "Номер заявки (id)", example = "1")
    Long applicationId;

    @NotNull
    @Min(value = 10000, message = "Amount should be greater than 10000")
    @Schema(description = "Запрашиваемая денежная сумма", example = "2000000")
    BigDecimal requestedAmount;

    @NotNull
    @Min(value = 10000, message = "Amount should be greater than 10000")
    @Schema(description = "Итоговый размер тела кредита", example = "2060000")
    BigDecimal totalAmount;

    @NotNull
    @Min(value = 6, message = "Tern should be greater than 6")
    @Schema(description = "Срок кредита в месяцах", example = "24")
    Integer term;

    @NotNull
    @Schema(description = "Ежемесячный платеж", example = "100864.01")
    BigDecimal monthlyPayment;

    @NotNull
    @Schema(description = "Процентная ставка", example = "16")
    BigDecimal rate;

    @NotNull
    @Schema(description = "Приобретение страховки (true или false)", example = "true")
    Boolean isInsuranceEnabled;

    @NotNull
    @Schema(description = "Получаете ли зарплату в данном банке (true или false)", example = "true")
    Boolean isSalaryClient;

}
