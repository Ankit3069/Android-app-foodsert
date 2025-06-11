package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Cart;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class CartController implements IController<Cart> {

    private final DBHelper dbHelper;
    private final ProductController controller;

    public CartController(Context context) {
        dbHelper = new DBHelper(context);
        controller = new ProductController(context);
    }

    @Override
    public long save(Cart entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        return sqLiteDatabase.insert(Constants.TABLE_CART, null, contentValues);
    }

    @Override
    public long update(Cart entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        String whereClause = Constants.COLUMN_CART_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getCartId())};
        return sqLiteDatabase.update(Constants.TABLE_CART, contentValues, whereClause, whereArgs);
    }

    @Override
    public boolean delete(Cart entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String whereClause = Constants.COLUMN_CART_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getCartId())};
        return sqLiteDatabase.delete(Constants.TABLE_CART, whereClause, whereArgs) > 0;
    }

    @Override
    public List<Cart> getAll() {
        List<Cart> cartList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_CART, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) cartList.add(getCartFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return cartList;
    }

    @Override
    public Cart getById(String id) {
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_CART,
                    null, Constants.COLUMN_CART_ID + "= ?",
                    new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) return getCartFromCursor(cursor);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cart> getByCondition(String whereClause, String[] clauseValue) {
        List<Cart> cartList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_CART,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) cartList.add(getCartFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return cartList;
    }

    @Override
    public List<Cart> getByQuery(String query) {
        List<Cart> cartList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) cartList.add(getCartFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return cartList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ContentValues getContentValues(Cart entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_PRODUCT_ID, entity.getProductId());
        contentValues.put(Constants.COLUMN_USER_ID, entity.getUserId());
        contentValues.put(Constants.COLUMN_SHOP_ID, entity.getShopId());
        contentValues.put(Constants.COLUMN_QUANTITY, entity.getQuantity());
        return contentValues;
    }

    @NonNull
    public Cart getCartFromCursor(Cursor cursor) {
        Cart cart = new Cart();
        cart.setCartId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_CART_ID)));
        cart.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_ID)));
        cart.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID)));
        cart.setShopId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID)));
        cart.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_QUANTITY)));
        final ProductMaster product = controller.getById(String.valueOf(cart.getProductId()));
        cart.setProductMaster(product);
        return cart;
    }
}
