package gift.controller;

import gift.dto.ProductForm;
import gift.entity.product.Product;
import gift.exception.ProductNotFoundExection;
import gift.service.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin/product_list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productForm", new ProductForm(null, "", null, ""));
        return "admin/product_form";
    }

    @PostMapping("/new")
    public String create(
            @Valid @ModelAttribute ProductForm productForm,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            System.out.println(bindingResult.getAllErrors());
            return "admin/product_form";
        }
        productService.createProduct(
                productForm.getName(),
                productForm.getPrice(),
                productForm.getImageUrl()
        );
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(
            @PathVariable Long id,
            Model model
    ) {
        Product p = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundExection(id));

        ProductForm form = new ProductForm(
                p.id().value(),
                p.name().value(),
                p.price().value(),
                p.imageUrl().value()
        );
        model.addAttribute("productForm", form);
        return "admin/product_form";
    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable Long id,
            @Valid @ModelAttribute ProductForm productForm,
            BindingResult bindingResult,
            HttpServletResponse response
    ) {
        if (bindingResult.hasErrors()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return "admin/product_form";
        }
        productService.updateProduct(
                id,
                productForm.getName(),
                productForm.getPrice(),
                productForm.getImageUrl()
        );
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/unhide")
    public String unhide(@PathVariable Long id) {
        productService.unhideProduct(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/hide")
    public String hide(@PathVariable Long id) {
        productService.unhideProduct(id);
        return "redirect:/admin/products";
    }
}
