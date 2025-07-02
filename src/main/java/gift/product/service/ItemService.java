package gift.product.service;


import gift.product.entity.Item;
import gift.product.repository.ItemRepository;
import gift.product.repository.ItemRepositoryImpl;
import gift.product.dto.GetItemResponse;
import gift.product.dto.ItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;

	public ItemService(ItemRepositoryImpl itemRepository) {this.itemRepository = itemRepository;}


	public Long createItem(ItemRequest req) {

		Item item = new Item(req.name(), req.price(), req.imageUrl());

		return itemRepository.save(item);
	}

	@Transactional(readOnly = true)
	public List<GetItemResponse> getAllItems() {

		List<Item> items = itemRepository.findAll();

		return items.stream()
			.map(
				item -> new GetItemResponse(
					item.getId(),
					item.getName(),
					item.getPrice(),
					item.getImageUrl()
				)
			).toList();
	}

	@Transactional(readOnly = true)
	public GetItemResponse getItem(Long itemId) {

		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));

		return new GetItemResponse(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
	}


	public GetItemResponse updateItem(Long itemId, ItemRequest req) {

		itemRepository.findById(itemId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));

		Item item = new Item(itemId, req.name(), req.price(), req.imageUrl());

		itemRepository.update(item);

		return getItem(itemId);
	}


	public void deleteItem(Long itemId) {

		itemRepository.findById(itemId)
			.orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));

		itemRepository.deleteById(itemId);

	}

}
