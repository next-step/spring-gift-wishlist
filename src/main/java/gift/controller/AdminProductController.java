package gift.controller;

import gift.model.Product;
import gift.repository.ProductDao;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {
    private final ProductDao productDao;

    public AdminProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productDao.getAllProducts());
        return "product/list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product",new Product(null,null,null,null));
        return "product/form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("상품 추가 유효성 검사 실패! 폼 재렌더링.");
            bindingResult.getAllErrors().forEach(error -> System.out.println("오류: " + error.getDefaultMessage()));
            return "product/form";
        }
        productDao.insertProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productDao.getProductById(id);
        model.addAttribute("product", product);
        return "product/form";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("상품 추가 유효성 검사 실패! 폼 재렌더링.");
            bindingResult.getAllErrors().forEach(error -> System.out.println("오류: " + error.getDefaultMessage()));
            return "product/form";
        }

        System.out.println("상품 수정 성공: " + product.getName());
        productDao.updateProduct(product.getId(), product, product);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productDao.removeProduct(id);
        return "redirect:/admin/products";
    }
}