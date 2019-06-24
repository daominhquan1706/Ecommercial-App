
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Firebase.Database;
using Firebase.Database.Query;
using System.Threading.Tasks;
using AdminR.Models;


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

        public async Task<List<Product>> GetAllAsync()
        {
            var firebaseClient = new FirebaseClient("https://ecommerial-40d25.firebaseio.com/");
            //Retrieve data from Firebase
            List<String> listCategory = new List<String>();
            listCategory.Add("ALPHA");
            listCategory.Add("HAWK");
            listCategory.Add("ICON-AUTO");
            listCategory.Add("ICON-QUARTZ");
            listCategory.Add("MARQUESS");
            listCategory.Add("MX10");
            List<Product> listproduct = new List<Product>();
            foreach (String category in listCategory)
            {
                var dbLogins = await firebaseClient
              .Child("NiteWatch")
              .Child(category)
              .OnceAsync<Product>();
                //Convert JSON data to original datatype
                foreach (var login in dbLogins)
                {
                    //timestampList.Add(Convert.ToDateTime(login.Object.TimestampUtc).ToLocalTime());
                    Product p = new Product();
                    p.category = Convert.ToString(login.Object.category);
                    p.createDate = Convert.ToInt64(login.Object.createDate);
                    p.Description = Convert.ToString(login.Object.Description);
                    p.discount = Convert.ToInt16(login.Object.discount);
                    p.Image = Convert.ToString(login.Object.Image);
                    p.Image_Night = Convert.ToString(login.Object.Image_Night);
                    p.Price = Convert.ToInt16(login.Object.Price);
                    p.Product_Name = Convert.ToString(login.Object.Product_Name);
                    p.Quantity = Convert.ToInt16(login.Object.Quantity);
                    listproduct.Add(p);
                }
            }
            return listproduct;
        }


        public Product()
        {

        }

    }
}