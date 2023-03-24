package com.erswebapp.backend.utilities;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmptyErrorResponseInfo extends BaseInfo {
    private HttpStatus status;
    private String path;
    private String error;

    public EmptyErrorResponseInfo(String timestamp, HttpStatus status, String path, String error) {
        super(timestamp);
        this.status = status;
        this.path = path;
        this.error = error;
    }
}
