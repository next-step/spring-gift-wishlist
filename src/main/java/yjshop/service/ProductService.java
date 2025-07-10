package yjshop.service;

import yjshop.dto.ProductRequestDto;
import yjshop.entity.Product;
import yjshop.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    //상품 추가
    public Long add(ProductRequestDto requestDto){
        return productRepository.add(requestDto);
    }

    //상품 검색
    public Optional<Product> findOne(Long id){
        return productRepository.findById(id);
    }

    //상품 검색
    public List<Product> searchProduct(String name){
        String keyword = "%" + name + "%";
        return productRepository.findProductByName(keyword);
    }

    //전체 상품 검색
    public List<Product> findAll(){
        return productRepository.findProducts();
    }

    //상품 수정
    public void modify(Long id, ProductRequestDto requestDto){
        productRepository.modifyProduct(id, requestDto);
    }

    //상품 삭제
    public void remove(Long id){
        productRepository.removeProduct(id);
    }

}
