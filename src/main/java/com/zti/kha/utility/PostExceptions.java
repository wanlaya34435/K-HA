package com.zti.kha.utility;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by up on 3/7/17.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PostExceptions extends Exception {

private int code=400;
    String message="";
    public PostExceptions(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
