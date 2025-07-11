package gift.yjshop.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.exception.ErrorCode;
import gift.exception.MyException;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/admin")
public class ProductAdminViewController {

    private static final Logger log = LoggerFactory.getLogger(ProductAdminViewController.class);
    private final ProductService productService;

    public ProductAdminViewController(ProductService productService) {
        this.productService = productService;
    }

    //전체 상품 가져오기
    @GetMapping("/products")
    public String adminProductList(Model model){
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "/yjshop/admin/product/home";
    }

    //상품 등록 화면을 가져오기
    @GetMapping("/products/add")
    public String productForm(Model model) {
        model.addAttribute("productRequestDto", new ProductRequestDto());
        model.addAttribute("requestDto", new ProductRequestDto());
        return "/yjshop/admin/product/form";
    }

    //특정 상품을 검색(id)
    @GetMapping("/products/search")
    public String getProduct(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        //상품 검색하기에 아무런 id를 입력하지 않은 경우 -> 전체 상품을 조회하는 페이지로 이동
        if(id == null){
            return "redirect:/view/admin/products";
        }

        Optional<Product> product = productService.findOne(id);
        if (product.isEmpty()) {
            throw new MyException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        model.addAttribute("product", product.get());
        return "/yjshop/admin/product/productinfo";
    }

    //등록된 상품을 삭제
    @PostMapping("/products/remove/{id}")
    public String removeProduct(@PathVariable Long id) {
        productService.remove(id);
        return "redirect:/view/admin/products";
    }

    //상품 등록
    @PostMapping("/products/add")
    public String createProduct(
            @ModelAttribute @Valid ProductRequestDto requestDto,
            BindingResult bindingResult,
            Model model
    ) {

        if(bindingResult.hasErrors()){
            model.addAttribute("requestDto", requestDto);
            return "/yjshop/admin/product/form";
        }

        Long id = productService.add(requestDto);
        return "redirect:/view/admin/products/search?id=" + id;
    }

    //수정 폼 불러오기
    @GetMapping("/products/modify/{id}")
    public String modifyForm(
            @PathVariable Long id,
            Model model
    ) {
        Product product = productService.findOne(id).get();
        model.addAttribute("product", product);
        model.addAttribute("productRequestDto", new ProductRequestDto());
        return "/yjshop/admin/product/modifyform";
    }

    //상품 수정
    @PostMapping("/products/modify/{id}")
    public String modifyProduct(
            @ModelAttribute @Valid ProductRequestDto requestDto,
            BindingResult bindingResult,
            @PathVariable Long id,
            Model model
    ) {
        if(bindingResult.hasErrors()){
            Product product = productService.findOne(id).get();
            model.addAttribute("product", product);
            return "/yjshop/admin/product/modifyform";
        }
        productService.modify(id, requestDto);
        return "redirect:/view/admin/products";
    }

    @ExceptionHandler(MyException.class)
    public String MyExceptionHandler(MyException e, Model model){
        model.addAttribute("errorMsg", e.getErrorCode().getMessage());
        return "/yjshop/admin/product/productnotfound";
    }
}
