package com.MyInventory.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class BillProduct {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @ManyToOne
        @JoinColumn(name = "productId")
        private Product product;
        private double price;
        private int quantity ;
        private double totalAmount;

        @ManyToOne
        @JoinColumn(name="billId")
        @JsonBackReference
        private Bill bill;




        public BillProduct() {
        }

        public BillProduct(long id, Product product, double price, int quantity, double totalAmount, Bill bill) {
            this.id = id;
            this.product = product;
            this.price = price;
            this.quantity = quantity;
            this.totalAmount = totalAmount;
            this.bill = bill;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(double totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Bill getBill() {
            return bill;
        }

        public void setBill(Bill bill) {
            this.bill = bill;
        }
    }

