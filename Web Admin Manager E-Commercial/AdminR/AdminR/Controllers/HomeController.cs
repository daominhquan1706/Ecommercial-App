using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Firebase.Database;
using Firebase.Database.Query;
using System.Threading.Tasks;
using AdminR.Models;
using System.Globalization;

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
                foreach (var product in dbLogins)
                {
                    //timestampList.Add(Convert.ToDateTime(login.Object.TimestampUtc).ToLocalTime());
                    Product p = product.Object;
                    listproduct.Add(p);
                }
            }


            //Pass data to the view
            ViewBag.CurrentUser = userId;
            ViewBag.ListProduct = listproduct;
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
            foreach (var product in dbLogins)
            {
                //timestampList.Add(Convert.ToDateTime(login.Object.TimestampUtc).ToLocalTime());
                Product p = product.Object;
                listproduct.Add(p);
            }

            //Pass data to the view
            ViewBag.CurrentUser = userId;
            ViewBag.ListProduct = listproduct;
            return View();
        }
        public async Task<ActionResult> QuanLyHoaDon()
        {
            //Save non identifying data to Firebase
            var firebaseClient = new FirebaseClient("https://ecommerial-40d25.firebaseio.com/");
            //var result = await firebaseclient
            //  .child("users/" + userid + "/logins")
            //  .postasync(currentuserlogin);

            //Retrieve data from Firebase
            var dbLogins = await firebaseClient
              .Child("Orders")
              .OnceAsync<Order>();

            List<Order> listHoaDon = new List<Order>();

            //Convert JSON data to original datatype
            foreach (var order in dbLogins)
            {
                //timestampList.Add(Convert.ToDateTime(login.Object.TimestampUtc).ToLocalTime());
                Order p = new Order();

                p = order.Object;
                listHoaDon.Add(p);
            }
            ViewBag.List = listHoaDon;
            return View();
        }

        public ActionResult Contact()
        {
            ViewBag.Message = "Your contact page.";

            return View();
        }



    }
}