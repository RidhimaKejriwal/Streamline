package com.crm.streamline.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.entities.Project;
import com.crm.streamline.repositories.ProjectRepository;
import com.crm.streamline.services.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectRepository projectRepo;

    @Override
    public Project save(Project project) {
        return projectRepo.save(project);
    }

    @Override
    public List<Project> getAll() {
        return projectRepo.findAll();
    }

    @Override
    public Project getById(int id) {
        return projectRepo.findById(id).orElse(null);
    }

    @Override
    public Page<Project> getByEmployee(Employee employee, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return projectRepo.findByEmployee(employee, pageable);
    }

    @Override
    public Page<Project> getByCustomer(Customer customer, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        var pageable = PageRequest.of(page, size, sort);

        return projectRepo.findByCustomer(customer, pageable);
    }

    @Override
    public long getNumberOfProjectsByEmployeeId(int employeeId) {
        return projectRepo.countProjectsByEmployeeId(employeeId);
    }

    @Override
    public long getNumberOfProjectsByCustomerId(int customerId) {
        return projectRepo.countProjectsByCustomerId(customerId);
    }

    @Override
    public long getNumberOfProjectsByCustomerIdAndStatus(int customerId, String status) {
        return projectRepo.countProjectsByCustomerIdAndStatus(customerId, status);
    }

    @Override
    public Page<Project> searchByName(String name, int page, int size, String sortBy, String direction,
            Customer customer) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return projectRepo.findByCustomerAndNameContaining(customer, name, pageable);
    }

    @Override
    public Page<Project> searchByStatus(String status, int page, int size, String sortBy, String direction,
            Customer customer) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return projectRepo.findByCustomerAndStatusContaining(customer, status, pageable);
    }

    @Override
    public Page<Project> searchByName(String name, int page, int size, String sortBy, String direction,
            Employee employee) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return projectRepo.findByEmployeeAndNameContaining(employee, name, pageable);
    }

    @Override
    public Page<Project> searchByStatus(String status, int page, int size, String sortBy, String direction,
            Employee employee) {
        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        var pageable = PageRequest.of(page, size, sort);
        return projectRepo.findByEmployeeAndStatusContaining(employee, status, pageable);
    }

}
