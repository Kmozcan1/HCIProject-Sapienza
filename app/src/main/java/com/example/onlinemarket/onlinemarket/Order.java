package com.example.onlinemarket.onlinemarket;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Order implements Serializable{

    private String userEmail;
    private Double TotalPrice;
    private HashMap<Product,Integer> products;  //integer is quantity
    private String zone;
    private String address;
    private String Time;
    private String companyName;

    public Order(String userEmail, String address, String zone, String companyName) {
        this.userEmail = userEmail;
        this.address= address;
        this.zone= zone;
        this.companyName= companyName;
        TotalPrice = 0.0;
        this.products = new HashMap<>();
    }
    public Order(){

    }
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        TotalPrice = totalPrice;
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }
    public String getTime() {
        return Time;
    }

    public void setTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date();
        Time = sdfDate.format(now);
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        zone = zone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void InsertProduct(Product product){
        if(products.containsKey(product))
            products.put(product, products.get(product).intValue() + 1);
        else
            products.put(product,1);
        setTotalPrice(Math.round((getTotalPrice()+ product.price)*100.0)/100.0);
    }
    public void DeleteProduct(Product product){
        if(products.get(product)==1)
            products.remove(product);
        else
            products.put(product,products.get(product).intValue() -1);
        setTotalPrice(Math.round((getTotalPrice()- product.price)*100.0)/100.0);
    }
}
