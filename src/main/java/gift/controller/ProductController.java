package gift.controller;


import gift.domain.Product;
import gift.dto.CreateProductRequest;
import gift.dto.UpdateProductRequest;
import gift.service.ProductService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
//@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public String items(Model model) {
        List<Product> products = service.findAll();
        model.addAttribute("products", products);
        return "/products";
    }

    @GetMapping("/add")
    public String addProduct(Model model) {
        model.addAttribute("product", new CreateProductRequest("",null,""));
        return "/addForm";
    }


    @PostMapping("/add")
    public String addProduct(@Validated @ModelAttribute("product") CreateProductRequest product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/addForm";
        }
        service.save(product);
        return "redirect:/api/products";
    }

    @GetMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id, Model model) {
        Product findProduct = service.findById(id);
        model.addAttribute("product", findProduct);
        return "/editForm";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Long id,
                                @Validated @ModelAttribute("product") UpdateProductRequest product,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/editForm";
        }
        service.update(id, product);
        return "redirect:/api/products";
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/api/products";
    }
}
