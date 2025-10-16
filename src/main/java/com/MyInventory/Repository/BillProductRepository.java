package com.MyInventory.Repository;

import com.MyInventory.Entity.BillProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BillProductRepository extends JpaRepository<BillProduct,Long> {
    List<BillProduct> findByBill_BillId(Long billId);
}
