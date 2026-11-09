package org.sid.accountserviceaxon.queries.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.accountserviceaxon.common.enums.AccountStatus;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String accountId;
    private String currency;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
