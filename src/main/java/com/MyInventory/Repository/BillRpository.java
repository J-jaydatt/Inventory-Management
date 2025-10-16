package com.MyInventory.Repository;

import com.MyInventory.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRpository extends JpaRepository<Bill,Long> {
    @Query("SELECT SUM(b.totalAmount) FROM Bill b")
    Double getTotalSales();

}
