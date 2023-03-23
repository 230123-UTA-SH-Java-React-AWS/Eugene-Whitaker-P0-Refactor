package com.erswebapp.backend.utilities;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseInfo<T> extends EmptyErrorResponseInfo {
    private T body;

    public ErrorResponseInfo(String timestamp, HttpStatus status, String path, String error, T body) {
        super(timestamp, status, path, error);
        this.body = body;
    }
}
