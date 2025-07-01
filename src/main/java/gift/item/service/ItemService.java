package gift.item.service;

import gift.item.dto.CreateItemDto;
import gift.item.dto.ItemDto;
import gift.item.dto.UpdateItemDto;
import gift.item.entity.Item;
import gift.item.exception.ItemNotFoundException;
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
        Item item = new Item(null, dto.getName(), dto.getPrice(), dto.getImageUrl());
        Item savedItem = itemRepository.saveItem(item);
        return new ItemDto(savedItem);
    }

    @Transactional(readOnly = true)
    public ItemDto findItem(Long id) {
        Item item = itemRepository.findItem(id);
        if (item == null) {
            throw new ItemNotFoundException("상품이 존재하지 않습니다: " + id);
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
        Item existingitem = itemRepository.findItem(id);
        if (existingitem == null) {
            throw new ItemNotFoundException("상품을 찾을 수 없습니다"+id);
        }
        itemRepository.updateItem(id, dto);
    }

}