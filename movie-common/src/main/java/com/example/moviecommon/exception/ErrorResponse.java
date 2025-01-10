package com.example.moviecommon.exception;

public record ErrorResponse(String errorCode, String message, int statusCode) {
}
