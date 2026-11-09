package org.sid.accountserviceaxon.common.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitAccountRequestDTO {
    private String accountId;
    private String currency;
    private BigDecimal amount;
}
