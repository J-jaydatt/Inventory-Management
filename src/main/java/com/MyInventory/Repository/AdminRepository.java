package com.MyInventory.Repository;

import com.MyInventory.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Users,Long> {

    Optional<Users> findByEmail(String email);
}
