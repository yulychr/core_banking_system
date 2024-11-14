package com.corebankingsystem.CustomerMs.model.entity;
import lombok.*;
import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;


@Data
@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="firstName")
    @NotEmpty(message = "El nombre es obligatorio")
    private String firstName;

    @Column(name="lastName")
    @NotEmpty(message = "El apellido es obligatorio")
    private String lastName;

    @Column(name="dni", unique = true)
    @NotEmpty(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @Column(name="email")
    @Email(message = "Formato de correo electrónico inválido")
    @NotEmpty(message = "El email es obligatorio")
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts;
}
