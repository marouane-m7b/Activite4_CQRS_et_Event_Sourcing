package org.sid.accountserviceaxon.queries.services;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.sid.accountserviceaxon.common.enums.TransactionType;
import org.sid.accountserviceaxon.common.events.AccountCreatedEvent;
import org.sid.accountserviceaxon.common.events.AccountCreditedEvent;
import org.sid.accountserviceaxon.common.events.AccountDebitedEvent;
import org.sid.accountserviceaxon.queries.entities.Account;
import org.sid.accountserviceaxon.queries.entities.AccountTransaction;
import org.sid.accountserviceaxon.queries.queries.GetAccountById;
import org.sid.accountserviceaxon.queries.queries.GetAccountTransactions;
import org.sid.accountserviceaxon.queries.queries.GetAllAccounts;
import org.sid.accountserviceaxon.queries.repository.AccountRepository;
import org.sid.accountserviceaxon.queries.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class AccountEventHandlerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final QueryUpdateEmitter queryUpdateEmitter;

    public AccountEventHandlerService(AccountRepository accountRepository,
                                      TransactionRepository transactionRepository,
                                      QueryUpdateEmitter queryUpdateEmitter) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.queryUpdateEmitter = queryUpdateEmitter;
    }

    @EventHandler
    @Transactional
    public void on(AccountCreatedEvent event) {
        accountRepository.save(new Account(
                event.getId(), event.getCurrency(), event.getBalance(), event.getStatus()));
    }

    @EventHandler
    @Transactional
    public void on(AccountCreditedEvent event) {
        Account account = getAccount(event.getId());
        account.setBalance(account.getBalance().add(event.getAmount()));
        Account saved = accountRepository.save(account);
        saveTransaction(saved, event.getAmount(), TransactionType.CREDIT);
        emitAccountUpdate(event.getId(), saved);
    }

    @EventHandler
    @Transactional
    public void on(AccountDebitedEvent event) {
        Account account = getAccount(event.getId());
        account.setBalance(account.getBalance().subtract(event.getAmount()));
        Account saved = accountRepository.save(account);
        saveTransaction(saved, event.getAmount(), TransactionType.DEBIT);
        emitAccountUpdate(event.getId(), saved);
    }

    @QueryHandler
    public Account on(GetAccountById query) {
        return getAccount(query.getAccountId());
    }

    @QueryHandler
    public List<Account> on(GetAllAccounts query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public List<AccountTransaction> on(GetAccountTransactions query) {
        return transactionRepository.findByAccountAccountId(query.getAccountId());
    }

    private Account getAccount(String accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }

    private void saveTransaction(Account account, java.math.BigDecimal amount, TransactionType type) {
        transactionRepository.save(new AccountTransaction(null, new Date(), amount, type, account));
    }

    private void emitAccountUpdate(String accountId, Account account) {
        queryUpdateEmitter.emit(
                message -> message.getPayload() instanceof GetAccountById
                        && ((GetAccountById) message.getPayload()).getAccountId().equals(accountId),
                account);
    }
}
