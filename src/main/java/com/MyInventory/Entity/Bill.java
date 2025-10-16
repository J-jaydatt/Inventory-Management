package com.MyInventory.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Bill {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long billId;

        @OneToMany(mappedBy = "bill" ,cascade = CascadeType.ALL)
        @JsonManagedReference
        private List<BillProduct> billProduct;

        private String CustomerName;
        private LocalDate purchaseDate;
        private double totalAmount;
        private long staffId;

        public Bill() {
        }

    public Bill(long billId, List<BillProduct> billProduct, String customerName, LocalDate purchaseDate, double totalAmount, long staffId) {
        this.billId = billId;
        this.billProduct = billProduct;
        CustomerName = customerName;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.staffId = staffId;
    }

    public long getBillId() {
            return billId;
        }

        public void setBillId(long billId) {
            this.billId = billId;
        }

        public List<BillProduct> getBillProduct() {
            return billProduct;
        }

        public void setBillProduct(List<BillProduct> billProduct) {
            this.billProduct = billProduct;
        }

        public String getCustomerName() {
            return CustomerName;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }

        public LocalDate getPurchaseDate() {
            return purchaseDate;
        }

        public void setPurchaseDate(LocalDate purchaseDate) {
            this.purchaseDate = purchaseDate;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

    public long getStaffId() {  return staffId;}

    public void setStaffId(long staffId) { this.staffId = staffId; }
}

