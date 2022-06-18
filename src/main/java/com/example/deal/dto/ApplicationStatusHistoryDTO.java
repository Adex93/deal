package com.example.deal.dto;

import com.example.deal.enums.Status;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.TypeDef;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ApplicationStatusHistoryDTO implements Serializable {
    Status status;
    LocalDateTime time;
    Status changeType;

    public ApplicationStatusHistoryDTO(Status status, LocalDateTime time, Status changeType) {
        this.status = status;
        this.time = time;
        this.changeType = changeType;
    }


}
