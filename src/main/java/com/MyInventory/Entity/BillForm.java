package com.MyInventory.Entity;

import java.util.List;

public class BillForm {

    private List<ProductIdQuantity> productIdQuantities;

    public List<ProductIdQuantity> getProductIdQuantities() {
        return productIdQuantities;
    }

    public void setProductIdQuantities(List<ProductIdQuantity> productIdQuantities) {
        this.productIdQuantities = productIdQuantities;
    }
}
