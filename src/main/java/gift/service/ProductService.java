package gift.service;

import gift.Entity.Product;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class ProductService {

    public boolean validate(Product product, BindingResult bindingResult) {
        if (product.getName().contains("카카오") && !product.getMDapproved()) {
            bindingResult.rejectValue("name", "forbidden.word", "상품 이름에 '카카오'는 포함할 수 없습니다.");
            return false;
        }
        return true;
    }

}
