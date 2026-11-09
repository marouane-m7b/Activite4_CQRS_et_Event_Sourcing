package org.sid.accountserviceaxon.common.events;

import lombok.Getter;
import org.sid.accountserviceaxon.common.enums.AccountStatus;

import java.math.BigDecimal;

public class AccountCreatedEvent extends BaseEvent<String> {
    @Getter private final String currency;
    @Getter private final BigDecimal balance;
    @Getter private final AccountStatus status;

    public AccountCreatedEvent(String id, String currency, BigDecimal balance, AccountStatus status) {
        super(id);
        this.currency = currency;
        this.balance = balance;
        this.status = status;
    }
}
