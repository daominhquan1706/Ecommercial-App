using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AdminR.Models
{
    public class Cart
    {
        public int Id { get; set; }
        public String ProductName { get; set; }
        public int Quantity { get; set; }
        public double Price { get; set; }
        public String ImageProduct { get; set; }
        public String Category { get; set; }
        public bool daBinhLuan { get; set; }
    }
}