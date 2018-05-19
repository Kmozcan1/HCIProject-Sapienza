package com.example.onlinemarket.onlinemarket;

public class Product {
    String productKey;
    String productName;
    Double price;
    String company;
    String productImage;
    String category;

    public String getProductKey() { return productKey; }

    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public String getCompany() {
        return company;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getCategory() {
        return category;
    }




    public Product(String productName, Double price, String company, String productImage, String category) {
        this.productName = productName;
        this.price = price;
        this.company = company;
        this.productImage = productImage;
        this.category = category;
    }

    //Alternative constructor with the "productKey" parameter
    public Product(String productKey, String productName, Double price, String company, String productImage, String category) {
        this.productKey = productKey;
        this.productName = productName;
        this.price = price;
        this.company = company;
        this.productImage = productImage;
        this.category = category;
    }

    public Product() {
    }
}
