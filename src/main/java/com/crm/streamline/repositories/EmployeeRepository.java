package com.crm.streamline.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.entities.Employee;

@Repository
public interface  EmployeeRepository extends JpaRepository<Employee, Integer> {
    
    Optional<Employee> findByEmail(String email);

    // custom finder method
    Page<Employee> findByAdmin(Admin admin, Pageable pageable);

    // custom query method
    @Query("SELECT e FROM Employee e WHERE e.admin.id =: adminId")
    List<Employee> findByAdminId(@Param("adminId") int adminId);

    Page<Employee> findByAdminAndFirstNameContaining(Admin admin, String firstName, Pageable pageable);
    
    Page<Employee> findByAdminAndEmailContaining(Admin admin, String email, Pageable pageable);
    
    Page<Employee> findByAdminAndPhoneNumberContaining(Admin admin, String phone, Pageable pageable);

}
