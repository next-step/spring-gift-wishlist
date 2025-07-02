package gift.controller;


import gift.dto.ItemCreateDTO;
import gift.dto.ItemDTO;
import gift.dto.ItemResponseDTO;
import gift.dto.ItemUpdateDTO;
import gift.entity.Item;
import gift.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminItemController {

    private final ItemService itemService;

    public AdminItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    @GetMapping
    public String searchItem(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer price) {

        List<ItemResponseDTO> items;

        if(name == null && price == null){
            items = itemService.getAllItems();
        }else {
            items = itemService.getItems(name, price);
        }
        model.addAttribute("items", items);
        return "admin/list";
    }

    @PostMapping
    public String saveItem(@ModelAttribute @Valid ItemCreateDTO itemDTO) {

        itemService.saveItem(itemDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("itemDTO", new ItemCreateDTO("", 0, "", false));
        return "admin/createForm";
    }

    @PostMapping("/delete")
    public String deleteItem(@RequestParam Long id) {
        ItemDTO item = itemService.findById(id);
        if (item != null) {
            itemService.deleteById(id);
        }
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/edit")
    public String updateItem(@PathVariable Long id, @ModelAttribute @Valid ItemUpdateDTO dto) {
        itemService.updateItem(id, dto);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ItemDTO item = itemService.findById(id);
        ItemUpdateDTO dto = new ItemUpdateDTO(item);
        model.addAttribute("itemDTO", dto);
        return "admin/editForm";
    }


}
