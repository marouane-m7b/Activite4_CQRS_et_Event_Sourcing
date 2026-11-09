package org.sid.accountserviceaxon.common.commands;

import lombok.Getter;

import java.math.BigDecimal;

public class CreditAccountCommand extends BaseCommand<String> {
    @Getter private final String currency;
    @Getter private final BigDecimal amount;

    public CreditAccountCommand(String id, String currency, BigDecimal amount) {
        super(id);
        this.currency = currency;
        this.amount = amount;
    }
}
