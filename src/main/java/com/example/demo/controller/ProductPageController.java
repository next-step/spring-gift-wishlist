package com.example.demo.controller;

import com.example.demo.dto.ProductRequestDto;
import com.example.demo.dto.ProductResponseDto;
import com.example.demo.dto.ProductUpdateDto;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product-page")
public class ProductPageController {

  private final ProductService productService;

  public ProductPageController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public String showList(Model model) {
    model.addAttribute("products", productService.productFindAll());
    return "product/list";
  }



  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("product", new ProductRequestDto());
    model.addAttribute("formAction", "/product-page");
    return "product/form";
  }

  @PostMapping
  public String createProduct(
      @ModelAttribute @Valid ProductRequestDto dto,
      BindingResult bindingResult,
      Model model
  ) {
      if(bindingResult.hasErrors()){
        model.addAttribute("product", dto);
        model.addAttribute("formAction", "/product-page");


        String errorMessage = bindingResult.getAllErrors().stream()
                                           .map(error -> error.getDefaultMessage())
                                           .collect(Collectors.joining("\n"));
        model.addAttribute("errorMessage", errorMessage);
        return "product/form";
      }
    productService.addProduct(dto);
    return "redirect:/product-page";
  }

  @GetMapping("/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    ProductResponseDto productToEdit = productService.productFindById(id);
    ProductUpdateDto updateDto = new ProductUpdateDto(
        productToEdit.name(),
        productToEdit.price(),
        productToEdit.imageUrl()
    );

    model.addAttribute("product", updateDto);
    model.addAttribute("formAction", "/product-page/" + id + "/edit");

    return "product/form";
  }

  @PostMapping("/{id}/edit")
  public String updateProduct(@PathVariable Long id, @ModelAttribute ProductUpdateDto dto) {
    productService.productUpdateById(id, dto);
    return "redirect:/product-page";
  }

  @PostMapping("/{id}/delete")
  public String deleteProduct(@PathVariable Long id) {
    productService.productDeleteById(id);
    return "redirect:/product-page";
  }
}
