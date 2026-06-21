package com.pfa.app.service;

public interface UserService {
    void changeInitialPassword(String email, String oldPassword, String newPassword);
}