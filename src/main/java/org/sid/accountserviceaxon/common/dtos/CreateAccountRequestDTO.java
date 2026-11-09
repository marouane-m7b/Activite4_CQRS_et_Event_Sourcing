package org.sid.accountserviceaxon.common.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountRequestDTO {
    private String currency;
    private BigDecimal initialBalance;
}
