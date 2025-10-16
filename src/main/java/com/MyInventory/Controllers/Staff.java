package com.MyInventory.Controllers;

import com.MyInventory.Entity.*;
import com.MyInventory.Repository.AdminRepository;
import com.MyInventory.Service.BillService;
import com.MyInventory.Service.ProductService;
import com.MyInventory.Service.StaffService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/staff")
public class Staff {

    @Autowired
    private ProductService productService;

    @Autowired
    private BillService billService;

    @Autowired
    private StaffService staffService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/Home")
    public String getHomePage(Model model , HttpSession session)
    {
        List<Product>productList=this.productService.getAllProducts();
        if(productList.isEmpty())
        {
            session.setAttribute("Message", "No Products Found !!");
            return "redirect:/staff/Home";
        }
        String userEmail= (String) session.getAttribute("username");
        Optional<Users> user=this.staffService.getUserDetailsByEmail(userEmail);
        String username=user.get().getUserName();
        session.setAttribute("staffId", user.get().getUserId());
        session.setAttribute("name",username);
        session.setAttribute("role", "Staff");
        model.addAttribute("productList",productList);
        return "Staff/StaffHomePage";
    }

    @GetMapping("/products")
    public String StaffProducts(Model model,HttpSession session)
    {
        List<Product>productList=this.productService.getAllProducts();
        if(productList.isEmpty())
        {
            session.setAttribute("Message", "No Products Found !!");
            return "Staff/StaffProducts";
        }
        model.addAttribute("productList",productList);
        return "Staff/StaffProducts";
    }

    @PostMapping("/submit-bill")
    public String generateBill(@ModelAttribute("billform") BillForm billForms, @RequestParam("customerName") String customerName, Model model)
    {

        List<ProductIdQuantity> list= billForms.getProductIdQuantities();
        Bill generatedBill = this.billService.saveBill(list, customerName);
        if(generatedBill==null)
        {
            return "/staff/Home";
        }
        System.out.println("Bill Id :"+generatedBill.getBillId());
        System.out.println("Customer Name: " +generatedBill.getCustomerName());
        model.addAttribute("newBill",generatedBill);
        return "Staff/StaffBillPrint";
    }

    @GetMapping("/profile")
    public String getProfilePage(HttpSession session,Model model)
    {
        String userEmail= (String) session.getAttribute("username");
        Optional<Users> user=this.staffService.getUserDetailsByEmail(userEmail);
        System.out.println(user.get().getUserId()+"  "+user.get().getUserName());
        if(user.isEmpty())
        {
            model.addAttribute("fail","Error Doent  exist");
            System.out.println("Fail !!!!!!!!!!!!!!!!!!!!!");
        }
        Users presentUser=user.get();
        model.addAttribute("user",presentUser);
        return "Staff/StaffProfile";
    }


    @GetMapping("/changepassword")
    public String changePassword(HttpSession session)
    {
        session.removeAttribute("message");
        session.removeAttribute("success");

        return "/Staff/StaffChangePassword";
    }

    @PostMapping("/changePassword")
    public String checkChangedPassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null || username.isEmpty()) {
            session.setAttribute("message", "Session expired! Please log in again.");
            return "redirect:/login";
        }

        Optional<Users> userOpt = adminRepository.findByEmail(username);

        if (userOpt.isEmpty()) {
            session.setAttribute("message", "User not found!");
            session.setAttribute("class", "alert-danger");
            return "/Staff/StaffChangePassword";
        }

        Users user = userOpt.get();
        System.out.println("Checking______________" + user.getUserId() + "  " + user.getUserName());

        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            session.setAttribute("message", "Old password is incorrect!");
            session.setAttribute("class", "alert-danger");
            return "/Staff/StaffChangePassword";
        }

        // Check new password confirmation
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("message", "New Password and Confirm Password do not match!");
            session.setAttribute("class", "alert-danger");
            return "/Staff/StaffChangePassword";
        }

        // Encode and update
        user.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(user);

        session.setAttribute("success", "Password changed successfully !");
        session.setAttribute("class", "alert-success");
        return "/Staff/StaffChangePassword";
    }

}
