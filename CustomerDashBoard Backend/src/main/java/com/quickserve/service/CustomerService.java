package com.quickserve.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import com.quickserve.model.Customer;
import com.quickserve.repository.CustomerRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private NotificationService notificationService;

    // CREATE CUSTOMER
    public Customer createCustomer(Customer customer) {

        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        return customerRepository.save(customer);
    }

    // GET ALL
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // GET BY ID
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // UPDATE
    public Customer updateCustomer(Long id, Customer updatedCustomer) {

        Customer existing = getCustomerById(id);

        if (!existing.getEmail().equals(updatedCustomer.getEmail())
                && customerRepository.existsByEmail(updatedCustomer.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        existing.setFullName(updatedCustomer.getFullName());
        existing.setEmail(updatedCustomer.getEmail());
        existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
        existing.setAddress(updatedCustomer.getAddress());
        existing.setCity(updatedCustomer.getCity());

        return customerRepository.save(existing);
    }

    // DELETE
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
    
    public void changePassword(Long id, String oldPassword, String newPassword) {

        Customer customer = getCustomerById(id);

        if (!customer.getPassword().equals(oldPassword)) {
            throw new RuntimeException("Old password is incorrect");
        }

        customer.setPassword(newPassword);
        customerRepository.save(customer);
        notificationService.createNotification(
                id,
                "Your password has been changed successfully."
        );
    }
}