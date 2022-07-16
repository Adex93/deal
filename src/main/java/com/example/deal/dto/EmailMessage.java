package com.example.deal.dto;

import com.example.deal.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmailMessage {

    String address;
    Theme theme;
    Long applicationId;


}
