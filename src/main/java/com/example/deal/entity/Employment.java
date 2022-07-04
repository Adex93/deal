package com.example.deal.entity;

import com.example.deal.enums.EmploymentStatus;
import com.example.deal.enums.Position;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Employment {

    EmploymentStatus employmentStatus;

    String employer;

    BigDecimal salary;

    Position position;

    Integer workExperienceTotal;

    Integer workExperienceCurrent;
}
