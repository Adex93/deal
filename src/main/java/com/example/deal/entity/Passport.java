package com.example.deal.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class Passport {

    String series;

    String number;

    LocalDate issueDate;

    String issueBranch;
}
