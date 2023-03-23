package com.erswebapp.backend.utilities;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseInfo<T> extends EmptyResponseInfo {
    private T body;

    public ResponseInfo(String timestamp, HttpStatus status, String path, T body) {
        super(timestamp, status, path);
        this.body = body;
    }
}
