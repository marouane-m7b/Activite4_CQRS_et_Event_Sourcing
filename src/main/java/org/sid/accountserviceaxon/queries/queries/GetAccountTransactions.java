package org.sid.accountserviceaxon.queries.queries;

import lombok.Getter;

@Getter
public class GetAccountTransactions {
    private final String accountId;

    public GetAccountTransactions(String accountId) {
        this.accountId = accountId;
    }
}
