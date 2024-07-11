package com.crm.streamline.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.forms.CustomerForm;
import com.crm.streamline.helpers.ResourceNotFoundException;
import com.crm.streamline.repositories.CustomerRepository;
import com.crm.streamline.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepo;

    @Override
    public Customer save(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public Customer update(CustomerForm customerForm, int id) {
        var customerOld = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        customerOld.setFirstName(customerForm.getFirstName());
        customerOld.setEmail(customerForm.getEmail());
        customerOld.setPhoneNumber(customerForm.getPhoneNumber());
        customerOld.setAddress(customerForm.getAddress());
        customerOld.setCity(customerForm.getCity());
        customerOld.setCompanyName(customerForm.getCompanyName());
        customerOld.setLastName(customerForm.getLastName());
        customerOld.setState(customerForm.getState());

        return customerRepo.save(customerOld);
    }

    @Override
    public List<Customer> getAll() {
        return customerRepo.findAll();
    }

    @Override
    public Customer getById(int id) {
        return customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with given id " + id));
    }

    @Override
    public void delete(int id) {
        var customer = customerRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Customer not found with given id " + id));
        customerRepo.delete(customer);
    }

    @Override
    public Page<Customer> searchByFirstName(String firstname, int page, int size, String sortBy, String direction,
            Employee employee) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return customerRepo.findByEmployeeAndFirstNameContaining(employee, firstname, pageable);
    }

    @Override
    public Page<Customer> searchByEmail(String email, int page, int size, String sortBy, String direction,
            Employee employee) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return customerRepo.findByEmployeeAndEmailContaining(employee, email, pageable);
    }

    @Override
    public Page<Customer> searchByPhoneNumber(String phone, int page, int size, String sortBy, String direction,
            Employee employee) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return customerRepo.findByEmployeeAndPhoneNumberContaining(employee, phone, pageable);
    }

    @Override
    public List<Customer> getByEmployeeId(int employeeId) {
        return customerRepo.findByEmployeeId(employeeId);
    }

    @Override
    public Page<Customer> getByEmployee(Employee employee, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return customerRepo.findByEmployee(employee, pageable);
    }

    @Override
    public long countCustomers() {
        return customerRepo.count();
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email).orElse(null);
    }

    @Override
    public long getNumberOfCustomersByEmployeeId(int employeeId) {
        return customerRepo.countCustomersByEmployeeId(employeeId);
    }

}
