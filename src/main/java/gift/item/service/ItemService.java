package gift.item.service;

import gift.global.exception.CustomException;
import gift.global.exception.ErrorCode;
import gift.item.constant.ItemConstants;
import gift.item.dto.CreateItemDto;
import gift.item.dto.ItemDto;
import gift.item.dto.UpdateItemDto;
import gift.item.entity.Item;
import gift.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public ItemDto saveItem(CreateItemDto dto) {
        validateKeyword(dto.getName());
        Item item = new Item(null, dto.getName(), dto.getPrice(), dto.getImageUrl());
        Item savedItem = itemRepository.saveItem(item);
        return new ItemDto(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemDto findItem(Long id) {
        Item item = itemRepository.findItem(id);
        if (item == null) {
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }
        return new ItemDto(item);
    }

    @Transactional(readOnly = true)
    public List<ItemDto> findAllItems() {
        List<Item> item = itemRepository.findAllItems();
        return item.stream()
                .map(ItemDto::new)
                .toList();
    }

    @Transactional
    public void deleteItem(Long id) {
        itemRepository.deleteItem(id);
    }

    @Transactional
    public void updateItem(Long id, UpdateItemDto dto) {
        validateKeyword(dto.getName());
        Item existingitem = itemRepository.findItem(id);
        if (existingitem == null) {
            throw new CustomException(ErrorCode.ITEM_NOT_FOUND);
        }
        itemRepository.updateItem(id, dto);
    }

    private void validateKeyword(String name) {
        if (name.contains(ItemConstants.BLOCKED_KEYWORD_KAKAO)) {
            throw new CustomException(ErrorCode.ITEM_KEYWORD_INVALID);
        }
    }

}
