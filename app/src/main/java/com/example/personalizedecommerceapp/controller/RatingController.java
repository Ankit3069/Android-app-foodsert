package com.example.personalizedecommerceapp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Ratings;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RatingController implements IController<Ratings> {

    private final DBHelper dbHelper;

    public RatingController(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public long save(Ratings entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String whereClause = Constants.COLUMN_SHOP_ID_RATINGS + " = ? AND " + Constants.COLUMN_USER_ID_RATINGS + " = ?";
        String[] whereArgs = new String[]{
                String.valueOf(entity.getShopId()),
                String.valueOf(entity.getUserId())
        };
        Cursor cursor = sqLiteDatabase.query(Constants.TABLE_RATINGS, null, whereClause, whereArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long rowsAffected = update(entity);
            cursor.close();
            return rowsAffected;
        } else {
            ContentValues contentValues = getContentValues(entity);
            long insertedRow = sqLiteDatabase.insert(Constants.TABLE_RATINGS, null, contentValues);
            closeCursor(cursor);
            return insertedRow;
        }
    }


    @Override
    public long update(Ratings entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        String whereClause = Constants.COLUMN_SHOP_ID_RATINGS + " = ? AND " + Constants.COLUMN_USER_ID_RATINGS + " = ?";
        String[] whereArgs = new String[]{
                String.valueOf(entity.getShopId()),
                String.valueOf(entity.getUserId())
        };
        return sqLiteDatabase.update(Constants.TABLE_RATINGS, contentValues, whereClause, whereArgs);
    }

    @Override
    public boolean delete(Ratings entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        String whereClause = Constants.COLUMN_SHOP_ID_RATINGS + " = ? AND " + Constants.COLUMN_USER_ID_RATINGS + " = ?";
        String[] whereArgs = new String[]{
                String.valueOf(entity.getShopId()),
                String.valueOf(entity.getUserId())
        };
        return sqLiteDatabase.delete(Constants.TABLE_RATINGS, whereClause, whereArgs) > 0;
    }

    @Override
    public List<Ratings> getAll() {
        List<Ratings> ratingsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_RATINGS, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) ratingsList.add(getRatingFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ratingsList;
    }

    @Override
    public Ratings getById(String id) {
        // Implement this method if needed, considering the ID field.
        return null;
    }

    @Override
    public List<Ratings> getByCondition(String whereClause, String[] clauseValue) {
        List<Ratings> ratingsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_RATINGS,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) ratingsList.add(getRatingFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ratingsList;
    }

    @Override
    public List<Ratings> getByQuery(String query) {
        List<Ratings> ratingsList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) ratingsList.add(getRatingFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ratingsList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ContentValues getContentValues(Ratings entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_SHOP_ID_RATINGS, entity.getShopId());
        contentValues.put(Constants.COLUMN_USER_ID_RATINGS, entity.getUserId());
        contentValues.put(Constants.COLUMN_RATING_COUNT, entity.getRatingCount());
        contentValues.put(Constants.COLUMN_REVIEW, entity.getReview());
        return contentValues;
    }

    @NonNull
    public Ratings getRatingFromCursor(Cursor cursor) {
        Ratings rating = new Ratings();
        rating.setShopId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID_RATINGS)));
        rating.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_USER_ID_RATINGS)));
        rating.setRatingCount(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_RATING_COUNT)));
        rating.setReview(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_REVIEW)));
        return rating;
    }
}
