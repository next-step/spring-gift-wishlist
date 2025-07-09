package gift.service;

import gift.dto.RequestDto;
import gift.dto.ResponseDto;
import gift.entity.Product;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import gift.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 1. 상품 등록
    @Override
    public ResponseDto create(RequestDto dto) {

        if (dto.getName().contains("카카오")) {
            throw new IllegalArgumentException("상품명에 '카카오'를 포함하려면 담당 MD와의 협의가 필요합니다.");
        }

        Product product = new Product(null, dto.getName(), dto.getImageUrl());
        Product saved = productRepository.save(product);

        return new ResponseDto(saved);
    }

    // 2-1. 전체 상품 조회
    @Override
    public List<ResponseDto> findAll() {
        List<Product> products = productRepository.findAll();

        List<ResponseDto> dtoList = new ArrayList<>();

        for (Product product : products) {
            dtoList.add(new ResponseDto(product));
        }

        return dtoList;
    }

    // 2-2. 특정 상품 조회
    @Override
    public ResponseDto findById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        return new ResponseDto(product);
    }

    // 3. 상품 수정
    @Override
    public ResponseDto update(Long id, RequestDto dto) {

        if (dto.getName().contains("카카오")) {
            throw new IllegalArgumentException("상품명에 '카카오'를 포함하려면 담당 MD와의 협의가 필요합니다.");
        }

        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        product.update(dto);
        productRepository.update(product);
        return new ResponseDto(product);
    }

    // 4. 상품 삭제
    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        productRepository.deleteById(id);
    }
}
