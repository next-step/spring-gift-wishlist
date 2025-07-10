package gift.exception.old;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String errorMsg){
        super(errorMsg);
    }

}
