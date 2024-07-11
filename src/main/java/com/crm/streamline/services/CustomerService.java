package com.crm.streamline.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.forms.CustomerForm;

public interface CustomerService {
    
    // save customers
    Customer save(Customer customer);
    
    // update customer
    Customer update(CustomerForm customerForm, int id);

    // get customer
    List<Customer> getAll();

    // get customer by id
    Customer getById(int id);

    // delete customer
    void delete(int id);

    // // search contact
    Page<Customer> searchByFirstName(String firstname, int page, int size, String sortBy, String direction, Employee employee);

    Page<Customer> searchByEmail(String email, int page, int size, String sortBy, String direction, Employee employee);

    Page<Customer> searchByPhoneNumber(String phone, int page, int size, String sortBy, String direction, Employee employee);

    // get customers by employeeId
    List<Customer> getByEmployeeId(int employeeId);

    Page<Customer> getByEmployee(Employee employee, int page, int size, String sortBy, String direction);

    long countCustomers();

    Customer getCustomerByEmail(String email);

    long getNumberOfCustomersByEmployeeId(int employeeId);
    
}
