package org.sid.accountserviceaxon.common.events;

import lombok.Getter;

import java.math.BigDecimal;

public class AccountDebitedEvent extends BaseEvent<String> {
    @Getter private final String currency;
    @Getter private final BigDecimal amount;

    public AccountDebitedEvent(String id, String currency, BigDecimal amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
