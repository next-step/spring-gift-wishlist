package gift.service;

import gift.dto.ItemCreateDTO;
import gift.dto.ItemDTO;
import gift.dto.ItemResponseDTO;
import gift.dto.ItemUpdateDTO;

import java.util.List;

public interface ItemService {
    ItemCreateDTO saveItem(ItemCreateDTO dto);

    List<ItemResponseDTO> getItems(String name, Integer price);

    void delete(String name);

    ItemUpdateDTO updateItem(Long id, ItemUpdateDTO dto);


    ItemDTO findById(Long id);

    void deleteById(Long id);

    List<ItemResponseDTO> getAllItems();
}
