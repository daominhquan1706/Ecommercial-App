using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR;
using eCommercial;

namespace eCommercial.signalr.hubs
{
    public class CommentProductHub : Hub
    {
        DefaultConnectionEntities db = new DefaultConnectionEntities();
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
            string ngay = String.Format("{0:G}", DateTime.Now);
            Clients.All.addNewMessageToPage(content,db.Customers.Find(idcustomer).Avatar,db.Customers.Find(idcustomer).Last_Name, db.Customers.Find(idcustomer).First_Name, ngay);
        }
    }
}