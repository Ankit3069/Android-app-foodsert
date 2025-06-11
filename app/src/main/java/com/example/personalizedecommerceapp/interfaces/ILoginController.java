package com.example.personalizedecommerceapp.interfaces;

public interface ILoginController<T> {
    boolean authenticateUser(String userName, String password,String userType);
}
