package com.example.test1706.model;

public class Product {
    private  String Product_Name;
    private  int Price;
    private String category;
    private String Image;

    public Product() {
    }

    public Product(String product_Name, int price, String category, String image) {
        Product_Name = product_Name;
        Price = price;
        this.category = category;
        Image = image;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
