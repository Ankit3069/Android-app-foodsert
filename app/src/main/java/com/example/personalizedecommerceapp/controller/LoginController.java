package com.example.personalizedecommerceapp.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalizedecommerceapp.interfaces.ILoginController;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;

public class LoginController<T> implements ILoginController<T> {


    private final Context context;
    private final DBHelper dbHelper;

    public LoginController(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }

    @Override
    public boolean authenticateUser(String userName, String password, String userType) {

        try {
            if (userType.equals(Constants.USER)) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + Constants.TABLE_USER + " where " + Constants.COLUMN_EMAIL_ID
                                + " = ? AND " + Constants.COLUMN_PASSWORD + " = ?",
                        new String[]{userName, password});

                int count = cursor.getCount();
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID));
                    if (studentId > 0) {
                        UserPref.setUserId(context, studentId);
                        UserPref.setLoginUserType(context, Constants.USER);
                    }
                }
                cursor.close();
                return count > 0;
            } else if (userType.equals(Constants.SHOP)) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                                + Constants.TABLE_SHOP + " where " + Constants.COLUMN_EMAIL_ID
                                + " = ? AND " + Constants.COLUMN_PASSWORD + " = ?",
                        new String[]{userName, password});
                int count = cursor.getCount();
                if (cursor.moveToFirst() && cursor.getCount() > 0) {
                    int studentId = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID));
                    if (studentId > 0) {
                        UserPref.setUserId(context, studentId);
                        UserPref.setLoginUserType(context, Constants.SHOP);
                    }
                }
                cursor.close();
                return count > 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
