package yjshop.exception;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String errorMsg){
        super(errorMsg);
    }

}
