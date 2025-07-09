package gift.service;

import gift.Entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class ProductService {

    // if문으로 제어해보기
    public boolean validateProduct(Product product, BindingResult bindingResult) {
        if (product.getName().contains("카카오") && !product.getMDapproved()) {
            bindingResult.rejectValue("name", "forbidden.word", "상품 이름에 '카카오'는 포함할 수 없습니다.");
            return false;
        }
        return true;
    }

    // try catch문으로 제어해보기
    public void validateProductException(Product product) {
        if (product.getName().contains("카카오") && !product.getMDapproved()) {
            throw new IllegalArgumentException("상품 이름에 '카카오'는 포함할 수 없습니다.");
        }
    }
}