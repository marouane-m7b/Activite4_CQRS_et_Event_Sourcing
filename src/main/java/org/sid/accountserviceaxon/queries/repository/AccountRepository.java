package org.sid.accountserviceaxon.queries.repository;

import org.sid.accountserviceaxon.queries.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
