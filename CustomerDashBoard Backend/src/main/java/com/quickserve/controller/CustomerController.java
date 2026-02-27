package com.quickserve.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quickserve.model.Customer;
import com.quickserve.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // CREATE CUSTOMER
    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {

        Customer savedCustomer = customerService.createCustomer(customer);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Customer registered successfully");
        response.put("customer", savedCustomer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Customer updateCustomer(@PathVariable Long id,
                                   @RequestBody Customer customer) {
        return customerService.updateCustomer(id, customer);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Customer deleted successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping("/{id}/change-password")
    public ResponseEntity<?> changePassword(@PathVariable Long id,
                                            @RequestBody Map<String, String> body) {

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        customerService.changePassword(id, oldPassword, newPassword);

        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }
}