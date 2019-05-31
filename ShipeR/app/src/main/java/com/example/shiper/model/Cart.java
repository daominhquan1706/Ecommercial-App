package com.example.shiper.model;

public class Cart {
    private int Id;
    private String ProductName;
    private int Quantity;
    private double Price;
    private String ImageProduct;
    private String Category;
    private boolean daBinhLuan;

    public boolean isDaBinhLuan() {
        return daBinhLuan;
    }

    public void setDaBinhLuan(boolean daBinhLuan) {
        this.daBinhLuan = daBinhLuan;
    }

    public Cart() {
    }

    public Cart(
            int id,
            String productName,
            int quantity,
            double price,
            String imageProduct,
            String category) {
        Id = id;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        ImageProduct = imageProduct;
        Category = category;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }


    public String getImageProduct() {
        return ImageProduct;
    }

    public void setImageProduct(String imageProduct) {
        ImageProduct = imageProduct;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }
}
