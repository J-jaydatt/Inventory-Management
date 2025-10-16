package com.MyInventory.Service;

import com.MyInventory.Entity.Users;
import com.MyInventory.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StaffService {


    @Autowired
    private AdminRepository adminRepository;

    public Optional<Users> getUserDetailsByEmail(String userEmail) {

        return this.adminRepository.findByEmail(userEmail);

    }
}
