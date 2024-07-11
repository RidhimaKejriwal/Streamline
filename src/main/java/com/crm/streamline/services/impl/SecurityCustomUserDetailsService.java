package com.crm.streamline.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.CustomUserDetails;
import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.repositories.AdminRepository;
import com.crm.streamline.repositories.CustomerRepository;
import com.crm.streamline.repositories.EmployeeRepository;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("getting info --##############");
        // email = "admin@gmail.com";
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        System.out.println(admin);
        System.out.println(email);
        if (admin != null) {
            System.out.println("calling cutom user details of admin");
            CustomUserDetails cud = new CustomUserDetails(admin);
            System.out.println(cud);
            return cud;
        }
        
        Employee employee = employeeRepository.findByEmail(email).orElse(null);
        if (employee != null) {
            return new CustomUserDetails(employee);
        }
        
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer != null) {
            return new CustomUserDetails(customer);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
    
}
