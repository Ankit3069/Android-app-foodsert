package com.example.personalizedecommerceapp.controller;

import static com.example.personalizedecommerceapp.util.Constants.COLUMN_CUISINE_CATEGORY;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_SHOP_TYPE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopController implements IController<Shop> {

    private final DBHelper dbHelper;
    private final ProductController controller;

    public ShopController(Context context) {
        dbHelper = new DBHelper(context);
        controller = new ProductController(context);
    }

    @Override
    public long save(Shop entity) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        return sqLiteDatabase.insert(Constants.TABLE_SHOP, null, contentValues);
    }

    @Override
    public long update(Shop entity) {
        String shopId = String.valueOf(entity.getShopId());
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = getContentValues(entity);
        return sqLiteDatabase.update(Constants.TABLE_SHOP, contentValues,
                Constants.COLUMN_SHOP_ID + " =?", new String[]{shopId});
    }

    @Override
    public boolean delete(Shop entity) {
        long result = -1;
        Cursor cursor = null;
        try {
            String shopId = String.valueOf(entity.getShopId());
            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery("select * from " + Constants.TABLE_SHOP +
                    " where " + Constants.COLUMN_SHOP_ID + " =?", new String[]{shopId});
            if (cursor.getCount() > 0) {
                result = sqLiteDatabase.delete(Constants.TABLE_SHOP,
                        Constants.COLUMN_SHOP_ID + " =?", new String[]{shopId});
            }
            closeCursor(cursor);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            closeCursor(cursor);
        }
        return result != -1;
    }

    @Override
    public List<Shop> getAll() {
        List<Shop> shopList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_SHOP, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) shopList.add(getShopFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return shopList;
    }

    @Override
    public Shop getById(String id) {
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_SHOP,
                    null, Constants.COLUMN_SHOP_ID + "= ?",
                    new String[]{id}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) return getShopFromCursor(cursor);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Shop> getByCondition(String whereClause, String[] clauseValue) {
        List<Shop> shopList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_SHOP,
                    null, whereClause, clauseValue, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) shopList.add(getShopFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return shopList;
    }

    @Override
    public List<Shop> getByQuery(String query) {
        List<Shop> shopList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            if (cursor != null) {
                while (cursor.moveToNext()) shopList.add(getShopFromCursor(cursor));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return shopList;
    }

    private void closeCursor(Cursor cursor) {
        try {
            if (cursor != null) cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public ContentValues getContentValues(Shop entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_SHOP_NAME, entity.getShopName());
        contentValues.put(Constants.COLUMN_SHOP_URL, entity.getShopUrl());
        contentValues.put(Constants.COLUMN_CITY, entity.getCity());
        contentValues.put(Constants.COLUMN_REGION, entity.getRegion());
        contentValues.put(Constants.COLUMN_SHOP_TYPE, entity.getShopType());
        contentValues.put(Constants.COLUMN_CUISINE_CATEGORY, entity.getCuisineCategory());
        contentValues.put(Constants.COLUMN_LATITUDE, entity.getLatitude());
        contentValues.put(Constants.COLUMN_LONGITUDE, entity.getLongitude());
        contentValues.put(Constants.COLUMN_DESCRIPTION, entity.getDescription());
        contentValues.put(Constants.COLUMN_TIMING, entity.getTiming());
        contentValues.put(Constants.COLUMN_RATING, entity.getRating());
        contentValues.put(Constants.COLUMN_PRICE, entity.getPrice());
        return contentValues;
    }

    @NonNull
    public Shop getShopFromCursor(Cursor cursor) {
        Shop shop = new Shop();
        shop.setShopId(cursor.getInt(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_ID)));
        shop.setShopName(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_NAME)));
        shop.setShopUrl(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_URL)));
        shop.setCity(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CITY)));
        shop.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_REGION)));
        shop.setShopType(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_SHOP_TYPE)));
        shop.setCuisineCategory(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_CUISINE_CATEGORY)));
        shop.setLatitude(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LATITUDE)));
        shop.setLongitude(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LONGITUDE)));
        shop.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_DESCRIPTION)));
        shop.setTiming(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_TIMING)));
        shop.setRating(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_RATING)));
        shop.setPrice(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_PRICE)));
        String whereClause = Constants.COLUMN_SHOP_ID + "= ?";
        String[] clauseValue = new String[]{java.lang.String.valueOf(shop.getShopId())};
        shop.setProductMasterList(controller.getByCondition(whereClause, clauseValue));
        return shop;
    }

    public List<Shop> getAll_byloc(Context con) {
        List<Shop> shopList = new ArrayList<>();
        try {
            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.query(Constants.TABLE_SHOP, null,
                    null, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext())
                {
                    Log.d("RESPONSE",UserPref.getLoc(con));
                    LatLng shopLL=new LatLng(
                            Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LATITUDE))),
                            Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LONGITUDE))));
                    String[] curloc= UserPref.getLoc(con).split(",");
                    LatLng curLL=new LatLng(Double.parseDouble(curloc[0]),Double.parseDouble(curloc[1]));

                    Location l1=new Location("");
                    l1.setLatitude(shopLL.latitude);
                    l1.setLongitude(shopLL.longitude);

                    Location l2=new Location("");
                    l2.setLatitude(curLL.latitude);
                    l2.setLongitude(curLL.longitude);

                    float dist=l1.distanceTo(l2);
                    Log.d("RESPONSE",dist+" DIST");
                    if(dist<5000)
                    {
                        shopList.add(getShopFromCursor(cursor));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return shopList;
    }

    public List<Shop> getRecommendation(Context con,Cursor c) {
        List<Shop> shopList = new ArrayList<>();
        try {

            String shoptype="",cuisinetype="",minprice="",maxprice="";
            if(c.getCount()>0)
            {
                c.moveToFirst();
                shoptype=c.getString(0);
                cuisinetype=c.getString(1);
                minprice=c.getString(2);
                maxprice=c.getString(3);
            }

            SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

            String query="select * from "+Constants.TABLE_SHOP+" where "+containsQuery(COLUMN_SHOP_TYPE,shoptype)
                    +" AND "+containsQuery(COLUMN_CUISINE_CATEGORY,cuisinetype)+" AND CAST(price AS INT)  between '"+minprice+"' AND '"+maxprice+"'";

            Log.d("RESPONSE",query);
            Cursor cursor=sqLiteDatabase.rawQuery(query,null);

            if (cursor != null) {
                while (cursor.moveToNext())
                {
                    Log.d("RESPONSE",UserPref.getLoc(con));
                    LatLng shopLL=new LatLng(
                            Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LATITUDE))),
                            Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(Constants.COLUMN_LONGITUDE))));
                    String[] curloc= UserPref.getLoc(con).split(",");
                    LatLng curLL=new LatLng(Double.parseDouble(curloc[0]),Double.parseDouble(curloc[1]));

                    Location l1=new Location("");
                    l1.setLatitude(shopLL.latitude);
                    l1.setLongitude(shopLL.longitude);

                    Location l2=new Location("");
                    l2.setLatitude(curLL.latitude);
                    l2.setLongitude(curLL.longitude);

                    float dist=l1.distanceTo(l2);
                    Log.d("RESPONSE",dist+" DIST");
                    if(dist<5000)
                    {
                        shopList.add(getShopFromCursor(cursor));
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return shopList;
    }

    public String containsQuery(String col,String values)
    {
        String[] val=values.split(",");
        String res="";
        for(int i=0;i<val.length;i++)
        {
            if(i==val.length-1)
            {
                res+=" "+col+" LIKE '%"+val[i]+"%'";
            }
            else {
                res+=" "+col+" LIKE '%"+val[i]+"%' OR ";
            }
        }

        res="("+res+")";
        return res;
    }
}
