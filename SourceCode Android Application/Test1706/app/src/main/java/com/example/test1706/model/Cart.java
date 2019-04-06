package com.example.test1706.model;

public class Cart {
    private int Id;
    private String ProductName;
    private int Quantity;
    private double Price;
    private String ImageProduct;

    public Cart() {
    }

    public Cart(
            int             id,
            String          productName,
            int             quantity,
            double          price,
            String          imageProduct)
    {
        Id = id;
        ProductName = productName;
        Quantity = quantity;
        Price = price;
        ImageProduct = imageProduct;
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
}
