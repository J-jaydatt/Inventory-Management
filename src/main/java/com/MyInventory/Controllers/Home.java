package com.MyInventory.Controllers;

import com.MyInventory.Entity.Users;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Home {


    @GetMapping("/")
    public String getHomePage()
    {
        return "Home";
    }

    @GetMapping("/Login")
    public String getLoginPage()
    {
        return "Login";
    }

    @GetMapping("/register")
    public String getRegistraionPage(Model model, HttpSession session)
    {
        Users user = new Users();
        model.addAttribute("user",user);
        session.removeAttribute("Message");
        session.removeAttribute("class");
        return "Register";
    }

}
