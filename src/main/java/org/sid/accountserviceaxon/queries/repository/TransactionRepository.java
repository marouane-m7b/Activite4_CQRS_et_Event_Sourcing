package org.sid.accountserviceaxon.queries.repository;

import org.sid.accountserviceaxon.queries.entities.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {
    List<AccountTransaction> findByAccountAccountId(String accountId);
}
