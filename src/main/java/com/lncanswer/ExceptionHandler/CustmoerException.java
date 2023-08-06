package com.lncanswer.ExceptionHandler;
/*
自定义异常类  runtimeException 运行时异常
 */
public class CustmoerException extends RuntimeException{
    public CustmoerException(String message)
    {
        super(message);
    }
}
