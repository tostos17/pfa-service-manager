package com.pfa.app.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApiResponse<T> {
    @JsonProperty int code;
    @JsonProperty boolean success;
    @JsonProperty String message;
    @JsonProperty T body;

    public static <T> ApiResponse<T> ok(T body) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 200;
        response.success = true;
        response.message = "Success";
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> error(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 500;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> notFound(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 40;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }
    public static <T> ApiResponse<T> unauthorised(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 401;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> conflict(T body, String message) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 409;
        response.success = false;
        response.message = message;
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> created(T body) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = 201;
        response.success = true;
        response.message = "Created";
        response.body = body;
        return response;
    }

    public static <T> ApiResponse<T> generic(T body, String message, boolean success, int code) {
        ApiResponse<T> response = new ApiResponse<T>();
        response.code = code;
        response.success = success;
        response.message = message;
        response.body = body;
        return response;
    }
}
