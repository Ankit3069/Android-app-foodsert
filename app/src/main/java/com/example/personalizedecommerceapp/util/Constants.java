package com.example.personalizedecommerceapp.util;

import android.os.Environment;

import java.io.File;

public class Constants {
    private static final String downloadDir = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String SHARED_PREF = "personalizedecommerceapp";
    public static final String DATABASE_NAME = "personalized_ecommerce_app_database.db";
    public static final String FILE_SAVE_DESTINATION = downloadDir
            + File.separator + "PersonalizedEcommerce" + File.separator;

    public static final String COLLECTIONS = "Collections";
    public static final long ERROR_SQL_RESULT = -1;


    //DIALOG OR SNACKBAR
    public static final String SOMETHING_WENT_WRONG = "Something Went Wrong!!!";
    public static final String PLEASE_WAIT = "Please Wait!!!";
    public static final String SAVING = "Saving";
    public static final String SAVING_SUCCESS = "Saved Successfully";
    public static final String UPDATING_SUCCESS = "Updated Successfully";
    public static final String UPDATING = "Updating";
    public static final String FETCHING = "Fetching";

    //SQL_ERRORS
    public static final long ALREADY = -55;
    public static final long ERROR = -1;

    //SHARED_PREF
    public static final String USER_TYPE = "UserType";
    public static final String SHOP = "Shop";
    public static final String USER = "user";

    //CONTROLLERS
    public static final String PRODUCT = "Product";
    public static final String CART = "Cart";
    public static final String ORDER = "Order";

    //SCREENS
    public static final String REGISTRATION_SCREEN = "Registration";
    public static final String HOME_SCREEN = "Home";
    public static final String RECOMMENDATION_SCREEN = "Recommendation";
    public static final String ORDER_SCREEN = "Orders";
    public static final String PRODUCT_SCREEN = "Products";
    public static final String CART_SCREEN = "Cart";
    public static final String RATING_SCREEN = "Rating";

    //DATABASE
    // Table Names
    public static final String TABLE_USER = "UserMaster";
    public static final String TABLE_SHOP = "Shop";
    public static final String TABLE_PRODUCT = "ProductMaster";
    public static final String TABLE_CART = "Cart";
    public static final String TABLE_ORDER = "OrderMaster";
    public static final String TABLE_ORDER_PRODUCT = "OrderProduct";
    public static final String TABLE_RATINGS = "Ratings";

    // Column Names - UserMaster
    public static final String COLUMN_USER_ID = "userId";
    public static final String COLUMN_FULL_NAME = "fullName";
    public static final String COLUMN_EMAIL_ID = "emailId";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_CONTACT_NUMBER = "contactNumber";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_SHOP_TYPE_PREFERENCE = "shopTypePreference";
    public static final String COLUMN_CUISINE_CATEGORY_PREFERENCE = "cuisineCategoryPreference";
    public static final String COLUMN_MIN_PRICE_PREFERENCE = "minPricePreference";
    public static final String COLUMN_MAX_PRICE_PREFERENCE = "maxPricePreference";

    // Column Names - Shop
    public static final String COLUMN_SHOP_ID = "shopId";
    public static final String COLUMN_SHOP_NAME = "shopName";
    public static final String COLUMN_SHOP_URL = "shopUrl";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_SHOP_TYPE = "shopType";
    public static final String COLUMN_CUISINE_CATEGORY = "cuisineCategory";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TIMING = "timing";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASS = "pass";

    // Column Names - Ratings
    public static final String COLUMN_SHOP_ID_RATINGS = "shopId";
    public static final String COLUMN_USER_ID_RATINGS = "userId";
    public static final String COLUMN_RATING_COUNT = "ratingCount";
    public static final String COLUMN_REVIEW = "review";

    // Column Names - ProductMaster
    public static final String COLUMN_PRODUCT_ID = "productId";
    public static final String COLUMN_PRODUCT_NAME = "productName";
    public static final String COLUMN_PRODUCT_PRICE = "productPrice";
    public static final String COLUMN_PRODUCT_IMAGE = "productImage";
    public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    public static final String COLUMN_SHOP_ID_PRODUCT = "shopId";

    // Column Names - UserMaster
    public static final String COLUMN_CART_ID = "cartId";
    public static final String COLUMN_QUANTITY = "quantity";

    // Column Names - OrderProduct
    public static final String COLUMN_ORDER_PRODUCT_ID = "orderProductId";
    public static final String COLUMN_ORDER_ID = "orderId";
    public static final String COLUMN_PRODUCT_ID_ORDER = "productId";

    // Column Names - Order
    public static final String COLUMN_ORDER_ID_ORDER = "orderId";
    public static final String COLUMN_USER_ID_ORDER = "userId";
    public static final String COLUMN_GRAND_TOTAL = "grandTotal";
    public static final String COLUMN_DATE_TIME = "dateTime";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_SHOP_ID_ORDER = "shopid";

}
