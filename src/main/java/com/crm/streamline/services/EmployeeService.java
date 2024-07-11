package com.crm.streamline.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.forms.EmployeeForm;

public interface EmployeeService {
    
    // save employees
    Employee save(Employee employee);
    
    // update employee
    Employee update(EmployeeForm employeeForm, int id);

    // get by email
    Employee getEmployeeByEmail(String email);

    // get employee
    List<Employee> getAll();

    // get employee by id
    Employee getById(int id);

    // delete employee
    void delete(int id);

    // // search contact
    Page<Employee> searchByFirstName(String firstname, int page, int size, String sortBy, String direction, Admin admin);

    Page<Employee> searchByEmail(String email, int page, int size, String sortBy, String direction, Admin admin);

    Page<Employee> searchByPhoneNumber(String phone, int page, int size, String sortBy, String direction, Admin admin);

    // get employees by adminId
    List<Employee> getByAdminId(int adminId);

    Page<Employee> getByAdmin(Admin admin, int page, int size, String sortBy, String direction);
    
    long countEmployees();
}
