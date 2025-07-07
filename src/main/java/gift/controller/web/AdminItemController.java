package gift.controller.web;

import gift.dto.ItemRequest;
import gift.dto.ItemResponse;
import gift.service.ItemService;
import gift.entity.Member;
import gift.login.Login;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/items")
public class AdminItemController {

    private final ItemService itemService;

    public AdminItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String listItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sortProperty,
        @RequestParam(defaultValue = "asc") String sortDirection,
        Model model
    ) {
        List<ItemResponse> items = itemService.getAllItems(page, size, sortProperty, sortDirection);
        model.addAttribute("items", items);
        return "admin/items/list";
    }

    @GetMapping("/new")
    public String newItemForm(Model model) {
        model.addAttribute("item", new ItemRequest(null, 0, null));
        return "admin/items/form";
    }

    @PostMapping
    public String createItem(
        @Valid @ModelAttribute("item") ItemRequest itemRequest,
        BindingResult bindingResult,
        @Login Member loginMember,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/items/form";
        }

        itemService.createItem(itemRequest, loginMember);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 등록되었습니다!");
        return "redirect:/admin/items";
    }

    @GetMapping("/{id}")
    public String detailItem(@PathVariable("id") Long id, Model model) {
        ItemResponse item = itemService.getItemById(id);
        model.addAttribute("item", item);
        return "admin/items/detail";
    }
    @GetMapping("/{id}/edit")
    public String editItemForm(@PathVariable("id") Long id, Model model) {
        ItemResponse item = itemService.getItemById(id);
        model.addAttribute("item",
            new ItemRequest(item.name(), item.price(), item.imageUrl()));
        model.addAttribute("itemId", id);
        return "admin/items/form";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public String updateItem(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute("item") ItemRequest itemRequest,
        BindingResult bindingResult,
        @Login Member loginMember,
        RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/items/form";
        }
        itemService.updateItem(id, itemRequest, loginMember);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 수정되었습니다!");
        return "redirect:/admin/items/" + id;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String deleteItem(
        @PathVariable("id") Long id,
        RedirectAttributes redirectAttributes
    ) {
        itemService.deleteItem(id);
        redirectAttributes.addFlashAttribute("message", "상품이 성공적으로 삭제되었습니다!");
        return "redirect:/admin/items";
    }
}