package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class OrderController implements IController<Order> {

    private final DBHelper dbHelper;
    private final OrderProductsController controller;

    public OrderController(Context context) {
        dbHelper = new DBHelper(context);
        controller = new OrderProductsController(context);
    }

    @Override
    public long save(Order entity) {
            SQLiteDatabase sqLiteDatabase = null;
        long result = Constants.ERROR;
        try {
            sqLiteDatabase = dbHelper.getWritableDatabase();
            DBHelper.startTransaction(sqLiteDatabase);
            ContentValues contentValues = getContentValues(entity);
            final long insert = sqLiteDatabase.insert(Constants.TABLE_ORDER, null, contentValues);
            if (insert > 0) {
                result = controller.save(sqLiteDatabase,result, entity.getOrderProductList());
            }
        } finally {
            DBHelper.endTransaction(sqLiteDatabase);
        }
        return result;
    }

    @Override
    public long update(Order entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        String whereClause = Constants.COLUMN_ORDER_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getOrderId())};
        return sqLiteDatabase.update(Constants.TABLE_ORDER, contentValues, whereClause, whereArgs);
    }

    @Override
    public boolean delete(Order entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String whereClause = Constants.COLUMN_ORDER_ID + " = ?";
        String[] whereArgs = new String[]{String.valueOf(entity.getOrderId())};
        return sqLiteDatabase.delete(Constants.TABLE_ORDER, whereClause, whereArgs) > 0;
    }

    @Override
    public List<Order> getAll() {
        List<Order> orderList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) orderList.add(getOrderFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderList;
    }

    @Override
    public Order getById(String id) {
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER,
                    null, Constants.COLUMN_ORDER_ID + "= ?",
                    new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) return getOrderFromCursor(cursor);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> getByCondition(String whereClause, String[] clauseValue) {
        List<Order> orderList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_ORDER,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) orderList.add(getOrderFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderList;
    }

    @Override
    public List<Order> getByQuery(String query) {
        List<Order> orderList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) orderList.add(getOrderFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return orderList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ContentValues getContentValues(Order entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_USER_ID, entity.getUserId());
        contentValues.put(Constants.COLUMN_GRAND_TOTAL, entity.getGrandTotal());
        contentValues.put(Constants.COLUMN_DATE_TIME, entity.getDateTime());
        contentValues.put(Constants.COLUMN_STATUS, entity.getStatus());
        return contentValues;
    }

    @NonNull
    public Order getOrderFromCursor(Cursor cursor) {
        Order order = new Order();
        order.setOrderId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_ORDER_ID)));
        order.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID_ORDER)));
        return order;
    }
}
