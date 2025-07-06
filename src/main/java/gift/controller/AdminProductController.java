package gift.controller;

import gift.dto.ProductRequestDto;
import gift.entity.Product;
import gift.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductRepository repository;

    public AdminProductController(ProductRepository repository) {

        this.repository = repository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", repository.findAll());
        return "main";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("productRequestDto", new ProductRequestDto("", null, ""));
        return "create";
    }

    @PostMapping("/new")
    public String create(@Valid @ModelAttribute("productRequestDto") ProductRequestDto dto,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create";
        }

        Product product = new Product(null, dto.getName(), dto.getPrice(), dto.getImgUrl());
        repository.create(product);
        return "redirect:/admin/products";


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = repository.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/admin/products";
        }

        ProductRequestDto dto = new ProductRequestDto(product.getName(), product.getPrice(), product.getImgUrl());
        model.addAttribute("productRequestDto", dto);
        model.addAttribute("productId", id);
        return "create";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
            @Valid @ModelAttribute("productRequestDto") ProductRequestDto dto,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("productId", id);
            return "create";
        }

        Product updated = new Product(id, dto.getName(), dto.getPrice(), dto.getImgUrl());
        repository.update(updated);
        return "redirect:/admin/products";
    }}

    @PostMapping("/{id}")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/admin/products";
    }
}
