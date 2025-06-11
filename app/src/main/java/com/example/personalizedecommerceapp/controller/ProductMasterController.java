package com.example.personalizedecommerceapp.controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ProductMasterController {

    private final DBHelper dbHelper;

    public ProductMasterController(Context context) {
        this.dbHelper = new DBHelper(context);
    }

    public long saveOrUpdate(ProductMaster entity) {
        boolean isSuccess = false;
        long result = Constants.ERROR_SQL_RESULT;
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = getContentValues(entity);
            Log.d("entity", String.valueOf(getContentValues(entity)));
            result = db.insertWithOnConflict(Constants.TABLE_PRODUCT, null, contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);

        } finally {
            dbHelper.endTransaction(db);
        }
        return result;
    }

    @SuppressLint("SuspiciousIndentation")
    @NonNull
    private static ContentValues getContentValues(ProductMaster card) {
        ContentValues contentValues = new ContentValues();
        if (card.getProductId() > 0)
            contentValues.put(Constants.COLUMN_PRODUCT_ID, card.getProductId());
        contentValues.put(Constants.COLUMN_PRODUCT_NAME, card.getProductName());
        contentValues.put(Constants.COLUMN_PRODUCT_IMAGE, card.getProductImage());
        contentValues.put(Constants.COLUMN_PRODUCT_DESCRIPTION, card.getDescription());
        contentValues.put(Constants.COLUMN_PRODUCT_PRICE, card.getProductPrice());
        contentValues.put(Constants.COLUMN_SHOP_ID_PRODUCT, card.getShopId());
        return contentValues;
    }

    public List<ProductMaster> getAll() {
        List<ProductMaster> discountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor = null;
        try {
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query(Constants.TABLE_PRODUCT, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext())
                    discountList.add(getProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return discountList;
    }

    public List<ProductMaster> getByShopId(int shopId) {
        List<ProductMaster> discountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor = null;
        try {
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query(Constants.TABLE_PRODUCT, null,
                    Constants.COLUMN_SHOP_ID + " =? ", new String[]{String.valueOf(shopId)},
                    null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext())
                    discountList.add(getProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            dbHelper.close();
        }
        return discountList;
    }

    private ProductMaster getProductFromCursor(Cursor cursor) {
        ProductMaster discount = new ProductMaster();
        discount.setProductId(Integer.parseInt(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_ID)))));
        discount.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_NAME)));
        discount.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_IMAGE)));
        discount.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_DESCRIPTION)));
        discount.setProductPrice(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_PRICE)));
        discount.setShopId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID_PRODUCT))));

        return discount;
    }

    public Cursor getProductMasterDetails(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_PRODUCT + " where " + Constants.COLUMN_SHOP_ID + " = '" + id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }

    public boolean delete(long offerid) {
        int result = 0;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        try {
            // Finally, delete the product from TABLE_PRODUCT
            result = sqLiteDatabase.delete(Constants.TABLE_PRODUCT, Constants.COLUMN_PRODUCT_ID + " =?", new String[]{String.valueOf(offerid)});

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result > -1;
    }
}
