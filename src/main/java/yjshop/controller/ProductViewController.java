package yjshop.controller;

import yjshop.entity.Product;
import yjshop.exception.ProductNotFoundException;
import yjshop.service.ProductService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/view")
@Controller
public class ProductViewController {


    private final ProductService productService;

    public ProductViewController(ProductService productService){
        this.productService = productService;
    }

    //전체 상품을 조회
    @GetMapping("/products/list")
    public String getProducts(Model model) {
        List<Product> productList = productService.findAll();
        model.addAttribute("productList", productList);
        return "/yjshop/home";
    }

    //특정 상품을 조회(id)
    @GetMapping("/products/info")
    public String getProduct(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        Optional<Product> product = productService.findOne(id);
        if (product.isEmpty()) {
            String errorMsg = "상품 ID가 " + id + "인 상품은 존재하지 않습니다.";
            throw new ProductNotFoundException(errorMsg);
        }
        model.addAttribute("product", product.get());
        return "/yjshop/productinfo";
    }

    //특정 상품을 검색(상품명을 통한 검색)
    @GetMapping("/products/search")
    public String searchProduct(
            @RequestParam(required = false) String name,
            Model model
    ) {
        //상품 검색하기에 아무런 상품명를 입력하지 않은 경우 -> 전체 상품을 조회하는 페이지로 이동
        if(name == null){
            return "redirect:/view/product/list";
        }

        List<Product> product = productService.searchProduct(name);
        model.addAttribute("productList", product);
        return "/yjshop/home";
    }

}

