package org.sid.accountserviceaxon.common.commands;

import lombok.Getter;

import java.math.BigDecimal;

public class CreateAccountCommand extends BaseCommand<String> {
    @Getter private final String currency;
    @Getter private final BigDecimal initialBalance;

    public CreateAccountCommand(String id, String currency, BigDecimal initialBalance) {
        super(id);
        this.currency = currency;
        this.initialBalance = initialBalance;
    }
}
