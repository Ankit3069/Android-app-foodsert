package com.example.personalizedecommerceapp.controller;

import android.app.Activity;
import android.content.Context;


import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.interfaces.ILoginController;
import com.example.personalizedecommerceapp.model.UserMaster;
import com.example.personalizedecommerceapp.util.Constants;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class BaseController {

    public static <T> IController<T> getController(String name, Context context) {
        switch (name) {
            case Constants.REGISTRATION_SCREEN:
                return (IController<T>) new RegistrationController(context);
            case Constants.RATING_SCREEN:
                return (IController<T>) new RatingController(context);
            case Constants.SHOP:
                return (IController<T>) new ShopController(context);
            case Constants.PRODUCT:
                return (IController<T>) new ProductController(context);
            case Constants.CART:
                return (IController<T>) new CartController(context);
            case Constants.ORDER:
                return (IController<T>) new OrderProductsController(context);
        }
        return new EmptyController<>();
    }
    public static <T> ILoginController<T> getLoginController(Context context)  {
        return new LoginController<T>(context);
    }

    public static class EmptyController<T> implements IController<T> {

        @Override
        public long save(T entity) {
            return -1;
        }

        @Override
        public long update(T entity) {
            return -1;
        }

        @Override
        public boolean delete(T entity) {
            return false;
        }

        @Override
        public List<T> getAll() {
            return new ArrayList<>();
        }

        @Override
        public T getById(String id) {
            return (T) new Object();
        }

        @Override
        public List<T> getByCondition(String whereClause, String[] clauseValue) {
            return new ArrayList<>();
        }

        @Override
        public List<T> getByQuery(String query) {
            return new ArrayList<>();
        }

    }
}
