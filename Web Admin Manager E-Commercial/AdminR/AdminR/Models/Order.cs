using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AdminR.Models
{
    public class Order
    {
        public long creationTime { get; set; }
        public double total { get; set; }
        public String userID { get; set; }
        public String customerName { get; set; }
        public String customerAddress { get; set; }
        public String customerPhoneNumber { get; set; }
        public String status { get; set; }
        public List<Cart> orderDetails { get; set; }
        public String paymentid { get; set; }
        public Double Address_Lat { get; set; }
        public Double Address_Lng { get; set; }
        public List<String> Timeline { get; set; }
        public String shiper_uid { get; set; }
        public String shiper_email { get; set; }
    }
}