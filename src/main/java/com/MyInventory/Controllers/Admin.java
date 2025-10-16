package com.MyInventory.Controllers;

import com.MyInventory.Entity.*;
import com.MyInventory.Repository.*;
import com.MyInventory.Service.AdminService;
import com.MyInventory.Service.BillService;
import com.MyInventory.Service.ProductService;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class Admin {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private BillRpository billRpository;

    @Autowired
    private BillProductRepository billProductRepository;

    @GetMapping("/Home")
    public String getAdminhomePage(Model model,HttpSession session)
    {
        model.addAttribute( "totalProducts",this.productService.getAllProducts().size());
        model.addAttribute("totalSales",this.billRpository.getTotalSales());
        List<Product> products=this.productRepository.findAll();
        int totalUser=this.adminService.getAllUsers().size();
        String userName = this.adminService.getAllUsers().stream()
                .filter(x -> "ADMIN".equals(x.getRole()))
                .map(Users::getUserName)
                .findFirst()
                .orElse(null);
        session.setAttribute("userName", userName);
        session.setAttribute("role", "Admin");
        int LowStockItems= (int) products.stream().filter(x->  x.getQuantity() < 5).count();
        model.addAttribute("totalUsers",(totalUser-1));
        model.addAttribute("LowStockItems",LowStockItems);
        model.addAttribute("productList",products);
        return "Admin/AdminHome";
    }

    @GetMapping("/products")
    public String getProductsPage()
    {
        return "Admin/AdminProducts";
    }

    @GetMapping("/purchaseOrders")
    public String getAdminsPurchase(Model model)
    {

        List<Purchase> purchaseList=this.purchaseRepository.findAll();
        model.addAttribute("purchaseList",purchaseList);
        return "Admin/AdminPurchaseOrder";
    }

    @GetMapping("/restock")
    public String getRestockForm(Model model,HttpSession session)
    {
        session.removeAttribute("Message");
        session.removeAttribute("class");
        List<Product> productsList=productRepository.findAll();
        if(productsList.isEmpty())
        {
            model.addAttribute("Message","No Products Found");
            return "Admin/AdminRestockProductForm";
        }
        model.addAttribute("Products",productsList);
        return "Admin/AdminRestockProductForm";
    }

    @PostMapping("/restockSave")
    public String saveRestock(@ModelAttribute Purchase purchase, @RequestParam("productId") long productId, HttpSession session) {
        // Fetch the existing product
        Product getProduct = this.productRepository.getReferenceById(productId);

        // Update product's price and quantity before saving
        int restockQuantity = purchase.getQuantity();
        long newPricePerUnit = purchase.getPricePerUnit();

        // Update the product's quantity and price
        getProduct.setQuantity(getProduct.getQuantity() + restockQuantity);
        getProduct.setPrice(newPricePerUnit);

        // Save the updated product
        this.productRepository.save(getProduct); // Save product updates

        // Set product in purchase
        purchase.setProduct(getProduct);
        purchase.setPurchaseDate(LocalDate.now());

        // Save purchase record
        Purchase saved = this.purchaseRepository.save(purchase);

        if (saved != null) {
            session.setAttribute("Message", "Product Restocked Successfully!");
            session.setAttribute("class", "alert-success");
        } else {
            session.setAttribute("Message", "Failed to restock product.");
            session.setAttribute("class", "alert-danger");
        }

        return "Admin/AdminRestockProductForm";
    }


    @GetMapping("/sales")
    public String getSales(Model model)
    {
        List<Bill> billList=this.billRpository.findAll();
        model.addAttribute("billList",billList);
        return "Admin/AdminSalesOrder";
    }

        @PostMapping("/billDetails")
        public String billDetails(@RequestParam("billId") long billId ,Model model)
        {
            Optional<Bill> optionalBill = billRpository.findById(billId);

            if (optionalBill.isPresent()) {
                Bill bill = optionalBill.get();


                long  staffId=bill.getStaffId();
                System.out.println("Staff bill id"+staffId);
                Optional<Users> staffDetails= this.adminService.getUserById(staffId);
                if(staffDetails.isEmpty())
                {

                }
                Users finalDetails =staffDetails.get();
                model.addAttribute("staffName",finalDetails.getUserName());
                model.addAttribute("staffId",finalDetails.getUserId());

                List<BillProduct> billView = billProductRepository.findByBill_BillId(billId);
                model.addAttribute("billPrint", billView);
                model.addAttribute("bill", bill); // unwrap and send actual Bill
            } else {
                // handle case where bill is not found
                model.addAttribute("error", "Bill not found for ID: " + billId);
                return "error"; // return to a proper error page
            }
            return "Admin/AdminSalesBillDetails";
        }

    @GetMapping("/users")
    public String getAllUsers(Model model)
    {
        List<Users> userList=this.adminService.getAllUsers();

        System.out.println("Users fetched: " + userList.size());
        model.addAttribute("userList",userList);
        return "Admin/AdminUsersPage";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("userId") long userId )
    {
        System.out.println("Deleting user with ID: " + userId);
        this.adminService.deleteById(userId);
        return "redirect:/admin/users";
    }


    @GetMapping("/profile")
    public String getProfile(Authentication authentication ,Model model)
    {
        String email = authentication.getName(); //  logged-in user's email

        // Use email to get user ID
        Users user = adminRepository.findByEmail(email).orElse(null);
        if (user != null)
        {
            System.out.println("User Name : "+user.getUserName() + "Role :"+user.getRole() +"Email : "+user.getEmail());
            model.addAttribute("userDetails", user);
            return "Admin/AdminProfile";

        }
        System.out.println("User is null");
        return "redirect:/admin/Home";
    }

    @GetMapping("/changepassword")
    public String changePassword(HttpSession session)
    {
        session.removeAttribute("message");
        session.removeAttribute("success");
        return "/Admin/AdminChangePassword";
    }

    @PostMapping("/changePassword")
    public String checkChangedPassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       HttpSession session) {

        String username = (String) session.getAttribute("username");

        if (username == null || username.isEmpty()) {
            session.setAttribute("message", "Session expired! Please log in again.");
            session.setAttribute("class", "alert-danger");
            return "Home";
        }

        Optional<Users> userOpt = adminRepository.findByEmail(username);

        if (userOpt.isEmpty()) {
            session.setAttribute("message", "User not found!");
            session.setAttribute("class", "alert-danger");
            return "/Admin/AdminChangePassword";
        }

        Users user = userOpt.get();
        System.out.println("Checking______________" + user.getUserId() + "  " + user.getUserName());

        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            session.setAttribute("message", "Old password is incorrect!");
            session.setAttribute("class", "alert-danger");
            return "/Admin/AdminChangePassword";
        }

        // Check new password confirmation
        if (!newPassword.equals(confirmPassword)) {
            session.setAttribute("message", "New Password and Confirm Password do not match!");
            session.setAttribute("class", "alert-danger");
            return "/Admin/AdminChangePassword";
        }

        // Encode and update
        user.setPassword(passwordEncoder.encode(newPassword));
        adminRepository.save(user);

        session.setAttribute("success", "Password changed successfully!");
        session.setAttribute("class", "alert-success");
        return "/Admin/AdminChangePassword";
    }


}
