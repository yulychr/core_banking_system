package com.corebankingsystem.CustomerMs.controller;

import com.corebankingsystem.CustomerMs.model.entity.Customer;

import com.corebankingsystem.CustomerMs.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Post - Create a new customer
    @PostMapping
    public ResponseEntity<Object> crateCustomer(@Valid @RequestBody Customer customer) {
        Optional<Customer> dni_already_exists = customerService.getCustomerDni(customer.getDni());
        if (dni_already_exists.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "El DNI ya existe");
            return ResponseEntity.status(409).body(response);
        }
        Customer customerCreated = customerService.savedCustomer(customer);
        return ResponseEntity.status(201).body(customerCreated);
    }

    //Retrieves a list of all customers in the system.
    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getCustomers();
        return ResponseEntity.status(200).body(customers);
    }

    // Retrieve a customer details by id
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerId(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerId(id);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(404).build());
    }

    //Updates the details of an existing customer identified by the `id` provided in the path.
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customer customer) {
        if (customerService.getCustomerId(id).isPresent()) {
            customer.setId(id);
            Customer customerUpdate = customerService.savedCustomer(customer);
            return ResponseEntity.status(200).body(customerUpdate);
        }
        return ResponseEntity.status(404).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustomerId(id);
        if (customer.isPresent()) {
            if (!customer.get().getAccounts().isEmpty()) {
                return ResponseEntity.status(422).build();
            }
            customerService.deleteCustomer(id);
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).build();
    }

}
