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
        model.addAttribute("product",new Product(null,null,null,null, false));
        return "product/form";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute Product product, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println("오류: " + error.getDefaultMessage()));
            return "product/form";
        }
        if (!product.getName().contains("카카오")) {
            product.setMdApproved(true);
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
            bindingResult.getAllErrors().forEach(error -> System.out.println("오류: " + error.getDefaultMessage()));
            return "product/form";
        }
        if (product.getName().contains("카카오")) {
            product.setMdApproved(false);
            model.addAttribute("infoMessage", "카카오가 포함된 상품은 MD 승인 후 사용 가능합니다.");
        }

        productDao.updateProduct(product.getId(), product, product);
        return "redirect:/admin/products";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productDao.removeProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/approve/{id}")
    public String apporve(@PathVariable Long id) {
        Product product = productDao.getProductById(id);
        product.setMdApproved(true);
        productDao.updateProduct(product.getId(), product, product);
        return "redirect:/admin/products";
    }
}