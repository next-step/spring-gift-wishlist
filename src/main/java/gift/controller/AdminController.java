package gift.controller;

import gift.dto.ProductCreateFormDto;
import gift.dto.ProductRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ProductUpdateFormDto;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminController {

  ProductService service;
  private final String PRODUCTS_LIST_PAGE_PATH = "/admin/products";

  public AdminController(ProductService service) {
    this.service = service;
  }

  @GetMapping("")
  public String ShowAllProduct(Model model) {
    List<ProductResponseDto> responseDtoList = service.findAllProduct();
    model.addAttribute("responseDtoList", responseDtoList);
    return "list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("CreateFormDto", new ProductCreateFormDto());
    return "createProductForm";
  }

  @PostMapping("/new")
  public String create(@Valid @ModelAttribute ProductCreateFormDto createFormDto) {
    ProductRequestDto requestDto = new ProductRequestDto(createFormDto.getName(),
        createFormDto.getPrice(),
        createFormDto.getImageUrl());
    service.createProduct(requestDto);
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

  @GetMapping("/{id}/update")
  public String updateForm(@PathVariable("id") Long id, Model model) {
    ProductResponseDto responseDto = service.findProductById(id);
    ProductUpdateFormDto updateFormDto = new ProductUpdateFormDto(responseDto.getId(), responseDto.getName(), responseDto.getPrice(), responseDto.getImageUrl());
    model.addAttribute("updateFormDto", updateFormDto);
    return "updateProductForm";
  }

  @PutMapping("/{id}/update")
  public String update(@PathVariable("id") Long id, @Valid @ModelAttribute ProductUpdateFormDto updateFormDto) {
    service.updateProduct(id, new ProductRequestDto(updateFormDto.getName(),updateFormDto.getPrice(),updateFormDto.getImageUrl()));
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

  @DeleteMapping("/{id}/delete")
  public String delete(@PathVariable("id") Long id) {
    service.deleteProduct(id);
    return "redirect:" + PRODUCTS_LIST_PAGE_PATH;
  }

}
