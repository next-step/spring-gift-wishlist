package gift.service.itemService;

import gift.dto.itemDto.ItemCreateDto;
import gift.dto.itemDto.ItemDto;
import gift.dto.itemDto.ItemResponseDto;
import gift.dto.itemDto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemCreateDto saveItem(ItemCreateDto dto);

    List<ItemResponseDto> getItems(String name, Integer price);

    void delete(String name);

    ItemUpdateDto updateItem(Long id, ItemUpdateDto dto);


    ItemDto findById(Long id);

    void deleteById(Long id);

    List<ItemResponseDto> getAllItems();
}
