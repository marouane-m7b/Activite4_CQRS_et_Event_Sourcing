package org.sid.accountserviceaxon.commands.controllers;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.sid.accountserviceaxon.common.commands.CreateAccountCommand;
import org.sid.accountserviceaxon.common.commands.CreditAccountCommand;
import org.sid.accountserviceaxon.common.commands.DebitAccountCommand;
import org.sid.accountserviceaxon.common.dtos.CreateAccountRequestDTO;
import org.sid.accountserviceaxon.common.dtos.CreditAccountRequestDTO;
import org.sid.accountserviceaxon.common.dtos.DebitAccountRequestDTO;
import org.sid.accountserviceaxon.common.exceptions.NegativeInitialBalanceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/commands")
public class AccountCommandController {
    private final CommandGateway commandGateway;

    public AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/accounts/create")
    public CompletableFuture<String> createAccount(@RequestBody CreateAccountRequestDTO request) {
        return commandGateway.send(new CreateAccountCommand(
                UUID.randomUUID().toString(), request.getCurrency(), request.getInitialBalance()));
    }

    @PutMapping("/accounts/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request) {
        return commandGateway.send(new CreditAccountCommand(
                request.getAccountId(), request.getCurrency(), request.getAmount()));
    }

    @PutMapping("/accounts/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountRequestDTO request) {
        return commandGateway.send(new DebitAccountCommand(
                request.getAccountId(), request.getCurrency(), request.getAmount()));
    }

    @ExceptionHandler({NegativeInitialBalanceException.class, IllegalArgumentException.class})
    public ResponseEntity<String> handleCommandError(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
