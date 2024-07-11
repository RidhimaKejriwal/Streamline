package com.crm.streamline.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;
import com.crm.streamline.entities.Project;

@Repository
public interface  ProjectRepository extends JpaRepository<Project, Integer> {

    @Query("SELECT COUNT(p) FROM Project p WHERE p.employee.id = :employeeId")
    long countProjectsByEmployeeId(@Param("employeeId") int employeeId);
    
    @Query("SELECT COUNT(p) FROM Project p WHERE p.customer.id = :customerId")
    long countProjectsByCustomerId(@Param("customerId") int customerId);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.customer.id = :customerId AND p.status = :status")
    long countProjectsByCustomerIdAndStatus(@Param("customerId") int customerId, @Param("status") String status);
    
    Page<Project> findByEmployee(Employee employee, Pageable pageable);

    Page<Project> findByCustomer(Customer customer, Pageable pageable);

    Page<Project> findByEmployeeAndNameContaining(Employee employee, String name, Pageable pageable);

    Page<Project> findByEmployeeAndStatusContaining(Employee employee, String status, Pageable pageable);

    Page<Project> findByCustomerAndNameContaining(Customer customer, String name, Pageable pageable);

    Page<Project> findByCustomerAndStatusContaining(Customer customer, String status, Pageable pageable);
}
