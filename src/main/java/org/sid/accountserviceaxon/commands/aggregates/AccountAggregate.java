package org.sid.accountserviceaxon.commands.aggregates;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.sid.accountserviceaxon.common.commands.CreateAccountCommand;
import org.sid.accountserviceaxon.common.commands.CreditAccountCommand;
import org.sid.accountserviceaxon.common.commands.DebitAccountCommand;
import org.sid.accountserviceaxon.common.enums.AccountStatus;
import org.sid.accountserviceaxon.common.events.AccountCreatedEvent;
import org.sid.accountserviceaxon.common.events.AccountCreditedEvent;
import org.sid.accountserviceaxon.common.events.AccountDebitedEvent;
import org.sid.accountserviceaxon.common.exceptions.NegativeInitialBalanceException;

import java.math.BigDecimal;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private String currency;
    private BigDecimal balance;
    private AccountStatus status;

    public AccountAggregate() {
        // Required by Axon when restoring an aggregate from its event stream.
    }

    @CommandHandler
    public AccountAggregate(CreateAccountCommand command) {
        log.info("CreateAccountCommand received");
        if (command.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInitialBalanceException("Negative balance");
        }
        AggregateLifecycle.apply(new AccountCreatedEvent(
                command.getId(), command.getCurrency(), command.getInitialBalance(), AccountStatus.CREATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("AccountCreatedEvent sourced");
        this.accountId = event.getId();
        this.currency = event.getCurrency();
        this.balance = event.getBalance();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("CreditAccountCommand received");
        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInitialBalanceException("Negative amount");
        }
        AggregateLifecycle.apply(new AccountCreditedEvent(
                command.getId(), command.getCurrency(), command.getAmount()));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("AccountCreditedEvent sourced");
        this.balance = this.balance.add(event.getAmount());
    }

    @CommandHandler
    public void handle(DebitAccountCommand command) {
        log.info("DebitAccountCommand received");
        if (command.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativeInitialBalanceException("Negative amount");
        }
        if (command.getAmount().compareTo(this.balance) > 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        AggregateLifecycle.apply(new AccountDebitedEvent(
                command.getId(), command.getCurrency(), command.getAmount()));
    }

    @EventSourcingHandler
    public void on(AccountDebitedEvent event) {
        log.info("AccountDebitedEvent sourced");
        this.balance = this.balance.subtract(event.getAmount());
    }
}
