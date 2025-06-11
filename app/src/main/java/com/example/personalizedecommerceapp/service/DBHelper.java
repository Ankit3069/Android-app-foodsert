package com.example.personalizedecommerceapp.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import static com.example.personalizedecommerceapp.util.Constants.*;

import com.example.personalizedecommerceapp.util.Constants;


public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FULL_NAME + " VARCHAR(255), "
                + COLUMN_EMAIL_ID + " VARCHAR(255), "
                + COLUMN_PASSWORD + " VARCHAR(255), "
                + COLUMN_CONTACT_NUMBER + " VARCHAR(255), "
                + COLUMN_ADDRESS + " VARCHAR(255), "
                + COLUMN_SHOP_TYPE_PREFERENCE + " VARCHAR(255), "
                + COLUMN_CUISINE_CATEGORY_PREFERENCE + " VARCHAR(255), "
                + COLUMN_MIN_PRICE_PREFERENCE + " VARCHAR(255), "
                + COLUMN_MAX_PRICE_PREFERENCE + " VARCHAR(255)" + ")";

        String CREATE_TABLE_SHOP = "CREATE TABLE " + TABLE_SHOP + " ("
                + COLUMN_SHOP_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SHOP_NAME + " VARCHAR(255), "
                + COLUMN_SHOP_URL + " VARCHAR(255), "
                + COLUMN_CITY + " VARCHAR(255), "
                + COLUMN_REGION + " VARCHAR(255), "
                + COLUMN_SHOP_TYPE + " VARCHAR(255), "
                + COLUMN_CUISINE_CATEGORY + " VARCHAR(255), "
                + COLUMN_LATITUDE + " VARCHAR(255), "
                + COLUMN_LONGITUDE + " VARCHAR(255), "
                + COLUMN_DESCRIPTION + " VARCHAR(255), "
                + COLUMN_TIMING + " VARCHAR(255), "
                + COLUMN_PRICE + " VARCHAR(255), "
                + COLUMN_EMAIL + " VARCHAR(255), "
                + COLUMN_PASSWORD + " VARCHAR(255), "
                + COLUMN_RATING + " VARCHAR(255)" + ")";

        String CREATE_TABLE_RATINGS = "CREATE TABLE " + TABLE_RATINGS + " ("
                + COLUMN_SHOP_ID_RATINGS + " INTEGER  , "
                + COLUMN_USER_ID_RATINGS + " INTEGER, "
                + COLUMN_RATING_COUNT + " INTEGER, "
                + COLUMN_REVIEW + " VARCHAR(255), "
                + "PRIMARY KEY (" + COLUMN_SHOP_ID_RATINGS + ", "
                + COLUMN_USER_ID_RATINGS + "), "
                + "FOREIGN KEY (" + COLUMN_SHOP_ID_RATINGS
                + ") REFERENCES " + TABLE_SHOP + "(" + COLUMN_SHOP_ID + "), "
                + "FOREIGN KEY (" + COLUMN_USER_ID_RATINGS
                + ") REFERENCES " + TABLE_USER + "(" + COLUMN_USER_ID + ")" + ")";

        String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + " ("
                + COLUMN_PRODUCT_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PRODUCT_NAME + " VARCHAR(255), "
                + COLUMN_PRODUCT_PRICE + " VARCHAR(255), "
                + COLUMN_PRODUCT_IMAGE + " VARCHAR(255), "
                + COLUMN_PRODUCT_DESCRIPTION + " VARCHAR(255), "
                + COLUMN_SHOP_ID_PRODUCT + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_SHOP_ID_PRODUCT + ") REFERENCES "
                + TABLE_SHOP + "(" + COLUMN_SHOP_ID + ")" + ")";


        String CREATE_TABLE_CART = "CREATE TABLE " + Constants.TABLE_CART + " ("
                + Constants.COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Constants.COLUMN_PRODUCT_ID + " INTEGER, "
                + Constants.COLUMN_USER_ID + " INTEGER, "
                + Constants.COLUMN_SHOP_ID + " INTEGER, "
                + Constants.COLUMN_QUANTITY + " INTEGER, "
                + "FOREIGN KEY (" + Constants.COLUMN_PRODUCT_ID + ") REFERENCES "
                + Constants.TABLE_PRODUCT + "(" + Constants.COLUMN_PRODUCT_ID + "), "
                + "FOREIGN KEY (" + Constants.COLUMN_USER_ID + ") REFERENCES "
                + Constants.TABLE_USER + "(" + Constants.COLUMN_USER_ID + "), "
                + "FOREIGN KEY (" + Constants.COLUMN_SHOP_ID + ") REFERENCES "
                + Constants.TABLE_SHOP + "(" + Constants.COLUMN_SHOP_ID + ")" + ")";

        String CREATE_TABLE_FACULTY_SUBJECT = "CREATE TABLE " + TABLE_ORDER_PRODUCT + " ("
                + COLUMN_ORDER_PRODUCT_ID + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ORDER_ID + " INTEGER, "
                + COLUMN_PRODUCT_ID_ORDER + " INTEGER, "
                + COLUMN_QUANTITY + " INTEGER, "
                + "FOREIGN KEY (" + COLUMN_ORDER_ID + ") REFERENCES "
                + TABLE_ORDER + "(" + COLUMN_ORDER_ID_ORDER + "), "
                + "FOREIGN KEY (" + COLUMN_PRODUCT_ID_ORDER + ") REFERENCES "
                + TABLE_PRODUCT + "(" + COLUMN_PRODUCT_ID + ")" + ")";


        String CREATE_TABLE_ORDER = "CREATE TABLE " + TABLE_ORDER + " ("
                + COLUMN_ORDER_ID_ORDER + " INTEGER  PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_ID_ORDER + " INTEGER, "
                + COLUMN_SHOP_ID_ORDER + " INTEGER, "
                + COLUMN_GRAND_TOTAL + " VARCHAR(255), "
                + COLUMN_DATE_TIME + " VARCHAR(255), "
                + COLUMN_STATUS + " VARCHAR(255), "
                + "FOREIGN KEY (" + COLUMN_USER_ID_ORDER + ") REFERENCES "
                + TABLE_USER + "(" + COLUMN_USER_ID + ")" + ")";


        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_SHOP);
        db.execSQL(CREATE_TABLE_RATINGS);
        db.execSQL(CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_FACULTY_SUBJECT);
        db.execSQL(CREATE_TABLE_ORDER);
        db.execSQL(CREATE_TABLE_CART);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USER);
        db.execSQL("DROP TABLE " + Constants.TABLE_SHOP);
        db.execSQL("DROP TABLE " + Constants.TABLE_PRODUCT);
        db.execSQL("DROP TABLE " + Constants.TABLE_ORDER);
        db.execSQL("DROP TABLE " + Constants.TABLE_ORDER_PRODUCT);
        db.execSQL("DROP TABLE " + Constants.TABLE_RATINGS);
        db.execSQL("DROP TABLE " + TABLE_CART);

        onCreate(db);
    }

    public static void startTransaction(SQLiteDatabase sqLiteDatabase) {
        try {
            endTransaction(sqLiteDatabase);
            sqLiteDatabase.beginTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void successTransaction(SQLiteDatabase sqLiteDatabase) {
        try {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.setTransactionSuccessful();
                if (sqLiteDatabase.inTransaction()) sqLiteDatabase.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void endTransaction(SQLiteDatabase sqLiteDatabase) {
        try {
            if (sqLiteDatabase != null && sqLiteDatabase.inTransaction()) {
                sqLiteDatabase.endTransaction();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
