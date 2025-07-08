package gift.product.service;


import gift.product.entity.Item;
import gift.product.entity.User;
import gift.product.repository.ItemRepository;
import gift.product.repository.ItemRepositoryImpl;
import gift.product.dto.GetItemResponse;
import gift.product.dto.ItemRequest;
import gift.product.repository.UserRepository;
import org.apache.coyote.BadRequestException;
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
		//Item item = new Item(userId, req.name(), req.price(), req.imageUrl());
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
		return items.stream().map(item -> new GetItemResponse(item.getId(), item.getName(), item.getPrice(), item.getImageUrl())).toList();
	}


	@Transactional(readOnly = true)
	public GetItemResponse getItem(Long itemId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		return new GetItemResponse(item.getId(), item.getName(), item.getPrice(), item.getImageUrl());
	}


	public GetItemResponse updateItem(Long itemId, Long userId, ItemRequest req) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 아이템입니다."));
		isItemAuthor(item.getUserId(), userId);

		Item updatedItem = Item.builder()
				.id(itemId)
				.userId(userId)
				.name(req.name())
				.price(req.price())
				.imageUrl(req.imageUrl())
				.build();

		itemRepository.update(updatedItem);
		return getItem(itemId);
	}


	public void deleteItem(Long itemId, Long userId) {
		Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("존재하지 않는 아이템입니다."));
		isItemAuthor(item.getUserId(), userId);
		itemRepository.deleteById(itemId);
	}

	private void isItemAuthor(Long itemAuthorId, Long userId) {
		if(itemAuthorId != userId){
			throw new IllegalArgumentException("작성자만 수정,삭제 가능합니다.");
		}
	}

}
