package br.com.paulo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyFileNotFoundException extends RuntimeException{

    public MyFileNotFoundException(String ex) {
        super(ex);
    }

    public MyFileNotFoundException(String ex, Throwable cause) {
        super(ex, cause);
    }

    private static final long serialVersionUID = 1L;
}