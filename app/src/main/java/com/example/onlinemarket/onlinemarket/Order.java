package com.example.onlinemarket.onlinemarket;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order implements Serializable{

    private String userEmail;
    private Double TotalPrice;
    private String orderKey;
    private HashMap<Product,Integer> products;  //integer is quantity , String is name
    private String zone;
    private String address;
    private String Time;
    private String companyName;
    private Boolean isDone;
    private String companyImage;
    private ArrayList<OrderedProductData> orderedProducts;

    public Order(String orderKey, Boolean isDone, String userEmail, String address, String zone,
                 String companyName, Double totalPrice, String companyImage, ArrayList<OrderedProductData> orderedProducts, String time) {
        this.orderKey = orderKey;
        this.isDone=false;
        this.userEmail = userEmail;
        this.address= address;
        this.zone= zone;
        this.companyName= companyName;
        TotalPrice = totalPrice;
        this.orderedProducts = orderedProducts;
        this.companyImage = companyImage;
        this.Time = time;
        this.isDone = isDone;
    }

    public Order(String orderKey, Boolean isDone, String userEmail, String address, String zone,
                 String companyName, Double totalPrice, ArrayList<OrderedProductData> orderedProducts, String time) {
        this.orderKey = orderKey;
        this.isDone=false;
        this.userEmail = userEmail;
        this.address= address;
        this.zone= zone;
        this.companyName= companyName;
        TotalPrice = totalPrice;
        this.products = new HashMap<>();
        this.orderedProducts = orderedProducts;
        this.Time = time;
        this.isDone = isDone;
    }

    public Order(String orderKey, String userEmail, String address, String zone, String companyName) {
        this.orderKey = orderKey;
        this.isDone=false;
        this.userEmail = userEmail;
        this.address= address;
        this.zone= zone;
        this.companyName= companyName;
        TotalPrice = 0.0;
        this.products = new HashMap<>();
    }
    public Order( String userEmail, String address, String zone, String companyName) {
        this.isDone=false;
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

    public void setDone(Boolean done) {this.isDone = done;}

    public Boolean getDone() {return isDone;}
    public String getOrderKey() {
        return orderKey;
    }
    public String getCompanyImage() { return companyImage; }
    public ArrayList<OrderedProductData> getOrderedProducts() { return orderedProducts; }

}
