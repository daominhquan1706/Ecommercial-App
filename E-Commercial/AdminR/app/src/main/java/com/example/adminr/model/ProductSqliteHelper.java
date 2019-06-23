package com.example.adminr.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class ProductSqliteHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Phiên bản
    private static final int DATABASE_VERSION = 3;


    // Tên cơ sở dữ liệu.
    private static final String DATABASE_NAME = "Product_Manager";


    // Tên bảng: Product.
    private static final String TABLE_CART = "Product";
    private static final String COLUMN_PRODUCT_PRODUCTNAME = "ProductName";
    private static final String COLUMN_PRODUCT_PRICE = "Price";
    private static final String COLUMN_PRODUCT_IMAGE = "Image";
    private static final String COLUMN_PRODUCT_IMAGE_NIGHT = "ImageNight";
    private static final String COLUMN_PRODUCT_DESCRIPTION = "ImageProduct";
    private static final String COLUMN_PRODUCT_CATEGORY = "Category";
    private static final String COLUMN_PRODUCT_CREATIONTIME = "createDate";


    public ProductSqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Tạo các bảng.
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "ProductSqliteHelper.onCreate ... ");
        // Script tạo bảng.
        String script =
                "CREATE TABLE " + TABLE_CART + "("
                        + COLUMN_PRODUCT_PRODUCTNAME + " TEXT,"
                        + COLUMN_PRODUCT_PRICE + " INT,"
                        + COLUMN_PRODUCT_IMAGE + " TEXT,"
                        + COLUMN_PRODUCT_IMAGE_NIGHT + " TEXT,"
                        + COLUMN_PRODUCT_DESCRIPTION + " TEXT,"
                        + COLUMN_PRODUCT_CATEGORY + " TEXT,"
                        + COLUMN_PRODUCT_CREATIONTIME + " LONG"

                        + ")";
        // Chạy lệnh tạo bảng.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "ProductSqliteHelper.onUpgrade ... ");

        // Hủy (drop) bảng cũ nếu nó đã tồn tại.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);

        // Và tạo lại.
        onCreate(db);
    }


    public void addProduct(Product product) {
        Log.i(TAG, "ProductSqliteHelper.addProduct ... " + product.getProduct_Name());
        if (!CheckExists(product)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_PRODUCT_PRODUCTNAME, product.getProduct_Name());
            values.put(COLUMN_PRODUCT_PRICE, product.getPrice());
            values.put(COLUMN_PRODUCT_IMAGE, product.getImage());
            values.put(COLUMN_PRODUCT_IMAGE_NIGHT, product.getImage_Night());
            values.put(COLUMN_PRODUCT_DESCRIPTION, product.getDescription());
            values.put(COLUMN_PRODUCT_CATEGORY, product.getCategory());
            values.put(COLUMN_PRODUCT_CREATIONTIME, System.currentTimeMillis());

            // Trèn một dòng dữ liệu vào bảng.
            db.insert(TABLE_CART, null, values);
            // Đóng kết nối database.
            db.close();
        } else {
            deleteProduct(product);
            addProduct(product);
        }
    }


    public Product getProduct(String productName) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(TABLE_CART, new String[]{
                        COLUMN_PRODUCT_PRODUCTNAME,
                        COLUMN_PRODUCT_PRICE,
                        COLUMN_PRODUCT_IMAGE,
                        COLUMN_PRODUCT_IMAGE_NIGHT,
                        COLUMN_PRODUCT_DESCRIPTION,
                        COLUMN_PRODUCT_CATEGORY

                }, COLUMN_PRODUCT_PRODUCTNAME + "=?",
                new String[]{productName}, null, null, null, null);
        Product product = new Product();
        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.getString(0).isEmpty()) {
                product.setProduct_Name((cursor.getString(0)));
                product.setPrice(Integer.parseInt(cursor.getString(1)));
                product.setImage(cursor.getString(2));
                product.setImage_Night(cursor.getString(3));
                product.setDescription(cursor.getString(4));
                product.setCategory(cursor.getString(5));
            }
            //Product.setCreateDate(Long.parseLong(cursor.getString(6)));
        }
        // return Product
        return product;
    }


    public List<Product> getAllProducts() {
        Log.i(TAG, "ProductSqliteHelper.getAllProducts ... ");

        List<Product> ProductList = new ArrayList<Product>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CART;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        // Duyệt trên con trỏ, và thêm vào danh sách.
        if (cursor.moveToFirst()) {
            do {
                Product Product = new Product();
                Product.setProduct_Name((cursor.getString(0)));
                Product.setPrice(Integer.parseInt(cursor.getString(1)));
                Product.setImage(cursor.getString(2));
                Product.setImage_Night(cursor.getString(3));
                Product.setDescription(cursor.getString(4));
                Product.setCategory(cursor.getString(5));
                Product.setCreateDate(Long.parseLong(cursor.getString(6)));


                // Thêm vào danh sách.
                ProductList.add(Product);
            } while (cursor.moveToNext());
        }

        // return Product list
        return ProductList;
    }

    public int getProductQuantityCount() {
        Log.i(TAG, "ProductSqliteHelper.getProductsCount ... ");

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

    public double getProductPriceCount() {
        Log.i(TAG, "ProductSqliteHelper.getProductsCount ... ");

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
        String[] columns = {COLUMN_PRODUCT_PRODUCTNAME};
        String selection = COLUMN_PRODUCT_PRODUCTNAME + " =?";
        String[] selectionArgs = {product.getProduct_Name()};
        String limit = "1";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void deleteProduct(Product product) {
        if(!product.getProduct_Name().isEmpty()){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_CART, COLUMN_PRODUCT_PRODUCTNAME + " = ?",
                    new String[]{String.valueOf(product.getProduct_Name())});
            db.close();
        }
    }

}
