package com.MyInventory.Service;

import com.MyInventory.Entity.Bill;
import com.MyInventory.Entity.BillProduct;
import com.MyInventory.Entity.Product;
import com.MyInventory.Entity.ProductIdQuantity;
import com.MyInventory.Repository.BillRpository;
import com.MyInventory.Repository.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRpository billRepository;

    @Autowired
    private HttpSession session;




    public Bill saveBill(List<ProductIdQuantity> items, String customerName) {
        Bill bill = new Bill();
        bill.setCustomerName(customerName);
        bill.setPurchaseDate(LocalDate.now());

        List<BillProduct> billList = new ArrayList<>();

        // Begin transaction to ensure atomicity
        for (ProductIdQuantity item : items) {
            // Fetch product from repository
            Product product = this.productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: ID " + item.getProductId()));

            // Check if sufficient quantity is available
            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName() +
                        ". Available: " + product.getQuantity() + ", Requested: " + item.getQuantity());
            }

            // Create BillProduct entry
            BillProduct bp = new BillProduct();
            bp.setProduct(product);
            bp.setQuantity(item.getQuantity());
            bp.setPrice(product.getPrice());
            bp.setTotalAmount(product.getPrice() * item.getQuantity());
            bp.setBill(bill);

            billList.add(bp);

            // Reduce product quantity
            product.setQuantity(product.getQuantity() - item.getQuantity());
            this.productRepository.save(product); // Update product quantity
        }

        // Set bill products and calculate total
        bill.setBillProduct(billList);
        bill.setTotalAmount(billList.stream().mapToDouble(BillProduct::getTotalAmount).sum());

        // Save the bill
        long staffId = (long) session.getAttribute("staffId");
        bill.setStaffId(staffId);
        return billRepository.save(bill);
    }
}
