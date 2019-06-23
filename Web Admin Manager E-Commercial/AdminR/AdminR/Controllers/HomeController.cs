using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Firebase.Database;
using Firebase.Database.Query;
using System.Threading.Tasks;
using AdminR.Models;


namespace AdminR.Controllers
{
    public class HomeController : Controller
    {
        public async Task<ActionResult> Index()
        {
            //Simulate test user data and login timestamp
            var userId = "12345";
            var currentLoginTime = DateTime.UtcNow.ToString("MM/dd/yyyy HH:mm:ss");

            //Save non identifying data to Firebase
            //var currentUserLogin = new LoginData() { TimestampUtc = currentLoginTime };
            var firebaseClient = new FirebaseClient("https://ecommerial-40d25.firebaseio.com/");
            //var result = await firebaseclient
            //  .child("users/" + userid + "/logins")
            //  .postasync(currentuserlogin);

            //Retrieve data from Firebase
            var dbLogins = await firebaseClient
              .Child("NiteWatch")
              .Child("ALPHA")
              .OnceAsync<Product>();

            var listproduct = new List<Product>();

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

            //Pass data to the view
            ViewBag.CurrentUser = userId;
            ViewBag.ListProduct = listproduct.OrderByDescending(x => x.Price);
            return View();
        }

        public async Task<ActionResult> About()
        {
            //Simulate test user data and login timestamp
            var userId = "12345";
            var currentLoginTime = DateTime.UtcNow.ToString("MM/dd/yyyy HH:mm:ss");

            //Save non identifying data to Firebase
            //var currentUserLogin = new LoginData() { TimestampUtc = currentLoginTime };
            var firebaseClient = new FirebaseClient("https://ecommerial-40d25.firebaseio.com/");
            //var result = await firebaseclient
            //  .child("users/" + userid + "/logins")
            //  .postasync(currentuserlogin);

            //Retrieve data from Firebase
            var dbLogins = await firebaseClient
              .Child("NiteWatch")
              .Child("ALPHA")
              .OnceAsync<Product>();

            List<Product> listproduct = new List<Product>();

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

            //Pass data to the view
            ViewBag.CurrentUser = userId;
            ViewBag.ListProduct = listproduct;
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }
    }
}