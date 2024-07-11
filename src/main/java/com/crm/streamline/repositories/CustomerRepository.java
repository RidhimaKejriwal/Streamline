package com.crm.streamline.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crm.streamline.entities.Customer;
import com.crm.streamline.entities.Employee;

@Repository
public interface  CustomerRepository extends JpaRepository<Customer, Integer> {
    
    Optional<Customer> findByEmail(String email);

    // custom finder method
    Page<Customer> findByEmployee(Employee employee, Pageable pageable);

    // custom query method
    @Query("SELECT c FROM Customer c WHERE c.employee.id =: employeeId")
    List<Customer> findByEmployeeId(@Param("employeeId") int employeeId);

    @Query("SELECT COUNT(c) FROM Customer c WHERE c.employee.id = :employeeId")
    long countCustomersByEmployeeId(@Param("employeeId") int employeeId);

    Page<Customer> findByEmployeeAndFirstNameContaining(Employee employee, String firstName, Pageable pageable);
    
    Page<Customer> findByEmployeeAndEmailContaining(Employee employee, String email, Pageable pageable);
    
    Page<Customer> findByEmployeeAndPhoneNumberContaining(Employee employee, String phone, Pageable pageable);
}
