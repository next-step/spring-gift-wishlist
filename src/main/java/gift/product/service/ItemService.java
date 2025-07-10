package gift.product.service;


import gift.product.entity.Item;
import gift.product.repository.ItemRepository;
import gift.product.dto.GetItemResponse;
import gift.product.dto.ItemRequest;
import gift.product.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;


@Service
@Transactional
public class ItemService {

	private final ItemRepository itemRepository;
	private final UserRepository userRepository;
	public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.userRepository = userRepository;
	}


	public Long createItem(ItemRequest req, Long userId) {
		Item item = Item.builder()
			.userId(userId)
			.name(req.name())
			.price(req.price())
			.imageUrl(req.imageUrl())
			.build();

		return itemRepository.save(item);
	}


	@Transactional(readOnly = true)
	public List<GetItemResponse> getAllItems() {
		List<Item> items = itemRepository.findAll();
		return items.stream()
			.map(item -> GetItemResponse.builder()
				.id(item.getId())
				.authorId(item.getUserId())
				.name(item.getName())
				.price(item.getPrice())
				.imageUrl(item.getImageUrl())
				.build())
			.toList();
	}


	@Transactional(readOnly = true)
	public GetItemResponse getItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		return GetItemResponse.builder()
			.id(item.getId())
			.authorId(item.getUserId())
			.name(item.getName())
			.price(item.getPrice())
			.imageUrl(item.getImageUrl())
			.build();
	}


	public GetItemResponse updateItem(Long itemId, Long userId, ItemRequest req) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		item.isItemAuthor(userId);

		Item updatedItem = Item.builder()
				.userId(userId)
				.name(req.name())
				.price(req.price())
				.imageUrl(req.imageUrl())
				.build();

		itemRepository.update(itemId, updatedItem);
		return getItem(itemId);
	}


	public void deleteItem(Long itemId, Long userId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));
		item.isItemAuthor(userId);
		itemRepository.deleteById(itemId);
	}



}
