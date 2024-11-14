package com.corebankingsystem.AccountMs.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Entity
@Table(name="account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="accountNumber", unique = true)
    private String accountNumber;

    @Column(name="balance")
    @NotEmpty(message = "To open an account you need an amount")
    private double balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "typeAccount", nullable = false)
    @NotEmpty(message = "Account type is required")
    private TypeAccount typeAccount;

    @Column(name="customer_id", nullable = false)
    @NotEmpty(message = "CustomerID type is required")
    private long customerId;

    public enum TypeAccount {
        ahorros,
        corriente
    }

    //Metodo para depositar
    public void deposit(double amount){
        if (amount > 0) {
            this.balance += amount;
        }
    }

    // Método para retirar
    public void withdrawal(Double amount) {
        if (amount > 0) {
            if (this.typeAccount == TypeAccount.ahorros) {

                if (this.balance - amount < 0) {
                    throw new IllegalArgumentException("No se puede realizar un retiro que deje el saldo en negativo para cuentas de ahorro.");
                }
            } else if (this.typeAccount == TypeAccount.corriente) {

                if (this.balance - amount < -500) {
                    throw new IllegalArgumentException("El sobregiro máximo permitido en una cuenta corriente es de -500.");
                }
            }

            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("La cantidad a retirar debe ser mayor que 0.");
        }
    }



}
