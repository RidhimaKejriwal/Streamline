package com.crm.streamline.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.helpers.Helper;
import com.crm.streamline.repositories.AdminRepository;
import com.crm.streamline.repositories.CustomerRepository;
import com.crm.streamline.repositories.EmployeeRepository;

@ControllerAdvice
public class RootController {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;
    
    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) 
    {
        if(authentication == null)
            return;
        System.out.println("Adding logged in user information to the model");
        String username = Helper.getEmailOfLoggedInUser(authentication);

        logger.info("User logged in: {}", username);


        Admin admin = adminRepository.findByEmail(username).orElse(null);
        if(admin != null) {
            model.addAttribute("user", admin);
            model.addAttribute("role", admin.getRole());
            return;
        }

        Employee employee = employeeRepository.findByEmail(username).orElse(null);
        if (employee != null) {
            model.addAttribute("user", employee);
            return;
        }
        
        Customer customer = customerRepository.findByEmail(username).orElse(null);
        if (customer != null) {
            model.addAttribute("user", customer);
            return;
        }

        model.addAttribute("user", null);
    }
}
