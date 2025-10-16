package com.MyInventory.Service;

import com.MyInventory.Entity.Users;
import com.MyInventory.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {



        @Autowired
        private AdminRepository adminRepository;

        public List<Users> getAllUsers()
        {
            return this.adminRepository.findAll();
        }

        public Users addUser(Users user)
        {
            return this.adminRepository.save(user);
        }

        public Optional<Users> getUserById(long id) {

            return this.adminRepository.findById(id);
        }

        public void deleteById(long userById) {

            this.adminRepository.deleteById(userById);
        }
    }
