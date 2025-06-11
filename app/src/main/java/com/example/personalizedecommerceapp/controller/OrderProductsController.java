package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Cart;
import com.example.personalizedecommerceapp.model.OrderProduct;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderProductsController implements IController<OrderProduct> {

    private final DBHelper dbHelper;
    private final ProductController controller;

    public OrderProductsController(Context context) {
        dbHelper = new DBHelper(context);
        controller = new ProductController(context);
    }

    public long save(SQLiteDatabase sqLiteDatabase, long orderId, List<OrderProduct> entityList) {
        sqLiteDatabase = sqLiteDatabase == null ? dbHelper.getWritableDatabase() : sqLiteDatabase;
        for (OrderProduct entity : entityList) {
            entity.setOrderId((int) orderId);
            ContentValues contentValues = getContentValues(entity);
            final long insert = sqLiteDatabase.insert(Constants.TABLE_ORDER_PRODUCT, null, contentValues);
            if (insert < 0) return -1;
        }
        return 1;
    }

    @Override
    public long save(OrderProduct entity) {
        return 0;
    }

    public long update(OrderProduct entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        String whereClause = Constants.COLUMN_ORDER_PRODUCT_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getOrderProductId())};
        return sqLiteDatabase.update(Constants.TABLE_ORDER_PRODUCT, contentValues, whereClause, whereArgs);
    }

    public boolean delete(OrderProduct entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String whereClause = Constants.COLUMN_ORDER_PRODUCT_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getOrderProductId())};
        return sqLiteDatabase.delete(Constants.TABLE_ORDER_PRODUCT, whereClause, whereArgs) > 0;
    }

    public List<OrderProduct> getAll() {
        List<OrderProduct> orderProductList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER_PRODUCT, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) orderProductList.add(getOrderProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderProductList;
    }

    public OrderProduct getById(String id) {
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER_PRODUCT,
                    null, Constants.COLUMN_ORDER_PRODUCT_ID + "= ?",
                    new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) return getOrderProductFromCursor(cursor);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public List<OrderProduct> getByCondition(String whereClause, String[] clauseValue) {
        List<OrderProduct> orderProductList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER_PRODUCT,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) orderProductList.add(getOrderProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderProductList;
    }

    public List<OrderProduct> getByQuery(String query) {
        List<OrderProduct> orderProductList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) orderProductList.add(getOrderProductFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderProductList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ContentValues getContentValues(OrderProduct entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_ORDER_ID, entity.getOrderId());
        contentValues.put(Constants.COLUMN_PRODUCT_ID, entity.getProductId());
        return contentValues;
    }

    @NonNull
    public OrderProduct getOrderProductFromCursor(Cursor cursor) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.setOrderProductId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_ORDER_PRODUCT_ID)));
        orderProduct.setProductId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRODUCT_ID)));
        orderProduct.setProductMaster(controller.getById(String.valueOf(orderProduct.getProductId())));
        return orderProduct;
    }
}
