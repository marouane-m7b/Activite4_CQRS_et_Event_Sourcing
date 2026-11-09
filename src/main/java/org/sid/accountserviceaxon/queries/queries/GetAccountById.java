package org.sid.accountserviceaxon.queries.queries;

import lombok.Getter;

@Getter
public class GetAccountById {
    private final String accountId;

    public GetAccountById(String accountId) {
        this.accountId = accountId;
    }
}
