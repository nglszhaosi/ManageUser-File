package com.liu.exception;


import lombok.Data;

@Data
public class ServiceException extends RuntimeException{
    String code;

    public ServiceException(String code , String msg){
        super(msg);
        this.code = code;
    }
}
