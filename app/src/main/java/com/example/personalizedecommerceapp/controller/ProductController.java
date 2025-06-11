package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.ArrayList;
import java.util.List;

public class ProductController implements IController<ProductMaster> {

    private final int userId;
    private final DBHelper dbHelper;


    public ProductController(Context context) {
        this.userId = UserPref.getLoginUserType(context).equals(Constants.USER)
                ? UserPref.getUserId(context) : -1;
        dbHelper = new DBHelper(context);

    }

    @Override
    public long save(ProductMaster entity) {
        return -1;
    }

    @Override
    public long update(ProductMaster entity) {
        return -1;
    }

    @Override
    public boolean delete(ProductMaster entity) {
        return false;
    }

    @Override
    public List<ProductMaster> getAll() {
        List<ProductMaster> productList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PRODUCT, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) productList.add(getProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productList;
    }

    @Override
    public ProductMaster getById(String id) {
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PRODUCT,
                    null, Constants.COLUMN_PRODUCT_ID + "= ?",
                    new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) return getProductFromCursor(cursor);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductMaster> getByCondition(String whereClause, String[] clauseValue) {
        List<ProductMaster> productList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_PRODUCT,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) productList.add(getProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productList;
    }

    @Override
    public List<ProductMaster> getByQuery(String query) {
        List<ProductMaster> productList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) productList.add(getProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return productList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ContentValues getContentValues(ProductMaster entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_PRODUCT_ID, entity.getProductId());
        contentValues.put(Constants.COLUMN_SHOP_ID, entity.getShopId());
        contentValues.put(Constants.COLUMN_PRODUCT_NAME, entity.getProductName());
        contentValues.put(Constants.COLUMN_PRODUCT_PRICE, entity.getProductPrice());
        contentValues.put(Constants.COLUMN_PRODUCT_IMAGE, entity.getProductImage());
        contentValues.put(Constants.COLUMN_DESCRIPTION, entity.getDescription());
        return contentValues;
    }

    public ProductMaster getProductFromCursor(Cursor cursor) {
        ProductMaster product = new ProductMaster();
        product.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_ID)));
        product.setShopId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID)));
        product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_NAME)));
        product.setProductPrice(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_PRICE)));
        product.setProductImage(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_IMAGE)));
        product.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_DESCRIPTION)));
        product.setQuantityInCart(getProductQuantityInCart(product.getProductId(),userId));
        return product;
    }

    public int getProductQuantityInCart(int productId, int userId) {
        int quantity = 0;
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(
                    Constants.TABLE_CART,
                    new String[]{Constants.COLUMN_QUANTITY},
                    Constants.COLUMN_PRODUCT_ID + " = ? AND " + Constants.COLUMN_USER_ID + " = ?",
                    new String[]{String.valueOf(productId), String.valueOf(userId)},
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    quantity = cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_QUANTITY));
                }
                cursor.close();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return quantity;
    }


}
