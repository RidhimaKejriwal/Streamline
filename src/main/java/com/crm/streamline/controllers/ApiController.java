package com.crm.streamline.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.services.CustomerService;
import com.crm.streamline.services.EmployeeService;

@RestController
@RequestMapping("/api")
public class ApiController {
    
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable int employeeId) {
        return employeeService.getById(employeeId);
    }

    @GetMapping("/customers/{customerId}")
    public Customer getCustomer(@PathVariable int customerId) {
        return customerService.getById(customerId);
    }
}
