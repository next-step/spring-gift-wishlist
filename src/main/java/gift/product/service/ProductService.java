package gift.product.service;

import org.springframework.stereotype.Service;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gift.product.dto.ProductResponseDto.fromEntity;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    //단건 조회
    public ProductResponseDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("id가 옳바르지 않습니다."));
        return fromEntity(product);
    }

    //전체 조회
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> productResponseDtos = new ArrayList<>();
        for (Product product : products) {
            productResponseDtos.add(fromEntity(product));
        }
        return productResponseDtos;
    }

    //추가
    public ProductResponseDto addProduct(ProductRequestDto productRequestDto) {
        Product product = new Product();

        if(productRequestDto != null) {
            product.setName(productRequestDto.getName());
            product.setPrice(productRequestDto.getPrice());
            product.setImageUrl(productRequestDto.getImageUrl());
            return productRepository.save(product);
        }
        return null;
    }

    //수정
    public ProductResponseDto updateProduct(Long id ,ProductRequestDto productRequestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("id가 옳바르지 않습니다."));
        if(productRequestDto != null) {
            product.setName(productRequestDto.getName());
            product.setPrice(productRequestDto.getPrice());
            product.setImageUrl(productRequestDto.getImageUrl());

            return fromEntity(productRepository.update(product));
        }
        return null;
    }

    //삭제
    public void deleteProduct(Long id) {
        productRepository.delete(id);
    }

    //상품 이름 검사
    public Boolean validateProduct(ProductRequestDto productRequestDto) {
        if(productRequestDto.getName().contains("카카오")){
            Optional<Product> product = productRepository.findById(productRequestDto.getId());
            if(product.isPresent()) {
                return product.get().getNamePermission(); //허가 받았는지 확인
            }else{
                return false; // 아이디가 없으므로 허가 x
            }
        }
        return true; // 카카오가 이름에 포함 x
    }

}
