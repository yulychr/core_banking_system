package com.corebankingsystem.AccountMs.controller;

import com.corebankingsystem.AccountMs.model.entity.Account;
import com.corebankingsystem.AccountMs.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Respuesta 201 cuando se crea la cuenta
    public Account createAccount(@Valid @RequestBody Account account) {
        // Llamar al servicio para crear la cuenta
        return accountService.createAccount(
                account.getBalance(),
                account.getTypeAccount(),
                account.getCustomerId()
        );
    }

    @GetMapping
    public ResponseEntity<List<Account>> listAccounts() {
        List<Account> accounts = accountService.getAccounts();
        return ResponseEntity.status(200).body(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountId(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/depositar")
    public ResponseEntity<Account> deposit(@PathVariable Long cuentaId, @RequestParam Double amount) {
        Account account = accountService.deposit(cuentaId, amount);
        return ResponseEntity.ok(account);
    }

    @PutMapping("/{Id}/retirar")
    public ResponseEntity<Account> withdrawal(@PathVariable Long cuentaId, @RequestParam Double amount) {
        Account account = accountService.withdrawal(cuentaId, amount);
        return ResponseEntity.ok(account);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
