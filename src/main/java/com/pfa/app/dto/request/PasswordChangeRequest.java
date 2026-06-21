package com.pfa.app.dto.request;

public record PasswordChangeRequest(
        String oldPassword,
        String newPassword
) {}