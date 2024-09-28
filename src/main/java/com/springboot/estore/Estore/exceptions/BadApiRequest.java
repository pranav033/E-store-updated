package com.springboot.estore.Estore.exceptions;

public class BadApiRequest extends RuntimeException{
    public BadApiRequest()
    {
        super("Bad request.");
    }

    public BadApiRequest(String message)
    {
        super(message);
    }
}
