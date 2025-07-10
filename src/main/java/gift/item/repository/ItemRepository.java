package gift.item.repository;

import gift.item.dto.UpdateItemDto;
import gift.item.entity.Item;
import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Item saveItem(Item item);

    List<Item> findAllItems();

    Item findItem(Long id);

    void updateItem(Long id, UpdateItemDto dto);

    void deleteItem(Long id);

    Optional<Item> findById(Long id);
}
