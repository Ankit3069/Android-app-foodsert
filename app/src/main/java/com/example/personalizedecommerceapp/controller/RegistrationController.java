package com.example.personalizedecommerceapp.controller;

import static com.example.personalizedecommerceapp.util.Constants.COLUMN_CUISINE_CATEGORY_PREFERENCE;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_MAX_PRICE_PREFERENCE;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_MIN_PRICE_PREFERENCE;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_SHOP_TYPE_PREFERENCE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.UserMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.List;

public class RegistrationController implements IController<UserMaster> {

    private final Context context;
    private final DBHelper dbHelper;

    public RegistrationController(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }

    @Override
    public long save(UserMaster entity) {
        try {
            if (!checkIfUserExist(entity.getEmailId())) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = getContentValues(entity);
                return sqLiteDatabase.insert(Constants.TABLE_USER, null, contentValues);
            } else return Constants.ALREADY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public long update(UserMaster entity) {
        try {
            if (!checkIfUserExist_Exceptcurrent(entity.getEmailId(),entity.getUserId()+"")) {
                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                ContentValues contentValues = getContentValuesFORUPDATE(entity);
                return sqLiteDatabase.update(Constants.TABLE_USER,contentValues,Constants.COLUMN_USER_ID +" = '"+entity.getUserId()+"'",null);
            } else return Constants.ALREADY;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean delete(UserMaster entity) {
        return false;
    }

    @Override
    public List<UserMaster> getAll() {
        return null;
    }

    @Override
    public UserMaster getById(String id) {
        UserMaster u=null;

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + Constants.TABLE_USER + " where " + Constants.COLUMN_USER_ID
                        + " = ? ",
                new String[]{id});
        int count = cursor.getCount();
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.moveToFirst();
            //UserId fullName, emailId, password, contactNumber, address, shopTypePreference, cuisineCategoryPreference
            //minPricePreference, maxPricePreference
            u=new UserMaster();
            u.setUserId(cursor.getInt(0));
            u.setFullName(cursor.getString(1));
            u.setEmailId(cursor.getString(2));
            u.setPassword(cursor.getString(3));
            u.setContactNumber(cursor.getString(4));
            u.setAddress(cursor.getString(5));
            u.setShopTypePreference(cursor.getString(6));
            u.setCuisineCategoryPreference(cursor.getString(7));
            u.setMinPricePreference(cursor.getString(8));
            u.setMaxPricePreference(cursor.getString(9));
        }
        cursor.close();
        return u;
    }

    @Override
    public List<UserMaster> getByCondition(String whereClause, String[] clauseValue) {
        return null;
    }

    @Override
    public List<UserMaster> getByQuery(String query) {
        return null;
    }

    @NonNull
    private ContentValues getContentValues(UserMaster entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_FULL_NAME, entity.getFullName());
        contentValues.put(Constants.COLUMN_EMAIL_ID, entity.getEmailId());
        contentValues.put(Constants.COLUMN_PASSWORD, entity.getPassword());
        contentValues.put(Constants.COLUMN_CONTACT_NUMBER, entity.getContactNumber());
        contentValues.put(Constants.COLUMN_ADDRESS, entity.getAddress());
        return contentValues;
    }

    @NonNull
    private ContentValues getContentValuesFORUPDATE(UserMaster entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_FULL_NAME, entity.getFullName());
        contentValues.put(Constants.COLUMN_EMAIL_ID, entity.getEmailId());
        contentValues.put(Constants.COLUMN_CONTACT_NUMBER, entity.getContactNumber());
        contentValues.put(Constants.COLUMN_ADDRESS, entity.getAddress());
        return contentValues;
    }

    private boolean checkIfUserExist(String emailId) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + Constants.TABLE_USER + " where " + Constants.COLUMN_EMAIL_ID
                        + " = ? ",
                new String[]{emailId});

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
    }

    private boolean checkIfUserExist_Exceptcurrent(String emailId,String uid) {

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "
                        + Constants.TABLE_USER + " where " + Constants.COLUMN_EMAIL_ID
                        + " = ? AND "+ Constants.COLUMN_USER_ID + " <> ?",
                new String[]{emailId,uid});

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
    }

    public Cursor getUserPreference(String id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select "+COLUMN_SHOP_TYPE_PREFERENCE+","+COLUMN_CUISINE_CATEGORY_PREFERENCE+","+COLUMN_MIN_PRICE_PREFERENCE+","+COLUMN_MAX_PRICE_PREFERENCE+" from " + Constants.TABLE_USER + " where " + Constants.COLUMN_USER_ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }

    public void updatePreference(String id,String shop,String cusines,String min,String max)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_SHOP_TYPE_PREFERENCE,shop);
        contentValues.put(Constants.COLUMN_CUISINE_CATEGORY_PREFERENCE,cusines);
        contentValues.put(Constants.COLUMN_MIN_PRICE_PREFERENCE,min);
        contentValues.put(Constants.COLUMN_MAX_PRICE_PREFERENCE, max);
        db.update(Constants.TABLE_USER,contentValues,Constants.COLUMN_USER_ID + " = '" + id + "'",null);

    }

    public void changepass(String pass,String uid) {
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_PASSWORD, pass);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Constants.TABLE_USER, values, Constants.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(uid)});
    }
}
