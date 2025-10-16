package com.MyInventory.Controllers;

import com.MyInventory.Entity.Purchase;
import com.MyInventory.Repository.PurchaseRepository;
import com.MyInventory.Service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.MyInventory.Entity.Product;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PurchaseRepository purchaseRepository;

   @GetMapping("/get")
   public String getProduct()
   {
       return "ProductList";
   }

   @GetMapping("/add")
    public String addProduct(Model model ,HttpSession session)
   {
       Product product = new Product();
       model.addAttribute("product",product);
        session.removeAttribute("Message");
        session.removeAttribute("class");
       return "Products/AddProduct";
   }

   @PostMapping("/SaveProduct")
    public String saveProduct(@ModelAttribute Product product, HttpSession session)
   {
       Product addedProduct =this.productService.addNewProduct(product);
        if(addedProduct !=null)
        {

            //Saving the Purchase with the Product
            Purchase purchase = new Purchase();
            purchase.setProduct(addedProduct); // assuming Purchase has a Product field
            purchase.setQuantity(product.getQuantity());
            purchase.setPricePerUnit(product.getPrice());
            purchase.setPurchaseDate(LocalDate.now());

            purchaseRepository.save(purchase);

            session.setAttribute("Message", "Successful !!");
            session.setAttribute("class", "alert-success");
            session.setAttribute("productController", addedProduct);
            return "Products/AddProduct";
        }
        else {
            session.setAttribute("Message", "Failed!!");
            session.setAttribute("class", "alert-danger");
            session.setAttribute("productController", product);
            return "Products/AddProduct";

        }
   }

    @GetMapping("/category")
    public String ShowProductByCategory(@RequestParam("name") String Category,Model model)
    {
        List<Product> categoryProducts= productService.findProductByCategory(Category);
        model.addAttribute("products",categoryProducts);
       return "Admin/AdminProductCategory";
    }





}
