package gift.Controller;

import gift.Entity.Product;
import gift.dto.ProductDto;
import gift.dto.WishDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user/products")
public class ProductViewController {

    private final ProductDto productDto;
    private final ProductService productservice;
    private final WishDto wishDto;

    public ProductViewController(ProductService productservice, ProductDto productDto,  WishDto wishDto) {
        this.productservice = productservice;
        this.productDto = productDto;
        this.wishDto = wishDto;
    }

    // 상품 목록 페이지
    @GetMapping
    public String list(Model model) {
        List<Product> products = productDto.showProducts();
        model.addAttribute("products", products);
        return "products/list";
    }

}