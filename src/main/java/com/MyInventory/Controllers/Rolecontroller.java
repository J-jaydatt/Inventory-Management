package com.MyInventory.Controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Rolecontroller {


    @GetMapping("/default")
    public String ServeTheRole(Authentication authentication, HttpSession session)
    {
        String username = authentication.getName();
        System.out.println("Logged-in user: " + username);
        String roles = authentication.getAuthorities().toString();
        if (roles.contains("ROLE_ADMIN"))
        {
            session.setAttribute("username", username);
            return "redirect:/admin/Home";
        }
        if (roles.contains("ROLE_STAFF"))
        {
            session.setAttribute("username", username);
            return "redirect:/staff/Home";
        }

        return "error";
    }
}
