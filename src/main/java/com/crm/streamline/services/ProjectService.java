package com.crm.streamline.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.entities.Project;

public interface ProjectService {

    Project save(Project project);

    List<Project> getAll();

    Project getById(int id);

    Page<Project> getByEmployee(Employee employee, int page, int size, String sortBy, String direction);

    Page<Project> getByCustomer(Customer customer, int page, int size, String sortBy, String direction);

    long getNumberOfProjectsByEmployeeId(int employeeId);

    long getNumberOfProjectsByCustomerId(int customerId);

    long getNumberOfProjectsByCustomerIdAndStatus(int customerId, String status);

    // // search project
    Page<Project> searchByName(String name, int page, int size, String sortBy, String direction, Customer customer);

    Page<Project> searchByStatus(String status, int page, int size, String sortBy, String direction, Customer customer);

    // // search prject
    Page<Project> searchByName(String name, int page, int size, String sortBy, String direction, Employee employee);

    Page<Project> searchByStatus(String status, int page, int size, String sortBy, String direction, Employee employee);

}
