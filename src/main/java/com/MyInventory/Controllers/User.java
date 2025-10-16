package com.MyInventory.Controllers;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.MyInventory.Entity.Users;
import com.MyInventory.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class User {


    @Autowired
    private AdminService adminService;

    @Autowired
   PasswordEncoder passwordEncoder; // inject the class created in Service

    @GetMapping("/allUser")
    public String getAllUsers(Model model)
    {
        List<Users> user= adminService.getAllUsers();
        model.addAttribute("user",user);
        return "User";

    }

    @PostMapping("/RegisterUser")
    public String addNewUser(@ModelAttribute Users user, Model model, HttpSession session)
    {
        try {


            Users newUser= new Users();
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRegistration_date(LocalDate.now());

            newUser = this.adminService.addUser(user);
            if (newUser != null) {
                session.setAttribute("Message", "Registration Successful !!");
                session.setAttribute("class", "alert-success");
                model.addAttribute("user", newUser);
                return "Register";
            } else {
                throw new Exception("User is not been saved");
            }

        }
        catch (Exception e)
        {
            session.setAttribute("Message", "Failed to add the user !!");
            session.setAttribute("class", "alert-danger");
            model.addAttribute("user", user);
        }
        return "Register";
    }



    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<Users>> getUserById(@PathVariable("id") long id)
    {
        if (id==0)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Optional<Users> userById=this.adminService.getUserById(id);
        if(userById.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userById);
    }


    @PutMapping("/user")
    public ResponseEntity<?> updateUser(@RequestBody Users user)
    {
        long userId=user.getUserId();
        Optional<Users> userById=this.adminService.getUserById(userId);
        if(userById.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found with id "+userId);
        }
        return  ResponseEntity.ok(this.adminService.addUser(user));
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") long id)
    {
        Optional<Users> userById=this.adminService.getUserById(id);

        if(userById.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with Id ->"+id);
        }
        else {
            this.adminService.deleteById(id);
            return ResponseEntity.ok("User Deleted Successfully !!!");
        }

    }
}
