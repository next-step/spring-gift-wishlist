package gift.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.service.ProductService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

//@ResponseBody + @Controller
@RestController
@RequestMapping("/api")
public class ProductController {


    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    Map<Long, Product> products = new HashMap<>();

    //create
    //생성한 product는 HashMap에 저장
    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(
            @ModelAttribute ProductRequestDto requestDto
    ) {
        return null;
    }

    //read
    //특정 상품을 조회(id)
    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        if(productService.findOne(id).isPresent()){
            Product product = productService.findOne(id).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    //read
    //전체 상품을 조회
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> productList = productService.findAll();
        return new ResponseEntity<>(productList, HttpStatus.OK);
    }

    //update
    //상품 수정
    @PutMapping("/products/{id}")
    public ResponseEntity<Product> modifyProduct(
            @RequestBody ProductRequestDto requestDto,
            @PathVariable Long id
    ) {
        productService.modify(id, requestDto);
        //특정 상품을 찾는 기능과 종속성을 가짐으로 좋지 않은 코드인지, 상관 없는지 궁금합니다.
        Product modifiedProduct = productService.findOne(id).get();
        return new ResponseEntity<>(modifiedProduct, HttpStatus.OK);
    }

    //delete
    //등록된 상품을 삭제
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> removeProduct(@PathVariable Long id) {
        Long productId = findProductById(id).getId();
        products.remove(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public boolean checkProduct(ProductRequestDto requestDto) {
        //상품명, 가격, 이미지의 경우 모두 필수 값
        if (requestDto.getName() == null || requestDto.getPrice() == null
                || requestDto.getImageUrl() == null) {
            return false;
        }
        //가격은 0이하가 될 수 없음
        else if (requestDto.getPrice() < 0) {
            return false;
        }
        return true;
    }

    public Product findProductById(Long id){
        Optional<Product> optionalProduct = Optional.ofNullable(products.get(id));
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다.");
    }
}