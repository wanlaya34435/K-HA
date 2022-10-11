package com.zti.framework.exception;

public class DAOException extends Exception{
    public DAOException(String errorMessage){
        super(errorMessage);
    }

    public DAOException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
