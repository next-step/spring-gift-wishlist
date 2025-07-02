package gift.service;

import gift.dto.ItemCreateDto;
import gift.dto.ItemDto;
import gift.dto.ItemResponseDto;
import gift.dto.ItemUpdateDto;

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
