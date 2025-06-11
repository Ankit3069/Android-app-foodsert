package com.example.personalizedecommerceapp.controller;

import static com.example.personalizedecommerceapp.util.Constants.COLUMN_ORDER_ID_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_USER_ID_ORDER;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderController {
    private final DBHelper dbHelper;

    public ShopOrderController (Context context) {
        this.dbHelper = new DBHelper(context);
    }


    public List<Order> getAll(String shpid) {
        List<Order> discountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor = null;
        String selection=null;
        String[] selectionArgs=null;
        try {
            selection = "shopid = ?  " ;
            // Define the selection arguments
            selectionArgs = new String[]{shpid};
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query(Constants.TABLE_ORDER, null,
                    selection, selectionArgs, null, null, COLUMN_ORDER_ID_ORDER+" DESC");
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

    public List<Order> getAll_UserId(String uid) {
        List<Order> discountList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase;
        Cursor cursor = null;
        String selection=null;
        String[] selectionArgs=null;
        try {
            selection = ""+COLUMN_USER_ID_ORDER+" = ?  " ;
            // Define the selection arguments
            selectionArgs = new String[]{uid};
            sqLiteDatabase = dbHelper.getReadableDatabase();
            cursor = sqLiteDatabase.query(Constants.TABLE_ORDER, null,
                    selection, selectionArgs, null, null, COLUMN_ORDER_ID_ORDER+" DESC");
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

    private Order getProductFromCursor(Cursor cursor) {
        Order discount = new Order();
        discount.setOrderId(Integer.parseInt(String.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(Constants.COLUMN_ORDER_ID_ORDER)))));
        discount.setUserId(Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID_ORDER))));
        discount.setShopid(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID_ORDER)));
        discount.setGrandTotal(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_GRAND_TOTAL)));
        discount.setDateTime(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_DATE_TIME)));
        discount.setStatus(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_STATUS)));

        return discount;
    }

    public Cursor getOrderDetails(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_ORDER_PRODUCT + " where " + Constants.COLUMN_ORDER_ID + " = '" + id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }
    public Cursor getProductDetails(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_PRODUCT + " where " + Constants.COLUMN_PRODUCT_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }
    public Cursor getUserDetails(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_USER + " where " + Constants.COLUMN_USER_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }

    public Cursor getShopDetails(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_SHOP + " where " + Constants.COLUMN_SHOP_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }

    public void changeorderstatus(Order user) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_STATUS, user.getStatus());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Constants.TABLE_ORDER, values, Constants.COLUMN_ORDER_ID_ORDER + " = ?", new String[]{String.valueOf(user.getOrderId())});
    }

}
