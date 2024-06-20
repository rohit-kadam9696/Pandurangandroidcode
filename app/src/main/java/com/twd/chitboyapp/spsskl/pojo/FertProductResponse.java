package com.twd.chitboyapp.spsskl.pojo;

import java.util.List;

public class FertProductResponse extends MainResponse {
    private List<FertProduct> fertProducts;

    public List<FertProduct> getFertProducts() {
        return fertProducts;
    }

    public void setFertProducts(List<FertProduct> fertProducts) {
        this.fertProducts = fertProducts;
    }
}
