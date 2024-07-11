package com.crm.streamline.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.forms.EmployeeForm;
import com.crm.streamline.helpers.ResourceNotFoundException;
import com.crm.streamline.repositories.EmployeeRepository;
import com.crm.streamline.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepo;
    
    @Override
    public Employee save(Employee employee) {
        return employeeRepo.save(employee);
    }

    @Override
    public Employee update(EmployeeForm employeeForm, int id) {
        var employeeOld = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employeeOld.setFirstName(employeeForm.getFirstName());
        employeeOld.setEmail(employeeForm.getEmail());
        employeeOld.setPhoneNumber(employeeForm.getPhoneNumber());
        employeeOld.setAddress(employeeForm.getAddress());
        employeeOld.setCity(employeeForm.getCity());
        employeeOld.setEmployeeType(employeeForm.getEmployeeType());
        employeeOld.setLastName(employeeForm.getLastName());
        employeeOld.setState(employeeForm.getState());
        
        return employeeRepo.save(employeeOld);
    }

    @Override
    public List<Employee> getAll() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee getById(int id) {
        return employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with given id "+id));
    }

    @Override
    public void delete(int id) {
        var employee = employeeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee not found with given id "+id));
        employeeRepo.delete(employee);
    }


    @Override
    public Page<Employee> searchByEmail(String email, int page, int size, String sortBy, String direction,
            Admin admin) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return employeeRepo.findByAdminAndEmailContaining(admin, email, pageable);
    }

    @Override
    public Page<Employee> searchByPhoneNumber(String phone, int page, int size, String sortBy, String direction,
            Admin admin) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return employeeRepo.findByAdminAndPhoneNumberContaining(admin, phone, pageable);
    }

    @Override
    public List<Employee> getByAdminId(int adminId) {
        return employeeRepo.findByAdminId(adminId);
    }

    @Override
    public Page<Employee> getByAdmin(Admin admin, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return employeeRepo.findByAdmin(admin, pageable);
    }

    @Override
    public Page<Employee> searchByFirstName(String firstname, int page, int size, String sortBy, String direction,
            Admin admin) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return employeeRepo.findByAdminAndFirstNameContaining(admin, firstname, pageable);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        
        return employeeRepo.findByEmail(email).orElse(null);
    }

    @Override
    public long countEmployees() {
        return employeeRepo.count();
    }

    
    
}
