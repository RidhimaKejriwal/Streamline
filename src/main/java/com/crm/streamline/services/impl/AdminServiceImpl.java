package com.crm.streamline.services.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crm.streamline.entities.Admin;
import com.crm.streamline.repositories.AdminRepository;
import com.crm.streamline.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

    @Autowired
    private AdminRepository adminRepo;

    // private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepo.save(admin);
    }

    @Override
    public Optional<Admin> getAdminById(int id) {
        return adminRepo.findById(id);
    }

    @Override
    public boolean isAdminExist(int id) {
        Admin admin = adminRepo.findById(id).orElse(null);
        return (admin!=null);
    }

    @Override
    public boolean isAdminExistByEmail(String email) {
        Admin admin = adminRepo.findByEmail(email).orElse(null);
        return (admin!=null);
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepo.findByEmail(email).orElse(null);
    }
    
}
