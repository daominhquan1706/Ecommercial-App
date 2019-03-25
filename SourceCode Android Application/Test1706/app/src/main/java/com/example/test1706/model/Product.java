package com.example.test1706.model;

public class Product {
    private  String Product_Name;
    private  int Price;
    private String category;
    private String Image;
    private String Image_Night;
    private  String Description;

    private  int Quantity;
    private  int discount;

    public Product() {}

    public Product(String product_Name, int price, String category, String image) {
        Product_Name = product_Name;
        Price = price;
        this.category = category;
        Image = image;
    }

    public Product(String product_Name, int price, String category, String image, String image_Night, String description, int quantity, int discount) {
        Product_Name = product_Name;
        Price = price;
        this.category = category;
        Image = image;
        Image_Night = image_Night;
        Description = description;
        Quantity = quantity;
        this.discount = discount;
    }

    public String getImage_Night() {
        return Image_Night;
    }

    public void setImage_Night(String image_Night) {
        Image_Night = image_Night;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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
