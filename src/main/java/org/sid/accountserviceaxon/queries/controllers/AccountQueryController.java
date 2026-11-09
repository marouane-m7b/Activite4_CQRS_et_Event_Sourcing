package org.sid.accountserviceaxon.queries.controllers;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.sid.accountserviceaxon.queries.entities.Account;
import org.sid.accountserviceaxon.queries.entities.AccountTransaction;
import org.sid.accountserviceaxon.queries.queries.GetAccountById;
import org.sid.accountserviceaxon.queries.queries.GetAccountTransactions;
import org.sid.accountserviceaxon.queries.queries.GetAllAccounts;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/query")
public class AccountQueryController {
    private final QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/accounts/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountById(accountId), Account.class).join();
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return queryGateway.query(new GetAllAccounts(),
                ResponseTypes.multipleInstancesOf(Account.class)).join();
    }

    @GetMapping("/accountOperations/{accountId}")
    public List<AccountTransaction> getAccountTransactions(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountTransactions(accountId),
                ResponseTypes.multipleInstancesOf(AccountTransaction.class)).join();
    }

    @GetMapping(value = "/{accountId}/watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Account> watchAccount(@PathVariable String accountId) {
        SubscriptionQueryResult<Account, Account> result = queryGateway.subscriptionQuery(
                new GetAccountById(accountId),
                ResponseTypes.instanceOf(Account.class),
                ResponseTypes.instanceOf(Account.class));
        return result.initialResult().concatWith(result.updates());
    }
}
