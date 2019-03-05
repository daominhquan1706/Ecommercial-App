using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR;
using eCommercial;

namespace eCommercial.signalr.hubs
{
    public class eCommercialHubs : Hub
    {
        DefaultConnectionEntities db = new DefaultConnectionEntities();
        //view truyền vào
        public void Comment(string content,int idproduct,string idcustomer)
        {
            Comment_Product cmt = new Comment_Product();
            cmt.Comment_Content = content;
            cmt.ID_Product = idproduct;
            cmt.CreationTime = DateTime.Now;
            cmt.IsDeleted = false;
            cmt.ID_Customer = idcustomer;
            db.Comment_Product.Add(cmt);
            db.SaveChanges();
            int count = db.Products.Find(idproduct).Comment_Product.Count();
            string ngay = String.Format("{0:G}", DateTime.Now);
            //hàm truyền ra view
            Clients.All.addNewMessageToPage(content,db.Customers.Find(idcustomer).Avatar,db.Customers.Find(idcustomer).Last_Name, db.Customers.Find(idcustomer).First_Name, ngay,count);
        }
        public void Like(int idproduct,string idcustomer)
        {
            List<Product_Like> dblike = db.Product_Like.Where(q => q.ID_Customer == idcustomer && q.ID_Product == idproduct).ToList();
            if (dblike.Count() ==0)
            {
                Product_Like like = new Product_Like();
                like.ID_Product = idproduct;
                like.ID_Customer = idcustomer;
                like.CreationTime = DateTime.Now;
                db.Product_Like.Add(like);
            }
            else
            {
                db.Product_Like.Remove(db.Product_Like.Find(dblike[0].Id));
            }
            db.SaveChanges();
            Clients.All.ChangeLikeButton();

        }
    }
}