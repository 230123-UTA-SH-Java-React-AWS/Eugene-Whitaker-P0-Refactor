package com.erswebapp.backend.utilities;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmptyResponseInfo extends BaseInfo {
    private HttpStatus status;
    private String path;

    public EmptyResponseInfo(String timestamp, HttpStatus status, String path) {
        super(timestamp);
        this.status = status;
        this.path = path;
    }
}
