
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AdminR.Models
{
    public class Product
    {
        public String Product_Name { get; set; }
        public int Price { get; set; }
        public String category { get; set; }
        public String Image { get; set; }
        public String Image_Night { get; set; }
        public String Description { get; set; }
        public int Quantity { get; set; }
        public int discount { get; set; }
        public long createDate { get; set; }

        public Product(string product_Name, int price, string category, string image, string image_Night, string description, int quantity, int discount, long createDate)
        {
            Product_Name = product_Name;
            Price = price;
            this.category = category;
            Image = image;
            Image_Night = image_Night;
            Description = description;
            Quantity = quantity;
            this.discount = discount;
            this.createDate = createDate;
        }

        public Product()
        {

        }

    }
}