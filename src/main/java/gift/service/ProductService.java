package gift.service;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    //상품 추가
    public void add(ProductRequestDto requestDto){
        productRepository.add(requestDto);
    }

    //상품 검색
    public Product findOne(Long id){
        return null;
    }

    //전체 상품 검색
    public List<Product> findAll(){
        return null;
    }

    //상품 수정
    public void modify(Long id, ProductRequestDto requestDto){

    }

    //상품 삭제
    public void remove(Long id){

    }

}
