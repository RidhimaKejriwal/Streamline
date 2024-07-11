package com.crm.streamline.services;

import java.util.Optional;

import com.crm.streamline.entities.Admin;

public interface AdminService {
    
    Admin saveAdmin(Admin admin);

    Optional<Admin> getAdminById(int id);

    boolean isAdminExist(int id);

    boolean isAdminExistByEmail(String email);

    Admin getAdminByEmail(String email);
}
