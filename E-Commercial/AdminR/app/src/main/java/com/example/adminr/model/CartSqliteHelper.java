package com.example.adminr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class CartSqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 2;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Cart_Manager";


    // Tên bảng: Cart.
    private static final String TABLE_CART = "Cart";
    private static final String COLUMN_CART_ID = "Id";
    private static final String COLUMN_CART_PRODUCTNAME = "ProductName";
    private static final String COLUMN_CART_CART_QUANTITY = "Quantity";
    private static final String COLUMN_CART_CART_PRICE = "Price";
    private static final String COLUMN_CART_IMAGEPRODUCT = "ImageProduct";
    private static final String COLUMN_CART_CATEGORY = "Category";


    public CartSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "CartSqliteHelper.onCreate ... ");
        // Script tạo bảng.
        String script =
                "CREATE TABLE " + TABLE_CART + "("
                        + COLUMN_CART_ID + " INTEGER PRIMARY KEY,"
                        + COLUMN_CART_PRODUCTNAME + " TEXT,"
                        + COLUMN_CART_CART_QUANTITY + " INT,"
                        + COLUMN_CART_CART_PRICE + " REAL,"
                        + COLUMN_CART_IMAGEPRODUCT + " TEXT,"
                        + COLUMN_CART_CATEGORY + " TEXT"

                        + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "CartSqliteHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);


        // Và tạo lại.
        onCreate(db);
    }

    // Nếu trong bảng Cart chưa có dữ liệu,
    // Trèn vào mặc định 2 bản ghi.
    public void createDefaultCartsIfNeed() {
        int count = this.getCartQuantityCount();
        if (count == 0) {
            Product product = new Product(
                    "name",
                    0,
                    "a",
                    "A",
                    "A");
            this.addCart(product);
        }
    }


    public void addCart(Product product) {
        Log.i(TAG, "CartSqliteHelper.addCart ... " + product.getProduct_Name());
        if (!CheckExists(product)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_CART_PRODUCTNAME, product.getProduct_Name());
            values.put(COLUMN_CART_CART_QUANTITY, 1);
            values.put(COLUMN_CART_CART_PRICE, product.getPrice());
            values.put(COLUMN_CART_IMAGEPRODUCT, product.getImage());
            values.put(COLUMN_CART_CATEGORY, product.getCategory());

            // Trèn một dòng dữ liệu vào bảng.
            db.insert(TABLE_CART, null, values);
            // Đóng kết nối database.
            db.close();
        } else {
            PlusOneQuantity(product);
        }
    }


    public Cart getCart(Product product) {
        Log.i(TAG, "CartSqliteHelper.getCart ... " + product.getProduct_Name());

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART, new String[]{
                        COLUMN_CART_ID,
                        COLUMN_CART_PRODUCTNAME,
                        COLUMN_CART_CART_QUANTITY,
                        COLUMN_CART_CART_PRICE,
                        COLUMN_CART_IMAGEPRODUCT,
                        COLUMN_CART_CATEGORY

                }, COLUMN_CART_PRODUCTNAME + "=?",
                new String[]{product.getProduct_Name()}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Cart Cart = new Cart();
        Cart.setId(Integer.parseInt(cursor.getString(0)));
        Cart.setProductName(cursor.getString(1));
        Cart.setQuantity(Integer.parseInt(cursor.getString(2)));
        Cart.setPrice(Double.parseDouble(cursor.getString(3)));
        Cart.setImageProduct(cursor.getString(4));
        Cart.setCategory(cursor.getString(5));
        // return Cart
        return Cart;
    }


    public List<Cart> getAllCarts() {
        Log.i(TAG, "CartSqliteHelper.getAllCarts ... ");

        List<Cart> CartList = new ArrayList<Cart>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Cart Cart = new Cart();
                Cart.setId(Integer.parseInt(cursor.getString(0)));
                Cart.setProductName(cursor.getString(1));
                Cart.setQuantity(Integer.parseInt(cursor.getString(2)));
                Cart.setPrice(Double.parseDouble(cursor.getString(3)));
                Cart.setImageProduct(cursor.getString(4));
                Cart.setCategory(cursor.getString(5));

                // Thêm vào danh sách.
                CartList.add(Cart);
            } while (cursor.moveToNext());
        }

        // return Cart list
        return CartList;
    }

    public int getCartQuantityCount() {
        Log.i(TAG, "CartSqliteHelper.getCartsCount ... ");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = 0;
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                count = count + Integer.parseInt(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return count
        return count;
    }

    public double getCartPriceCount() {
        Log.i(TAG, "CartSqliteHelper.getCartsCount ... ");

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        double count = 0;
        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                int quantity = Integer.parseInt(cursor.getString(2));
                count = count + (Double.parseDouble(cursor.getString(3)) * quantity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return count
        return count;
    }


    public boolean CheckExists(Product product) {
        String[] columns = {COLUMN_CART_PRODUCTNAME};
        String selection = COLUMN_CART_PRODUCTNAME + " =?";
        String[] selectionArgs = {product.getProduct_Name()};
        String limit = "1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void PlusOneQuantity(Product product) {
        Log.i(TAG, "CartSqliteHelper.updateCart ... " + product.getProduct_Name());

        SQLiteDatabase db = this.getWritableDatabase();
        Cart cart = getCart(product);
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_CART_PRICE, cart.getPrice());
        values.put(COLUMN_CART_CART_QUANTITY, cart.getQuantity() + 1);
        values.put(COLUMN_CART_IMAGEPRODUCT, cart.getImageProduct());
        values.put(COLUMN_CART_PRODUCTNAME, cart.getProductName());
        values.put(COLUMN_CART_CATEGORY, cart.getCategory());
        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cart.getId())});
    }

    public void MinusOneQuantity(Product product) {
        Log.i(TAG, "CartSqliteHelper.updateCart ... " + product.getProduct_Name());

        SQLiteDatabase db = this.getWritableDatabase();
        Cart cart = getCart(product);
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_CART_PRICE, cart.getPrice());
        if (cart.getQuantity() > 1) {
            values.put(COLUMN_CART_CART_QUANTITY, cart.getQuantity() - 1);
        } else {
            deleteCart(cart);
            return;
        }

        values.put(COLUMN_CART_IMAGEPRODUCT, cart.getImageProduct());
        values.put(COLUMN_CART_PRODUCTNAME, cart.getProductName());
        values.put(COLUMN_CART_CATEGORY, cart.getCategory());

        db.update(TABLE_CART, values, COLUMN_CART_ID + " = ?", new String[]{String.valueOf(cart.getId())});
    }

    public void deleteCart(Cart cart) {
        Log.i(TAG, "CartSqliteHelper.updateCart ... " + cart.getProductName());

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_CART_ID + " = ?",
                new String[]{String.valueOf(cart.getId())});
        db.close();
    }


}
