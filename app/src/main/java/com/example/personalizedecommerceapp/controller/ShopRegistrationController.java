package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.List;

public class ShopRegistrationController implements IController<Shop> {

    private final Context context;
    private final DBHelper dbHelper;

    public ShopRegistrationController(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public long save(Shop entity) {
        try {
            if (!checkIfShopExist(entity.getEmail())) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = getContentValues(entity);
                return sqLiteDatabase.insert(Constants.TABLE_SHOP, null, contentValues);
            } else return Constants.ALREADY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long update(Shop entity) {
        return -1;
    }

    @Override
    public boolean delete(Shop entity) {
        return false;
    }

    @Override
    public List<Shop> getAll() {
        return null;
    }

    @Override
    public Shop getById(String id) {
        return null;
    }

    @Override
    public List<Shop> getByCondition(String whereClause, String[] clauseValue) {
        return null;
    }

    @Override
    public List<Shop> getByQuery(String query) {
        return null;
    }

    @NonNull
    private ContentValues getContentValues(Shop entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_EMAIL, entity.getEmail());
        contentValues.put(Constants.COLUMN_PASSWORD, entity.getPass());
        return contentValues;
    }

    private boolean checkIfShopExist(String emailId) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + Constants.TABLE_SHOP + " where " + Constants.COLUMN_EMAIL
                        + " = ? ",
                new String[]{emailId});

        int count = cursor.getCount();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID));
            if (studentId > 0) {
                UserPref.setUserId(context, studentId);
                UserPref.setLoginUserType(context, Constants.USER);
            }
        }
        cursor.close();
        return count > 0;
    }


    public boolean authenticateShop(String userName, String password, String userType) {

        try {

                SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + Constants.TABLE_SHOP + " where " + Constants.COLUMN_EMAIL
                                + " = ? AND " + Constants.COLUMN_PASSWORD + " = ?",
                        new String[]{userName, password});
                int count = cursor.getCount();
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID));
                    if (studentId > 0) {
                        UserPref.setUserId(context, studentId);
                        UserPref.setLoginUserType(context, Constants.SHOP);
                    }
                }
                cursor.close();
                return count > 0;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public Cursor getProfileUser(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_SHOP + " where " + Constants.COLUMN_SHOP_ID + " = '" + id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }

    public void cpshop(Shop user) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_PASSWORD, user.getPass());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Constants.TABLE_SHOP, values, Constants.COLUMN_SHOP_ID + " = ?", new String[]{String.valueOf(user.getShopId())});
    }
}
